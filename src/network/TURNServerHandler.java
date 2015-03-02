package network;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Map;

import network.address.Endpoint;
import network.protocol.EventHeader;
import network.protocol.Message;

import org.apache.commons.lang3.SerializationUtils;

/**
 * 
 * @author Yifan Ruan (ry222ad@student.lnu.se)
 */
public class TURNServerHandler extends ServerHandler{

	// endpoint to be relayed
	private Map<Endpoint, Endpoint> relayEndpoints;
	
	public TURNServerHandler(Map<Endpoint, Endpoint> relayEndpoints){
		this.relayEndpoints=relayEndpoints;
	}
	
	@Override
	public void handle(UDPServer server, DatagramPacket receivedPacket) {
		InetAddress remoteAddress = receivedPacket.getAddress();
		int remotePort=receivedPacket.getPort();
		Endpoint publicEndpoint=new Endpoint(remoteAddress,remotePort);
		
    	Message message=(Message) SerializationUtils.deserialize(receivedPacket.getData());    	
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
    	}

	}
}
