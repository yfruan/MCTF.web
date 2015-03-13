package network;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import network.address.Endpoint;

/**
 * 
 * @author Yifan Ruan (ry222ad@student.lnu.se)
 */
public class RelayServer{

	private Map<Endpoint,Endpoint> relayEndpoints;
	private UDPServer STUNServer, TURNServer;

	public RelayServer(){
		this.relayEndpoints=new ConcurrentHashMap<>();
		this.STUNServer=new UDPServer(1000,new STUNServerHandler(relayEndpoints));
		this.TURNServer=new UDPServer(1001,new TURNServerHandler(relayEndpoints));
	}
	
	public void start(){
		System.out.println("Running STUN Server on port 1000 !!!!");
		new Thread(this.STUNServer).start();;
		System.out.println("Running TURN Server on port 1001 !!!!");
		new Thread(this.TURNServer).start();
	}
	
	public void stop(){
		this.STUNServer.stop();
		this.TURNServer.stop();
	}
    
	public static void main(String[] args){
		System.out.println("Running Relay Server!!!!");
		new RelayServer().start();
	}
}