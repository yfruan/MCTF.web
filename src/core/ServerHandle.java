package core;

import java.net.DatagramPacket;

public abstract class ServerHandle {
	public abstract void process(UDPServer server, DatagramPacket receivedPacket);
}
