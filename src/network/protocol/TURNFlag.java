package network.protocol;

/**
 * Flag of payload in "TURN" message
 * @author Yifan Ruan (ry222ad@student.lnu.se)
 */
public class TURNFlag {
	public static final int RELAY=0;     	// asks to relay data
	public static final int UNRELAY=1;  	// not relay data
}
