package com.example.grooveberry;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Connection {
	
	private static PrintWriter printer;
	private String serverIpAddress = "";
	private static boolean connected;
	private Socket socket;
	
	
	public Connection(String serverIpAddress) {
		this.serverIpAddress = serverIpAddress;
	}
	
	
	public void connectToServer() throws IOException {
		InetAddress serverAddr = InetAddress.getByName(serverIpAddress);
        this.socket = new Socket(serverAddr, 12345);
        Connection.connected = true;
	}
	
	public void setPrinter (PrintWriter printer) {
		Connection.printer = printer;
	}

	public static boolean getConnected () {
		return Connection.connected;
	}
	
	public void setConnected (boolean b) {
		Connection.connected = b;
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
