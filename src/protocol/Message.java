package protocol;

import java.io.Serializable;

public class Message implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int messageId=-1;
	private boolean isReliable=false;
	private String senderId;
	private int eventHeader; 
	
	private int code;  // request, reply 
	private int repliedMessageId=-1;  // if the message is reply, set this field, otherwise -1
	
	private Object payload=null;
	
	private static int count=0;
	
	public final static int REQUEST=0;
	public final static int REPLY=1;
	
	/*
	public Message(int eventHeader, Object payload){
		this.eventHeader=eventHeader;
		this.payload=payload;
		this.messageId=count;
		count++;
	}*/
	
	public Message(String senderId,int eventHeader, Object payload){
		this.senderId=senderId;
		this.eventHeader=eventHeader;
		this.payload=payload;
		this.messageId=count;
		count++;
	}
	
	public Message(String senderId,int eventHeader, int code, Object payload){
		this.senderId=senderId;
		this.eventHeader=eventHeader;
		this.code=code;
		this.payload=payload;
		this.messageId=count;
		count++;
	}
	
	public Message(String senderId,int eventHeader, int code, int repliedMessageId,Object payload){
		this.senderId=senderId;
		this.eventHeader=eventHeader;
		this.code=code;
		this.repliedMessageId=repliedMessageId;
		this.payload=payload;
		this.messageId=count;
		count++;
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

	public Object getPayload() {
		return payload;
	}

	public String getSenderId() {
		return senderId;
	}
}
