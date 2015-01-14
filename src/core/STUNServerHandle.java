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
	// network info of registered users
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
		//System.out.println(publicEndpoint);

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
				System.out.println("STUN REGISTER message from "+publicEndpoint);
				Endpoint privateEndpoint = (Endpoint) simpleSTUN.getContent();
				// System.out.println("private address: "+privateEndpoint.getAddress());
				// System.out.println("private port: "+privateEndpoint.getPort());

				NetworkInfo info = new NetworkInfo(senderId, privateEndpoint,publicEndpoint);
				networkInfos.put(senderId, info);
				break;
			}

			case STUNFlag.GETINFO: {
				System.out.println("STUN GETINFO message from "+publicEndpoint);
				String[] userIds=(String[]) simpleSTUN.getContent();
				Message reply;
				int length=userIds.length;
				NetworkInfo[] infos=new NetworkInfo[length];
				
				String userId;
				for(int i=0;i<length;i++){
					userId=userIds[i];
					if (networkInfos.containsKey(userId)) {
						infos[i]=(networkInfos.get(userId));
					}
					else
						infos[i]=new NetworkInfo(userId,null,null);
				}
					
				reply = new Message(SERVER, EventHeader.STUN,Message.REPLY, repliedMessageId, new SimpleSTUN(STUNFlag.GETINFO, infos));
				server.sendMessage(reply, remoteAddress, remotePort);
				break;
			}

			case STUNFlag.RELAY: {
				System.out.println("STUN RELAY message from "+publicEndpoint);
				String userId = (String) simpleSTUN.getContent();

				//System.out.println(publicEndpoint);
				Message reply;
				if (networkInfos.containsKey(userId)) {
					NetworkInfo info = networkInfos.get(userId);
					Endpoint otherEndpoint = info.getPublicEndpoint();
					relayEndpoints.put(publicEndpoint, otherEndpoint);
					relayEndpoints.put(otherEndpoint, publicEndpoint);
					reply = new Message(SERVER, EventHeader.STUN,Message.REPLY, repliedMessageId, new SimpleSTUN(STUNFlag.RELAY, true));
				}
				else
					reply = new Message(SERVER, EventHeader.STUN,Message.REPLY, repliedMessageId, new SimpleSTUN(STUNFlag.RELAY, false));
				server.sendMessage(reply, remoteAddress, remotePort);
				break;
			}
			
			case STUNFlag.UNRELAY: {
				System.out.println("STUN UNRELAY message from "+publicEndpoint);
				if (relayEndpoints.containsKey(publicEndpoint))
					relayEndpoints.remove(publicEndpoint);
				break;
			}
			
			case STUNFlag.UNREGISTER: {
				System.out.println("STUN UNREGISTER message from "+publicEndpoint);
				if (networkInfos.containsKey(senderId))
					networkInfos.remove(senderId);
				break;
			}

			default: {
				System.out.println("Wrong STUN message !!!!!");
				break;
				}
			}
		}
	}

}
