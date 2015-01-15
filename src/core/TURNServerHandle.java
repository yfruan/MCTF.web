package core;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Map;

import org.apache.commons.lang3.SerializationUtils;

import protocol.Endpoint;
import protocol.EventHeader;
import protocol.Message;

public class TURNServerHandle extends ServerHandle{

	// endpoint to be relayed
	private Map<Endpoint, Endpoint> relayEndpoints;
	
	public TURNServerHandle(Map<Endpoint, Endpoint> relayEndpoints){
		this.relayEndpoints=relayEndpoints;
	}
	
	@Override
	public void process(UDPServer server, DatagramPacket receivedPacket) {
		InetAddress remoteAddress = receivedPacket.getAddress();
		int remotePort=receivedPacket.getPort();
		
		System.out.println(remoteAddress);
		//System.out.println("public address: "+ remoteAddress);
		//System.out.println("public port: "+remotePort);
		//Endpoint publicEndpoint=new Endpoint(remoteAddress,remotePort);
    	//Message message=(Message) SerializationUtils.deserialize(receivedPacket.getData());
    	   
    	//System.out.println("Receiving message!!");
    	
    	/*
    	if(message.getEventHeader()!=EventHeader.PING){
    		
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
    		
    	}*/
	}
}
