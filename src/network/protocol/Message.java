package network.protocol;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Message protocol
 * @author Yifan Ruan (ry222ad@student.lnu.se)
 */
public class Message implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private int messageId=-1;                   
	private boolean isReliable=false;           //message reliable or not
	private String senderId; 					// who sends message
	
	private int code;  // request or reply 
	public final static int REQUEST=0;
	public final static int REPLY=1;
	private int repliedMessageId=-1;  // if message is reply, set this field, otherwise -1
	
	private int eventHeader;                    // message event
	private byte[] payload=null;
	
	private static AtomicInteger count=new AtomicInteger(0);   // count message number
	
	public Message(String senderId,int eventHeader, byte[] payload){
		this.senderId=senderId;
		this.eventHeader=eventHeader;
		this.payload=payload;
		this.messageId=count.getAndIncrement();
	}
	
	public Message(String senderId,int eventHeader, int code, byte[] payload){
		this.senderId=senderId;
		this.eventHeader=eventHeader;
		this.code=code;
		this.payload=payload;
		this.messageId=count.getAndIncrement();
	}
	
	public Message(String senderId,int eventHeader, int code, int repliedMessageId,byte[] payload){
		this.senderId=senderId;
		this.eventHeader=eventHeader;
		this.code=code;
		this.repliedMessageId=repliedMessageId;
		this.payload=payload;
		this.messageId=count.getAndIncrement();
	}
	
	public int getCode(){
		return this.code;
	}
	
	public int getRepliedMessageId(){
		return this.repliedMessageId;
	}
	

	public void setReliable(){
		this.isReliable=true;
	}
	
	public boolean isReliable(){
		return this.isReliable;
	}
	
	public int getMessageId() {
		return messageId;
	}

	public int getEventHeader() {
		return eventHeader;
	}

	public byte[] getPayload() {
		return payload;
	}

	public String getSenderId() {
		return senderId;
	}
}
