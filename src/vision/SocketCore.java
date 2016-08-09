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
	public ServerSocket visionServer;
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
				visionServer = new ServerSocket(VisionConfig.port);
				clientSocket = visionServer.accept();
				init=true;
				out = new PrintWriter(clientSocket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				System.out.println("connected");
			} catch(Exception e) {
				System.out.println("visionServer initialization threw exception");
			}
		}
		
		try {
			while(true) {
				out.println(Integer.toString(updateNumber));
				xml = in.readLine();
				System.out.println(xml);
				updateNumber++;
				Thread.sleep(50);
			} 
		} catch(Exception e) {
			
		}
		init = true;
	}
	
	public String getXML(){
		return xml;
	}
	
	public void setInit(boolean init) {
		this.init = init;
	}
	
	public boolean getSocketStatus() {
		try {
			return lostConnection ? lostConnection : visionSocket.isConnected();			
		} catch (Exception e) {
			return false;
		}
	}
	
	public void initClient() {
		try {
			visionSocket = new Socket(VisionConfig.hostName, VisionConfig.port);
			out = new PrintWriter(visionSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(visionSocket.getInputStream()));	
			out.println("connected");
			
		} catch(UnknownHostException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void initServer() {
		try {
			visionServer = new ServerSocket(VisionConfig.port);
			Socket clientSocket = visionServer.accept();
			out = new PrintWriter(clientSocket.getOutputStream(), true);
		    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		} catch(Exception e) {
			
		}
	}
	
	public void connectServer () {
		try {
			visionServer.close();
			visionServer = new ServerSocket(VisionConfig.port);
			Socket clientSocket = visionServer.accept();
			out = new PrintWriter(clientSocket.getOutputStream(), true);
		    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		} catch(Exception e) {
			
		}
	}
	
	public void update() {
		try{
			out.println(Integer.toString(updateNumber));
			xml = in.readLine();
			updateNumber++;
		} catch (IOException e) {
			lostConnection = true;
			System.out.println("ioexception");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("exception");
		}
	}
}
