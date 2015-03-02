package network.protocol;

import java.util.HashMap;
import java.util.Map;

/**
 * Message event
 * @author Yifan Ruan (ry222ad@student.lnu.se)
 */
public class EventHeader {
	public final static int ACK=0;
	
	public final static int PING=1;
	public final static int STUN=2;
	public final static int CONNECT=3;
	public final static int VIDEO=4;
	public final static int AUDIO=5;
	public final static int PATH=6;
	
	public final static String[] defaultEvents={"ACK","PING","STUN", "CONNECT","VIDEO","AUDIO","PATH"};

	// for registering custom events
	private static Map<String,Integer> customEvents=new HashMap<>();  
	private static int eventCount=7;
		
	public static void registerCustomEvent(String event){
		customEvents.put(event, eventCount++);
	}
	
	public static int customEvent(String event){
		if(!customEvents.containsKey(event))
			return -1;
		return customEvents.get(event);
	}
}
