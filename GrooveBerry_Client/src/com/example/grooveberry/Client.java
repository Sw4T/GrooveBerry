package com.example.grooveberry;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class Client {


	private Socket socket;
	private String clientName;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private String adress = "";
	private boolean connected;
	
	public Client(Socket newSocket) {
		try {
			this.socket = newSocket;
			this.adress = this.socket.getInetAddress().getHostAddress();
			this.out = new ObjectOutputStream(socket.getOutputStream());
			this.in = new ObjectInputStream(socket.getInputStream());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public boolean sendSerializable(Serializable toSend) {
		if (out != null) {
			try {
				out.writeObject(toSend);
				out.flush();
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public boolean sendString(String toSend) {
		if (out != null) {
			try {
				out.writeUTF(toSend);
				out.flush();
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public Serializable readSerializable() {
		if (in != null) {
			try {
				return ((Serializable) in.readObject());
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public String readString() {
		if (in != null) {
			try {
				return in.readUTF();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public ObjectInputStream getIn() {
		return in;
	}

	public ObjectOutputStream getOut() {
		return out;
	}

	public Socket getSocket() {
		return socket;
	}
	
	public boolean isConnected() {
		return this.connected;
	}
	
	public void setConnected (boolean b) {
		this.connected = b;
	}
	
	public void setIn(ObjectInputStream i) {
		in = i;
	}

	public void setOut(ObjectOutputStream o) {
		out = o;
	}
	
	public String getAdress() {
		return this.adress;
	}
	
}
