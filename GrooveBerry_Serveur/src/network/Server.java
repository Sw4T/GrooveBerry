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
	public static SystemVolumeController volControl;
	
	private static volatile Server instanceServer;
	private static volatile ReadingQueue readingQueue; //Liste de lecture
	private static volatile ArrayList<Client> listClients; //Liste de clients se connectant au serveur
	private ServerSocket serverSocketSimple; //Classe g√©rant les connexions entrantes et l'envoi de chaines 
	private ServerSocket serverSocketObject; //Classe gÈrant l'envoi/rÈception d'objets plus lourds
	private Client currentClient; //Pour effectuer les tests
	
	private Server() {
		try {
			serverSocketSimple = new ServerSocket(SERVER_PORT_SIMPLE);
			serverSocketObject = new ServerSocket(SERVER_PORT_OBJECT);
			listClients = new ArrayList<Client>(NB_MAX_CLIENTS);
			readingQueue = new ReadingQueue();
			volControl = new SystemVolumeController();
			initReadingQueue();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Server getInstance() {
		if (instanceServer == null) {
			synchronized (Server.class) {
				if (instanceServer == null) {
					instanceServer = new Server();
				}
			}
		}
		return instanceServer;
	}
	
	//Attente d'une connexion cliente et authentification
	public void waitConnection() throws IOException, InterruptedException {
		while (true) {
			Socket newSocketSimple = serverSocketSimple.accept();
			if (listClients.size() != NB_MAX_CLIENTS) 
			{
				Socket newSocketObject = serverSocketObject.accept();
				System.out.println("Client " + newSocketSimple.getInetAddress() + " s'est connecte !");
				new Thread(new Authenticator(newSocketSimple, newSocketObject)).start();
			} else {
				newSocketSimple.close();
				System.out.println("ERREUR : Trop de connexions sont ouvertes sur le serveur, refus de connexion !");
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
		//if (listClients.size() == 0)
			listClients.add(newClient);
		/*else {
			for (Client c : listClients) {
				if (newClient.equals(c))
					System.out.println("Same client asking for new connection, rejected...");
				else
					listClients.add(newClient);
			}
		}*/
	}
	
	public void disconnectClient(Client client) {
		if (listClients.contains(client)) {
			System.out.println("Client existant " + client.getSocket() + " d√©connect√©");
			listClients.remove(client);
		} else
			System.out.println(client.getSocket());
	}
	
	public ArrayList<Client> getClients() {
		return listClients;
	}
	
	public ReadingQueue getReadingQueue() {
		return readingQueue;
	}
	
	public Client getCurrentClient() {
		return this.currentClient;
	}
}
