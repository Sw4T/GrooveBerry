package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import files.AudioFile;
import files.AudioFileScanner;
import files.Library;
import files.ReadingQueue;
import files.SystemVolumeController;

public class Server {

	public static final int SERVER_PORT_SIMPLE = 12347; 
	public static final int SERVER_PORT_OBJECT = 12348;
	private static final int NB_MAX_CLIENTS = 5; //Nombre de clients supportÈs au maximum
	public static ArrayList<Client> listClients; //Liste de clients se connectant au serveur
	public static ReadingQueue readingQueue; //Liste de lecture
	public static SystemVolumeController volControl;
	
	private ServerSocket serverSocketSimple; //Classe g√©rant les connexions entrantes et l'envoi de chaines 
	private ServerSocket serverSocketObject; //Classe gÈrant l'envoi/rÈception d'objets plus lourds
	private Client currentClient; //Pour effectuer les tests
	
	public Server() throws IOException {
		serverSocketSimple = new ServerSocket(SERVER_PORT_SIMPLE);
		serverSocketObject = new ServerSocket(SERVER_PORT_OBJECT);
		listClients = new ArrayList<Client>(NB_MAX_CLIENTS);
		readingQueue = new ReadingQueue();
		volControl = new SystemVolumeController();
		initReadingQueue();
	}
	
	//Attente d'une connexion cliente et traitement de test
	public void waitConnection() throws IOException, InterruptedException {
		while (true) {
			Socket newSocketSimple = serverSocketSimple.accept();
			if (listClients.size() != NB_MAX_CLIENTS) 
			{
				System.out.println("Client " + newSocketSimple.getInetAddress() + " has connected !");
				Socket newSocketObject = serverSocketObject.accept();
				final Client newClient = new Client(newSocketSimple, newSocketObject, this);
				
				//updateClientList(newClient);
				listClients.add(newClient); //TODO Clients illimit√©s pour test
				sendReadingQueueToRemote(newClient); //Envoi de la liste de lecture du serveur
				new Thread(newClient).start();
			} else {
				newSocketSimple.close();
				System.out.println("ERREUR : Trop de connexions sont ouvertes sur le serveur !");
			}
		}
	}
	
	//Utilisation d'un nouveau thread pour permettre d'effectuer les tests en parall√®le
	public void waitConnectionForTest() {
		new Thread(new Runnable() {
			@Override
			public void run() {
					while (true) {
						try {
							Socket socket = serverSocketSimple.accept();
							System.out.println("SERVEUR : Client " + socket.getInetAddress() + " has connected !");
							currentClient = new Client(socket);
						} catch (IOException e) {
							e.printStackTrace();
						} 
					}
			}
		}).start();
	}
	
	public void initReadingQueue() {
		AudioFileScanner directoryScanneur = new AudioFileScanner("audio/");
		Library library;
		try {
			library = new Library(directoryScanneur.getAudioFileList());
			//Lit toute la bibliotheque
			for (AudioFile audioFile : library.getAudioFileList()) {
				readingQueue.addLast(audioFile);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void sendReadingQueueToRemote(Client c) 
	{
		c.sendSerializable("#RQ"); //Constante pour reading queue
		String rep = (String) c.readSerializable();
		if (rep.equals("#OK")) {
			System.out.println("Client OK pour l'envoi de la reading queue");
			if (c.sendSerializable(Server.readingQueue)) {
				System.out.println("Envoi de la reading queue OK...");
			}
		} else
			System.out.println("Erreur lors de l'envoi de la reading queue");
	}
	
	public synchronized void execute(Object constant) 
	{
		if (constant instanceof String) {
			switch ((String)constant)
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
				case "+" : System.out.println("monte le son !!");
							Server.volControl.increaseVolume(); break;
				case "-" : Server.volControl.decreaseVolume(); break;
				default :
			}
			
			
		}
		if (constant instanceof Integer) {
			readingQueue.setCurrentTrackPostion((Integer) constant);
			readingQueue.getCurrentTrack().play();
		}
		System.out.println("Received " + constant + " from the client, processing...");
	}
	
	public void updateClientList(Client newClient) {
		if (listClients.size() == 0)
			listClients.add(newClient);
		else {
			for (Client c : listClients) {
				if (newClient.equals(c))
					System.out.println("Same client asking for new connection, rejected...");
				else
					listClients.add(newClient);
			}
		}
	}
	
	public void disconnectClient(Client client) {
		if (listClients.contains(client)) {
			System.out.println("Client existant " + client.getSocket() + " d√©connect√©");
			listClients.remove(client);
		} else
			System.out.println(client.getSocket());
	}
	
	public Client getCurrentClient() {
		return this.currentClient;
	}
}
