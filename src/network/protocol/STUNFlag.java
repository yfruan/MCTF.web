package network.protocol;

/**
 * Flag of payload in "STUN" message
 * @author Yifan Ruan (ry222ad@student.lnu.se)
 */
public class STUNFlag{	
	public static final int REGISTER=0;     // registers public and private endpoints to one port
	public static final int GETINFO=1;   	// gets network info of one user
	public static final int RELAY=2;     	// asks to relay data
	public static final int UNRELAY=3;  	// not relay data
	public static final int UNREGISTER=4;   // unregisters to one port
}
	