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

/**
 * 
 * @author Yifan Ruan (ry222ad@student.lnu.se)
 */
public class RelayServer{
	
	private UDPServer STUNServer, TURNServer;

	public RelayServer(){
		this.STUNServer=new UDPServer(1000,new STUNServerHandler());
		this.TURNServer=new UDPServer(1001,new TURNServerHandler());
	}
	
	public void start(){
		System.out.println("Running STUN Server on port 1000 !!!!");
		new Thread(this.STUNServer).start();;
		System.out.println("Running TURN Server on port 1001 !!!!");
		new Thread(this.TURNServer).start();
	}
	
	public void stop(){
		this.STUNServer.stop();
		this.TURNServer.stop();
	}
    
	public static void main(String[] args){
		System.out.println("Running Relay Server!!!!");
		new RelayServer().start();
	}
}
