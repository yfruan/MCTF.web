/*******************************************************************************
 * Copyright Yifan Ruan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package network.protocol;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Definition of the formatted message to be delivered
 * @author Yifan Ruan (ry222ad@student.lnu.se)
 */
public class Message implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private boolean isReliable=false;			// the message reliable or not
	private int messageId=-1;					// the unique identifier                   
	private String senderId;					// who sending the message
	
	private int code=-1;       					// message type
	public final static int GENERAL=0;
	public final static int ACK=1;
	public final static int PING=2;
	public final static int REPLY=3;
	private int repliedMessageId=-1;  			// if type of the message is REPLAY or ACK, set this field, otherwise equal to-1
	
	private int event=-1;      					// message event
	private byte[] payload=null;				// actual data 
	
	private static AtomicInteger count=new AtomicInteger(0);   // message number count
	
	public Message(String senderId,int event, byte[] payload){
		this.senderId=senderId;
		this.code=Message.GENERAL;
		this.event=event;
		this.payload=payload;
		this.messageId=count.getAndIncrement();
	}
	
	public Message(String senderId, int code, int repliedMessageId, byte[] payload){
		this.senderId=senderId;
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

	public int getEvent() {
		return event;
	}

	public byte[] getPayload() {
		return payload;
	}

	public String getSenderId() {
		return senderId;
	}
}
