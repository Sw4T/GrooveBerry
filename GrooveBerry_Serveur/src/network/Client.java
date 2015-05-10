package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicBoolean;

import protocol.Notifier;
import protocol.NotifierReadingQueue;
import protocol.NotifierVolume;
import protocol.Protocol;
import files.ReadingQueue;

public class Client implements Runnable {

	private Socket socketSimple; //Socket utilisé pour communiquer avec le client
	private Socket socketFile; //Socket utilisé pour communiquer avec le client
	private ObjectInputStream fileIn; 
	private ObjectOutputStream fileOut;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	private String clientName; //Pseudo du client
	protected AtomicBoolean connect; //Booléen assurant que le client est connecté
	private Server server; //Référence au serveur principal
	
	public Client(Socket socketSimple, Socket socketObject, Server server) {
		try {
			this.socketSimple = socketSimple;
			this.socketFile = socketObject;
			this.fileOut = new ObjectOutputStream(socketObject.getOutputStream());
			this.fileIn = new ObjectInputStream(socketObject.getInputStream());
			this.connect = new AtomicBoolean(true);
			this.server = server;
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Constructeur utilis� pour les tests
	 * @param socketSimple
	 * @throws IOException
	 */
	public Client(Socket socketSimple) throws IOException {
		this.socketSimple = socketSimple;
		this.fileOut = new ObjectOutputStream(socketSimple.getOutputStream());
		this.fileIn = new ObjectInputStream(socketSimple.getInputStream());
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
				
				Object obj = in.readObject();
				Protocol protocolUsed = execute(obj);
				Object [] toSend = new Object[2]; //Objets allant être envoyé aux clients
				Notifier threadNotifier = null;
				
				if (protocolUsed == Protocol.MODIFY_READING_QUEUE) 
					threadNotifier = new NotifierReadingQueue(toSend);
				else {
					toSend[0] = Server.getInstance().getMasterVolume();
					threadNotifier = new NotifierVolume(toSend);
				}
					
				new Thread(threadNotifier).start(); //Envoi à tous les clients du changement 
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
			execute(constant);
		} while (!constant.equals("exit"));
		System.out.println("Fin du traitement client " + getSocket());
	}
	
	public synchronized Protocol execute(Object constant) 
	{
		String commande = null;
		ReadingQueue readingQueue = Server.getInstance().getReadingQueue();
		if (constant instanceof String) {
			StringTokenizer stringTokenizer = new StringTokenizer((String) constant, "$");
			commande = stringTokenizer.nextToken();
			switch (commande)
			{
				case "play" : readingQueue.getCurrentTrack().play(); break;
				case "pause" : readingQueue.getCurrentTrack().pause(); break;
				case "mute" : readingQueue.getCurrentTrack().mute(); break;
				case "restart" : readingQueue.getCurrentTrack().restart(); break;
				case "stop" : readingQueue.getCurrentTrack().stop(); break;
				case "loop" : readingQueue.getCurrentTrack().loop(); break;
				case "next" : readingQueue.next(); break;
				case "prev" : readingQueue.prev(); break;
				case "random" : readingQueue.rand(); break;
				case "+" : Server.volControl.increaseVolume(); break;
				case "-" : Server.volControl.decreaseVolume(); break;
				case "download" : 
					if (stringTokenizer.hasMoreTokens()) {
						String filePath = stringTokenizer.nextToken();
						uploadFile(filePath);
					}
					break;
				case "upload" : 
					if (stringTokenizer.hasMoreTokens()) {
						String filePath = stringTokenizer.nextToken();
						downloadFile(filePath);
					}; break;
				default :
			}
			
		}
		if (constant instanceof Integer) {
			readingQueue.setCurrentTrackPostion((Integer) constant);
			readingQueue.getCurrentTrack().play();
		}
		System.out.println("Received " + constant + " from the client, processing...");
		/*if (commande.equals("+") || commande.equals("-"))
			return (Protocol.MODIFY_VOLUME);
		else
			return (Protocol.MODIFY_READING_QUEUE);*/
		return (Protocol.MODIFY_READING_QUEUE);
	}
	
	private void uploadFile(String filePath) {
		try {
			fileOut.writeObject("#OK");
			new Thread(new FileUpload(fileOut, filePath)).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void downloadFile(String filePath) {
		try {
			fileOut.writeObject("#OK");
			new Thread(new FileDownload(fileIn)).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	public void close() {
		try {
			this.fileIn.close();
			this.fileOut.close();
			this.in.close();
			this.out.close();
			this.socketFile.close();
			this.socketSimple.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setBuffers(ObjectOutputStream printer, ObjectInputStream bufferIn) {
		this.in = bufferIn;
		this.out = printer;
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