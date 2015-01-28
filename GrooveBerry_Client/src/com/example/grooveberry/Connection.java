package com.example.grooveberry;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Connection {
	
	private static PrintWriter printer;
	//private static String filePlaying = "audio/Bob Marley - Jammin.mp3";
	private String serverIpAddress = "";
	private boolean connected;
	private Socket socket;
	
	
	public Connection(String serverIpAddress) {
		this.serverIpAddress = serverIpAddress;
	}
	
	
	public void connectToServer() throws IOException {
		InetAddress serverAddr = InetAddress.getByName(serverIpAddress);
        this.socket = new Socket(serverAddr, 12345);
        connected = true;
	}
	
	public void setPrinter (PrintWriter printer) {
		Connection.printer = printer;
	}

	public boolean getConnected () {
		return this.connected;
	}
	
	public void setConnected (boolean b) {
		this.connected = b;
	}
	
	public static PrintWriter getPrinter() {
		return printer;
	}

	
	
	public String getServerIpAddress() {
		return serverIpAddress;
	}
	
	public Socket getSocket () {
		return this.socket;
	}
	
}
