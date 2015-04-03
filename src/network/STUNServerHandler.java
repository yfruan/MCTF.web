/*******************************************************************************
 * Copyright Yifan Ruan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package network;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import network.address.Endpoint;
import network.address.NetworkInfo;
import network.assist.Serialization;
import network.protocol.Event;
import network.protocol.Message;
import network.protocol.Payload;
import network.protocol.STUNFlag;

/**
 * 
 * @author Yifan Ruan (ry222ad@student.lnu.se)
 */
public class STUNServerHandler extends ServerHandler {
	// network info of registered users
	private Map<String, NetworkInfo> networkInfos = new ConcurrentHashMap<>();
	private final String SERVER = "STUNServer";

	@Override
	public void handle(UDPServer server, DatagramPacket receivedPacket) {

		InetAddress remoteAddress = receivedPacket.getAddress();
		int remotePort = receivedPacket.getPort();
		Endpoint publicEndpoint = new Endpoint(remoteAddress, remotePort);
		//System.out.println(publicEndpoint);

		Message message = (Message) Serialization.deserialize(receivedPacket.getData());
		int repliedMessageId = message.getMessageId();
		String senderId = message.getSenderId();

		// send ACK message
		if (message.isReliable()) {
			Message ACKMessage = new Message(SERVER, Message.ACK,repliedMessageId,null);
			server.sendMessage(ACKMessage, remoteAddress, remotePort);
		}
		
		if (message.getEvent() == Event.STUN) {
			Payload payload = (Payload)Serialization.deserialize(message.getPayload());
			
			// parse simpleSTUN message
			switch (payload.getFlag()) {

			case STUNFlag.REGISTER: {
				System.out.println("STUN REGISTER message from "+publicEndpoint);
				Endpoint privateEndpoint = (Endpoint) payload.getData();
				//System.out.println(privateEndpoint);
				NetworkInfo info = new NetworkInfo(senderId, privateEndpoint,publicEndpoint);
				networkInfos.put(senderId, info);
				break;
			}

			case STUNFlag.GETINFO: {
				System.out.println("STUN GETINFO message from "+publicEndpoint);
				String[] userIds=(String[]) payload.getData();
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
					
				reply = new Message(SERVER, Message.REPLY, repliedMessageId,
						Serialization.serialize(new Payload(STUNFlag.GETINFO, infos)));
				server.sendMessage(reply, remoteAddress, remotePort);
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
