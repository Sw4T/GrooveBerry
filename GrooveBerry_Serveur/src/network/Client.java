package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

import protocol.NotifierReadingQueue;

public class Client implements Runnable {

	private Socket socket; //Socket utilisé pour communiquer avec le client
	private String clientName; //Pseudo du client
	private ObjectInputStream in; 
	private ObjectOutputStream out;
	protected AtomicBoolean connect; //Booléen assurant que le client est connecté
	private Server server; //Référence au serveur principal
	
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
	
	/**
	 * Tourne en boucle tant que la connexion cliente est active.<br>
	 * Implementations pour le moment : <br>
	 * 		<b> - ReadingQueue </b>
	 */
	@Override
	public void run() {
		while (connect.get()) { //Tant que la connexion est active
			try {
				Object obj = in.readObject();
				server.execute((String) obj); 
				Object [] objs = new Object[1]; 
				objs[0] = Server.readingQueue; 
				
				NotifierReadingQueue notify = new NotifierReadingQueue(objs);
				new Thread(notify).start(); //Envoi à tous les clients du changement 
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
				connect.set(false);
				server.disconnectClient(this);
			}
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