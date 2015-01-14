package protocol;

import java.io.Serializable;

public class NetworkInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String userId;
	private Endpoint privateEndpoint;
	private Endpoint publicEndpoint;
	
	public NetworkInfo(String userId, Endpoint privateEndpoint,
			Endpoint publicEndpoint) {
		this.userId = userId;
		this.privateEndpoint = privateEndpoint;
		this.publicEndpoint = publicEndpoint;
	}

	public String getUserId() {
		return userId;
	}

	public Endpoint getPrivateEndpoint() {
		return privateEndpoint;
	}

	public Endpoint getPublicEndpoint() {
		return publicEndpoint;
	}
	
}
