package com.example.grooveberry;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.Serializable;
import java.net.Socket;

import files.FileDownload;
import files.FileUpload;

public class Client {

	private Socket socketFile; // Socket utilisé pour communiquer avec le
								// client
	private ObjectInputStream fileIn;
	private ObjectOutputStream fileOut;

	private Socket socketSimple;
	private String clientName;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private String adress = "";
	private boolean connected;

	public Client(Socket newSocketSimple, Socket newSocketFile) {
		try {
			this.socketSimple = newSocketSimple;
			this.socketFile = newSocketFile;
			this.adress = this.socketSimple.getInetAddress().getHostAddress();
			this.out = new ObjectOutputStream(socketSimple.getOutputStream());
			this.in = new ObjectInputStream(socketSimple.getInputStream());
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

	public boolean sendObject(Object toSend) {
		if (out != null) {
			try {
				// out.writeUTF(toSend);
				out.writeObject(toSend);
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

	public Object readObject() throws OptionalDataException,
			ClassNotFoundException, IOException {
		if (in != null) {
			return in.readObject();
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
		return socketSimple;
	}

	public boolean isConnected() {
		return this.connected;
	}

	public void setConnected(boolean b) {
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
	
	public ObjectOutputStream getFileOut() {
		return this.fileOut;
	}
	
	public ObjectInputStream getFileIn() {
		return this.fileIn;
	}

	public void makeFileStreams() {
		if (socketFile.isConnected() && socketFile.isBound()) {
			try {
				System.out
						.println("Client : Je me suis bien connecté au serveur ! youhou!");
				this.fileOut = new ObjectOutputStream(
						socketFile.getOutputStream());

				this.fileIn = new ObjectInputStream(socketFile.getInputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

}
