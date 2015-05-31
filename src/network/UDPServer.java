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
package network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import network.assist.Serializer;
import network.protocol.Message;


/**
 * 
 * @author Yifan Ruan (ry222ad@student.lnu.se)
 */
public class UDPServer implements Runnable{
	int port;
	DatagramSocket socket;
    ServerHandler serverHandler;
    
    ExecutorService executorService;

	boolean isStopped=false;
	private final int MAX_PACKET_SIZE=32000;   
    
	public UDPServer(int port,ServerHandler serverHandle){
		this.port=port;
		this.serverHandler=serverHandle;
        try {
			this.socket = new DatagramSocket(this.port);
			//this.executorService = Executors.newFixedThreadPool(50);
			this.executorService = Executors.newCachedThreadPool();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {		 
		try {
	        while (!isStopped()) {
		        byte[] receiveData = new byte[MAX_PACKET_SIZE];
		    	final DatagramPacket receivedPacket = new DatagramPacket(receiveData, receiveData.length);
        		socket.receive(receivedPacket);
            	
        		final UDPServer server=this;
        		//System.out.println("Message received!");
            	executorService.execute(new Runnable(){
            	    public void run() {
                      	 try{	    		
                     		serverHandler.handle(server,receivedPacket);
                       	 }
                       	 catch(Exception e){
                       		 e.printStackTrace();
                       	 }    	    
                   }
            	});	
	        }	        
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    public void sendMessage(Message message,InetAddress remoteAddress,int remotePort){
    	
  		//byte tempBuffer[]=SerializationUtils.serialize(message);
  		byte tempBuffer[]=Serializer.write(message);
  		final DatagramPacket sendPacket = new DatagramPacket(tempBuffer, tempBuffer.length, remoteAddress, remotePort);
  		
    	executorService.execute(new Runnable(){
    	    public void run() {
              	 try{	    		
              		 //System.out.println("Send UDP message!");
               		 socket.send(sendPacket);
               	 }
               	 catch(Exception e){
               		 e.printStackTrace();
               	 }    	    
           }
    	});	
	}
    		
    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop(){
        this.isStopped = true;
        this.socket.close();
        this.executorService.shutdown();
    }
}
