package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class Client implements Runnable {

	private Socket socket;
	private String clientName;
	//private Reception in;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private AtomicBoolean connect; 
	private Server server;
	
	public Client(Socket newSocket) {
		try {
			this.socket = newSocket;
			this.out = new ObjectOutputStream(socket.getOutputStream());
			this.in = new ObjectInputStream(socket.getInputStream());
			this.connect = new AtomicBoolean(true);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public Client(Socket newSocket, Server server) {
		this(newSocket);
		this.server = server;
	}
	
	@Override
	public void run() {
		while (connect.get()) {
			getTreatmentFromRemote();
			connect.set(false);
			server.disconnectClient(this);
		}
	}
	
	//Reçoit des chaines de caractères venant du client, tant que "exit" n'a pas été reçu
	public synchronized void getTreatmentFromRemote() 
	{
		String constant;
		do {
			constant = readString();
			if (constant == null)
				return;
			server.execute(constant);
		} while (!constant.equals("exit"));
		System.out.println("Fin du traitement client " + getSocket());
	}
		
	public boolean sendSerializable(Serializable toSend) {
		if (out != null) {
			try {
				out.writeObject(toSend);
				out.flush();
				out.reset();
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
				out.reset();
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
	
	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (!(o instanceof Client))
			return false;
		if (((Client) o).getSocket().getInetAddress().getHostAddress().equals(this.socket.getInetAddress().getHostAddress()))
			return true;
		return false;
	}
}
