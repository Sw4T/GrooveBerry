package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

import protocol.NotifierReadingQueue;

public class Client implements Runnable {

	private Socket socketSimple; //Socket utilisé pour communiquer avec le client
	private Socket socketObject; //Socket utilisé pour communiquer avec le client
	private ObjectInputStream objectIn; 
	private ObjectOutputStream objectOut;
	private BufferedReader in;
	private PrintWriter out;
	
	private String clientName; //Pseudo du client
	protected AtomicBoolean connect; //Booléen assurant que le client est connecté
	private Server server; //Référence au serveur principal
	
	public Client(Socket socketSimple, Socket socketObject, Server server) {
		try {
			this.socketSimple = socketSimple;
			this.socketObject = socketObject;
			this.objectOut = new ObjectOutputStream(socketSimple.getOutputStream());
			this.objectIn = new ObjectInputStream(socketSimple.getInputStream());
			this.connect = new AtomicBoolean(true);
			this.server = server;
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public Client(Socket socketSimple) throws IOException {
		this.socketSimple = socketSimple;
		this.objectOut = new ObjectOutputStream(socketSimple.getOutputStream());
		this.objectIn = new ObjectInputStream(socketSimple.getInputStream());
		this.connect = new AtomicBoolean(true);
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
				Object obj = objectIn.readObject();
				server.execute(obj);
				Object [] objs = new Object[1]; 
				objs[0] = Server.readingQueue;
				
				NotifierReadingQueue notify = new NotifierReadingQueue(objs);
				new Thread(notify).start(); //Envoi à tous les clients du changement 
			} catch (ClassNotFoundException | IOException e) {
				this.close();
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
		
	public void close() {
		try {
			this.objectIn.close();
			this.objectOut.close();
			this.in.close();
			this.out.close();
			this.socketObject.close();
			this.socketSimple.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setBuffers(PrintWriter printer, BufferedReader bufferIn) {
		this.in = bufferIn;
		this.out = printer;
	}
	
	public boolean sendSerializable(Serializable toSend) {
		if (objectOut != null) {
			try {
				objectOut.writeObject(toSend);
				objectOut.flush();
				objectOut.reset();
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public boolean sendString(String toSend) {
		if (objectOut != null) {
			try {
				objectOut.writeUTF(toSend);
				objectOut.flush();
				objectOut.reset();
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public Serializable readSerializable() {
		if (objectIn != null) {
			try {
				return ((Serializable) objectIn.readObject());
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public String readString() {
		if (objectIn != null) {
			try {
				return objectIn.readUTF();
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
		return objectIn;
	}

	public ObjectOutputStream getOut() {
		return objectOut;
	}

	public Socket getSocket() {
		return socketSimple;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (!(o instanceof Client))
			return false;
		if (((Client) o).getSocket().getInetAddress().getHostAddress().equals(this.socketSimple.getInetAddress().getHostAddress()))
			return true;
		return false;
	}

}