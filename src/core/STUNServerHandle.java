package core;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.SerializationUtils;

import protocol.Endpoint;
import protocol.EventHeader;
import protocol.Message;
import protocol.NetworkInfo;
import protocol.STUNFlag;
import protocol.SimpleSTUN;

public class STUNServerHandle extends ServerHandle {

	// endpoint to be relayed
	private Map<Endpoint, Endpoint> relayEndpoints;
	// network info of users
	private Map<String, NetworkInfo> networkInfos;

	private final String SERVER = "STUNServer";

	public STUNServerHandle(Map<Endpoint, Endpoint> relayEndpoints) {
		this.relayEndpoints = relayEndpoints;
		this.networkInfos = new ConcurrentHashMap<>();
	}
	
	@Override
	public void process(UDPServer server, DatagramPacket receivedPacket) {

		InetAddress remoteAddress = receivedPacket.getAddress();
		int remotePort = receivedPacket.getPort();

		// System.out.println("public address: "+ remoteAddress);
		// System.out.println("public port: "+remotePort);

		Endpoint publicEndpoint = new Endpoint(remoteAddress, remotePort);
		Message message = (Message) SerializationUtils.deserialize(receivedPacket.getData());


		int repliedMessageId = message.getMessageId();
		String senderId = message.getSenderId();

		// send ACK message
		if (message.isReliable()) {
			Message ACKMessage = new Message(SERVER, EventHeader.ACK,message.getMessageId());
			server.sendMessage(ACKMessage, remoteAddress, remotePort);
		}
		
		if (message.getEventHeader() == EventHeader.STUN) {
			SimpleSTUN simpleSTUN = (SimpleSTUN) message.getPayload();
			// parse simpleSTUN message relaying on flag
			switch (simpleSTUN.getFlag()) {

			case STUNFlag.REGISTER: {
				Endpoint privateEndpoint = (Endpoint) simpleSTUN.getContent();
				// System.out.println("private address: "+privateEndpoint.getAddress());
				// System.out.println("private port: "+privateEndpoint.getPort());

				NetworkInfo info = new NetworkInfo(senderId, privateEndpoint,publicEndpoint);
				networkInfos.put(senderId, info);
				break;
			}

			case STUNFlag.GETINFO: {
				String userId = (String) simpleSTUN.getContent();
				Message reply;

				if (networkInfos.containsKey(userId)) {
					NetworkInfo info = networkInfos.get(userId);
					reply = new Message(SERVER, EventHeader.STUN,Message.REPLY, repliedMessageId, new SimpleSTUN(STUNFlag.GETINFO, info));
				} else
					reply = new Message(SERVER, EventHeader.STUN,Message.REPLY, repliedMessageId, new SimpleSTUN(STUNFlag.GETINFO, null));
				server.sendMessage(reply, remoteAddress, remotePort);
				break;
			}

			case STUNFlag.RELAY: {
				String userId = (String) simpleSTUN.getContent();

				System.out.println(publicEndpoint);

				if (networkInfos.containsKey(userId)) {
					NetworkInfo info = networkInfos.get(userId);
					Endpoint otherEndpoint = info.getPublicEndpoint();
					relayEndpoints.put(publicEndpoint, otherEndpoint);

					Message reply = new Message(SERVER, EventHeader.STUN,Message.REPLY, repliedMessageId, new SimpleSTUN(STUNFlag.RELAY, true));
					server.sendMessage(reply, remoteAddress, remotePort);
				}
				break;
			}
			case STUNFlag.UNREGISTER: {
				if (networkInfos.containsKey(senderId)) {
					NetworkInfo info = networkInfos.get(senderId);
					networkInfos.remove(senderId);
					if (relayEndpoints.containsKey(info.getPublicEndpoint())) {
						relayEndpoints.remove(info.getPublicEndpoint());
					}
				}
				break;
			}

			default: {
				System.out.println("Wrong flag!!!!!");
				break;
				}
			}
		}
	}

}
