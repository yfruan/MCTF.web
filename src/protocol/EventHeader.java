package protocol;

public interface EventHeader {
	public final static int TEXT=0;
	public final static int CONNECT=1;
	public final static int VIDEO=2;
	public final static int AUDIO=3;
	public final static int DRAW=4;
	public final static int STUN=5;
	public final static int PING=6;     // test the connection
	public final static int ACK=7;      // acknowledge for receiving message
}
