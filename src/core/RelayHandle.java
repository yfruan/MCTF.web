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


public class RelayHandle {
	
	// endpoint to be relayed
	private static Map<Endpoint,Endpoint> relayEndpoints=new ConcurrentHashMap<>();
	// network info of users
	private static Map<String, NetworkInfo> networkInfos=new ConcurrentHashMap<>(); 
	private static Map<Endpoint,Long> timeStamps=new ConcurrentHashMap<>(); 
	private static final String SERVER="RelayServer";
	
	public void process(RelayServer UDPServer, DatagramPacket receivedPacket){
		
		InetAddress remoteAddress = receivedPacket.getAddress();
		int remotePort=receivedPacket.getPort();
		//System.out.println("public address: "+ remoteAddress);
		//System.out.println("public port: "+remotePort);

		Endpoint publicEndpoint=new Endpoint(remoteAddress.getHostName(),
				remotePort);
		
		if(!relayEndpoints.containsKey(publicEndpoint)){		
			
        	Message message=(Message) SerializationUtils.deserialize(receivedPacket.getData());
			int repliedMessageId=message.getMessageId();
			String senderId=message.getSenderId(); 
			
			// send ACK message
			if(message.isReliable()){
				Message ACKMessage=new Message(SERVER,EventHeader.ACK, message.getMessageId());
				UDPServer.sendMessage(ACKMessage, remoteAddress, remotePort);
			}
			if(message.getEventHeader()==EventHeader.STUN){
				SimpleSTUN simpleSTUN=(SimpleSTUN) message.getPayload();
				// parse simpleSTUN message relaying on flag
				switch(simpleSTUN.getFlag()){
				
					case STUNFlag.REGISTER:{
						Endpoint privateEndpoint=(Endpoint)simpleSTUN.getContent();
						System.out.println("private address: "+privateEndpoint.getAddress());
						System.out.println("private port: "+privateEndpoint.getPort());
						
						NetworkInfo info=new NetworkInfo(senderId,privateEndpoint,publicEndpoint);
						networkInfos.put(senderId, info);
						break;
					}
					
					case STUNFlag.GETINFO:{
						String userId=(String) simpleSTUN.getContent();
						Message reply;
																			
						if(networkInfos.containsKey(userId)){					
							NetworkInfo info=networkInfos.get(userId);
							reply=new Message(SERVER,EventHeader.STUN,Message.REPLY,repliedMessageId, new SimpleSTUN(STUNFlag.GETINFO,info));					
						}
						else
							reply=new Message(SERVER,EventHeader.STUN,Message.REPLY,repliedMessageId,new SimpleSTUN(STUNFlag.GETINFO,null));
						
						UDPServer.sendMessage(reply, remoteAddress, remotePort);
						break;
					}
					
					case STUNFlag.RELAY:{
						String userId=(String) simpleSTUN.getContent();
						if(networkInfos.containsKey(userId)){
							
							NetworkInfo info=networkInfos.get(userId);
							Endpoint otherEndpoint=info.getPublicEndpoint();	
							relayEndpoints.put(publicEndpoint, otherEndpoint);
									
							Message reply=new Message(SERVER,EventHeader.STUN,Message.REPLY,repliedMessageId,new SimpleSTUN(STUNFlag.RELAY,true));						
							UDPServer.sendMessage(reply, remoteAddress, remotePort);
						}
						break;
					}
					
					/*
					case STUNFlag.KEEPALIVE:{
						break;
					}*/
					
					case STUNFlag.UNREGISTER:{
						if(networkInfos.containsKey(senderId)){
							NetworkInfo info=networkInfos.get(senderId);
							networkInfos.remove(senderId);
							if(relayEndpoints.containsKey(info.getPublicEndpoint())){
								relayEndpoints.remove(info.getPublicEndpoint());
							}
						}
						break;
					}
					
					default:{
						System.out.println("Wrong flag!!!!!");
						break;
					}
				}
				
			}
			
		}
		else{
			
			try{
				System.out.println("Relay message!!");
			
				timeStamps.put(publicEndpoint,System.currentTimeMillis());
				Endpoint otherEndpoint=relayEndpoints.get(publicEndpoint);
						
				//System.out.println(publicEndpoint);
				System.out.println(otherEndpoint);
			
				UDPServer.sendMessage(receivedPacket.getData(), InetAddress.getByName(otherEndpoint.getAddress()), otherEndpoint.getPort());
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}	        
	}
}
