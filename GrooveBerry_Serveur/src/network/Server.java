package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import files.AudioFile;
import files.AudioFileScanner;
import files.AudioListener;
import files.Library;
import files.ReadingQueue;

public class Server {

	public static final int SERVER_PORT = 12345;
	public static ArrayList<Client> listClients; //Liste de clients se connectant au serveur
	public static ReadingQueue readingQueue; //Liste de lecture
	private static final int NB_MAX_CLIENTS = 5;
	private ServerSocket server; //Classe gérant les connexions entrantes
	private Client currentClient; //Pour effectuer les tests
	
	public Server() throws IOException {
		server = new ServerSocket(SERVER_PORT);
		listClients = new ArrayList<Client>(NB_MAX_CLIENTS);
		readingQueue = new ReadingQueue();
		initReadingQueue();
	}
	
	//Attente d'une connexion cliente et traitement de test
	public void waitConnection() throws IOException, InterruptedException {
		while (true) {
			if (listClients.size() != NB_MAX_CLIENTS) {
				Socket socket = server.accept();
				System.out.println("Client " + socket.getInetAddress() + " has connected !");
				final Client newClient = new Client(socket, this);
				//updateClientList(newClient);
				listClients.add(newClient); //TODO Clients illimités pour test
				
				sendReadingQueueToRemote(newClient); //Envoi de la liste de lecture du serveur
				new Thread(newClient).start();
			} 
		}
	}
	
	//Utilisation d'un nouveau thread pour permettre d'effectuer les tests en parallèle
	public void waitConnectionForTest() {
		new Thread(new Runnable() {
			@Override
			public void run() {
					while (true) {
						try {
							Socket socket = server.accept();
							System.out.println("Client " + socket.getInetAddress() + " has connected !");
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
			System.out.println("Client existant " + client.getSocket() + " déconnecté");
			listClients.remove(client);
		} else
			System.out.println(client.getSocket());
	}
	
	public Client getCurrentClient() {
		return this.currentClient;
	}
}
