package protocol;

import java.io.Serializable;
import java.net.InetAddress;

// store the private and public address and port of user
public class Endpoint implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private InetAddress address;
	private int port;

	public Endpoint(InetAddress address, int port) {
		this.address = address;
		this.port = port;
	}
	
	public InetAddress getAddress() {
		return address;
	}
	
	public int getPort() {
		return port;
	}
	
	@Override
	public int hashCode() {
		 return address.hashCode()+port;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Endpoint){
			Endpoint endpoint=(Endpoint) obj;
	    	if(endpoint.getAddress().equals(this.address) && endpoint.getPort()==this.port)
	    		return true;
	    }
	    return false;
	}
	
	public String toString(){
		return address+" : "+port;
	}
	
}
