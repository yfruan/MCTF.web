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
import network.assist.Serialization;
import network.protocol.Event;
import network.protocol.Message;
import network.protocol.Payload;
import network.protocol.TURNFlag;

/**
 * 
 * @author Yifan Ruan (ry222ad@student.lnu.se)
 */
public class TURNServerHandler extends ServerHandler{
	// endpoint to be relayed
	private Map<Endpoint, Endpoint> relayEndpoints=new ConcurrentHashMap<>();
	private final String SERVER = "TURNServer";
	
	@Override
	public void handle(UDPServer server, DatagramPacket receivedPacket) {
		InetAddress remoteAddress = receivedPacket.getAddress();
		int remotePort=receivedPacket.getPort();
		Endpoint publicEndpoint=new Endpoint(remoteAddress,remotePort);
		
    	Message message=(Message) Serialization.deserialize(receivedPacket.getData());   
		int repliedMessageId = message.getMessageId();
		
    	if(message.getCode()!=Message.PING){
    		
    		if(message.getEvent()==Event.TURN){
    			
    			// send ACK message
    			if (message.isReliable()) {
    				Message ACKMessage = new Message(SERVER, Message.ACK,repliedMessageId,null);
    				server.sendMessage(ACKMessage, remoteAddress, remotePort);
    			}
    			
    			Payload payload = (Payload)Serialization.deserialize(message.getPayload());

    			if(payload.getFlag()==TURNFlag.RELAY){
        			System.out.println("TURN RELAY message from "+publicEndpoint);
        			Endpoint otherEndpoint = (Endpoint) payload.getData();
        			this.relayEndpoints.put(publicEndpoint, otherEndpoint);
        			this.relayEndpoints.put(otherEndpoint, publicEndpoint);
        			//System.out.println(otherEndpoint);
        			Message reply=new Message(SERVER,Message.REPLY, repliedMessageId, 
        					Serialization.serialize(new Payload(TURNFlag.RELAY, true)));
        			server.sendMessage(reply, remoteAddress, remotePort);
    			}
    			else if(payload.getFlag()==TURNFlag.UNRELAY){
    				System.out.println("TURN UNRELAY message from "+publicEndpoint);
    				if (relayEndpoints.containsKey(publicEndpoint))
    					relayEndpoints.remove(publicEndpoint);
    			}
    		}
    		else{
        		if(relayEndpoints.containsKey(publicEndpoint)){		
        			try{
        				Endpoint otherEndpoint=relayEndpoints.get(publicEndpoint);
        				System.out.println("Relay message from "+publicEndpoint+" to "+otherEndpoint);
        				server.sendMessage(message, otherEndpoint.getAddress(), otherEndpoint.getPort());
        			}
        			catch(Exception e){
        				e.printStackTrace();
        			}
        		}	
        		else
        			System.out.println("Cannot relay for "+publicEndpoint);		
    		}
    	}
	}
}
