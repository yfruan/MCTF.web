package protocol;

public class STUNFlag {	
	public static final int REGISTER=0;     // registers public and private endpoints to one port
	public static final int GETINFO=1;   	// gets network info of one user
	//public static final int PING=2;       // ask both sides to ping to each other fot hole punching
	public static final int RELAY=3;     	// asks to exchange data
	//public static final int KEEPALIVE=4;  	// 
	public static final int UNREGISTER=5;   // unregisters to one port
}
	