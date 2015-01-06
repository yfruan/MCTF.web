package protocol;

import java.io.Serializable;

// store the private and public address and port of user
public class Endpoint implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String address;
	private int port;
	//private int videoPort;
	//private int audioPort;

	public Endpoint(String address, int port) {
		this.address = address;
		this.port = port;
	}
	
	public String getAddress() {
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
