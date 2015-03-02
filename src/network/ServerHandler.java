package network;

import java.net.DatagramPacket;

/**
 * 
 * @author Yifan Ruan (ry222ad@student.lnu.se)
 */
public abstract class ServerHandler {
	public abstract void handle(UDPServer server, DatagramPacket receivedPacket);
}
