package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import protocol.NotifierReadingQueue;
import files.AudioFile;
import files.AudioFileScanner;
import files.Library;
import files.ReadingQueue;
import files.SystemVolumeController;

public class Server implements Observer {

	public static final int SERVER_PORT_SIMPLE = 12347; //Port servant � l'�tablissement d'une connexion socket simple (envoi de primitifs)
	public static final int SERVER_PORT_OBJECT = 12348; //Port servant � l'�tablissement d'une connexion socket objet (envoi de d'objets s�rialis�s)
	private static final int NB_MAX_CLIENTS = 5; 
	
	private static volatile Server instanceServer; //Instance unique de la classe Server (pattern Singleton)
	private volatile ReadingQueue readingQueue; //Liste de lecture
	private volatile ArrayList<Client> listClients; //Liste de clients se connectant au serveur
	public volatile static SystemVolumeController volumeControl; //Classe g�rant le volume 
	private ServerSocket serverSocketSimple; //Classe g�rant les connexions entrantes et l'envoi de chaines 
	private ServerSocket serverSocketObject; //Classe g�rant l'envoi/r�ception d'objets plus lourds
	private Client currentClient; //Pour effectuer les tests
	
	private Server() {
		try {
			serverSocketSimple = new ServerSocket(SERVER_PORT_SIMPLE);
			serverSocketObject = new ServerSocket(SERVER_PORT_OBJECT);
			listClients = new ArrayList<Client>(NB_MAX_CLIENTS);
			readingQueue = new ReadingQueue();
			volumeControl = new SystemVolumeController();
			initReadingQueue();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	public synchronized static Server getInstance() {
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
				System.out.println("SERVEUR : Client " + newSocketSimple.getInetAddress() + " s'est connecte !");
				new Thread(new Authenticator(newSocketSimple, newSocketObject)).start();
			} else {
				newSocketSimple.close();
				System.out.println("ERREUR : Trop de connexions sont ouvertes sur le serveur, refus de connexion !");
			}
		}
	}
	
	//Utilisation d'un nouveau thread pour permettre d'effectuer les tests en parall�le
	public void waitConnectionForTest() {
		new Thread(new Runnable() {
			@Override
			public void run() {	
				Socket socket;
				try {
					socket = serverSocketSimple.accept();
					System.out.println("SERVEUR : Client " + socket.getInetAddress() + " has connected !");
					currentClient = new Client(socket);	
				} catch (IOException e) {
					e.printStackTrace();
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
			System.out.println("SERVEUR : Client existant " + client.getSocketSimple() + " d�connect�");
			listClients.remove(client);
		} else
			System.out.println(client.getSocketSimple());
	}
	
	public synchronized ArrayList<Client> getClients() {
		return listClients;
	}
	
	public synchronized ReadingQueue getReadingQueue() {
		return readingQueue;
	}
	
	public Integer getMasterVolume() {
		return volumeControl.getVolumePercentage();
	}
	
	public Client getCurrentClient() {
		return this.currentClient;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof AudioFile) {
			String state = (String) arg;
			switch (state) {
				case "EndOfPlay" : endOfPlay(); break;
				case "StopOfPlay" : stopOfPlay(); break;
			}
		}
		
	}

	private void stopOfPlay() {
		// TODO Auto-generated method stub
		
	}

	private void endOfPlay() {
		// Envoi du changement de morceau à tout les client
		Object [] objs = new Object[1]; 
		objs[0] = Server.getInstance().getReadingQueue(); 
		NotifierReadingQueue notify = new NotifierReadingQueue(objs);
		new Thread(notify).start();
		
	}
}
