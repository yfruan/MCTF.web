package core;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.SerializationUtils;

import protocol.Message;

public class UDPServer implements Runnable{
	int port;
	DatagramSocket socket;
    ServerHandle serverHandle;
    
    ExecutorService executorService;

	boolean isStopped=false;
	private final int MAXPACKETSIZE=32000;   
    
	public UDPServer(int port,ServerHandle serverHandle){
		this.port=port;
		this.serverHandle=serverHandle;
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
		        byte[] receiveData = new byte[MAXPACKETSIZE];
		    	DatagramPacket receivedPacket = new DatagramPacket(receiveData, receiveData.length);
        		socket.receive(receivedPacket);
            	
        		System.out.println("Message received!");
        		
        		serverHandle.process(this,receivedPacket);
	        }	        
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    public void sendMessage(Message message,InetAddress remoteAddress,int remotePort){
    	
  		byte tempBuffer[]=SerializationUtils.serialize(message);
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
    
    public void sendMessage(byte[] data,InetAddress remoteAddress,int remotePort){
  		final DatagramPacket sendPacket = new DatagramPacket(data, data.length, remoteAddress, remotePort);
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
