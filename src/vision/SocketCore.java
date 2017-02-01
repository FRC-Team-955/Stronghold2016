package vision;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import config.VisionConfig;

public class SocketCore implements Runnable {
	public Socket visionSocket;
	Socket clientSocket;
	PrintWriter out;
	BufferedReader in;
	boolean lostConnection = false;
	int updateNumber = 0;
	private boolean init = false;
	
	String xml = "<?xml version=\"1.0\"?><vision frameNumber = \"0\"></vision>";
	
	/**
	 * Runs in separate thread to handle communication efficiently 
	 */
	public void run() {
		if(!init){
			try {
				System.out.println("starting init");
				visionSocket = new Socket(config.VisionConfig.hostName, config.VisionConfig.port);
				System.out.println("socket created");
				out = new PrintWriter(visionSocket.getOutputStream(), true);
				System.out.println("out created");
				in = new BufferedReader(new InputStreamReader(visionSocket.getInputStream()));	
				System.out.println("in created.connected");
				out.println("connected");
			
				init = true;
				System.out.println("connected");
			} catch(Exception e) {
				System.out.println("visionServer initialization threw exception");
			}
		}
		
		try {
			while(true) {
				out.println(Integer.toString(updateNumber));
				try {
					String input = in.readLine();
					xml = input;						
					//String test = in.readLine();					
				} catch (Exception e) {
					//System.out.println("not a double");
				}
				//System.out.println(test);
				//System.out.println(distance);
				updateNumber++;
				//Thread.sleep(50);
			} 
		} catch(Exception e) {
			System.out.println("Vision updating threw exception");
		}
		init = true;
	}
	
	public String getXML(){
		return xml;
	}
}
