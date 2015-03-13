package network.protocol;

import java.util.HashMap;
import java.util.Map;

/**
 * Message event
 * @author Yifan Ruan (ry222ad@student.lnu.se)
 */
public class Event {
	public final static int STUN=0;
	public final static int CONNECT=1;
	public final static int VIDEO=2;
	public final static int AUDIO=3;
	public final static int PATH=4;
	
	public final static String[] defaultEvents={"STUN", "CONNECT","VIDEO","AUDIO","PATH"};

	// for registering custom events
	private static Map<String,Integer> customEvents=new HashMap<>();  
	private static int eventCount=5;
		
	public static int registerCustomEvent(String event){
		customEvents.put(event, eventCount);
		return eventCount++;
	}
	
	public static int customEvent(String event){
		if(!customEvents.containsKey(event))
			return -1;
		return customEvents.get(event);
	}
}
