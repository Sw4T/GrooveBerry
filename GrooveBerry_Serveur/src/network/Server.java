package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import files.AudioFile;
import files.ReadingQueue;

public class Server {

	public static final int SERVER_PORT = 12345;
	private static final int NB_MAX_CLIENTS = 5;
	private ServerSocket server; //Classe gérant les connexions entrantes
	private ArrayList<Client> listClients; //Liste de clients se connectant au serveur
	private ReadingQueue readingQueue; //Liste de lecture
	private Client currentClient; //Pour effectuer les tests
	
	public Server() throws IOException {
		server = new ServerSocket(SERVER_PORT);
		listClients = new ArrayList<Client>(NB_MAX_CLIENTS);
		readingQueue = new ReadingQueue();
		init_reading_queue();
	}
	
	//Attente d'une connexion cliente et traitement de test
	public void waitConnection() throws IOException, InterruptedException {
		while (true) {
			if (listClients.size() != NB_MAX_CLIENTS) {
				Socket socket = server.accept();
				System.out.println("Client " + socket.getInetAddress() + " has connected !");
				final Client newClient = new Client(socket, this);
				updateClientList(newClient);
				
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
	
	public void init_reading_queue() {
		readingQueue.addLast(new AudioFile("audio/04 Hey Joe.mp3"));
		readingQueue.addLast(new AudioFile("audio/05 Mentira.mp3"));
		readingQueue.addLast(new AudioFile("audio/Bob Marley - Jammin.mp3"));
		readingQueue.addLast(new AudioFile("audio/free.wav"));
		readingQueue.addLast(new AudioFile("audio/aol.wav"));
		readingQueue.addLast(new AudioFile("audio/banane2.wav"));
	}
	
	public synchronized void sendReadingQueueToRemote(Client c) 
	{
		c.sendString("#RQ"); //Constante pour reading queue
		String rep = c.readString();
		if (rep.equals("#OK")) {
			System.out.println("Client OK pour l'envoi de la reading queue");
			ArrayList<String> readingList = new ArrayList<String>();
			for (AudioFile file : readingQueue.getAudioFileList()) 
				readingList.add(file.getName());
			if (c.sendSerializable(readingList))
				System.out.println("Envoi de la reading queue OK...");
		} else
			System.out.println("Erreur lors de l'envoi de la reading queue");
	}
	
	public synchronized void execute(String constant) 
	{
		switch (constant)
		{
			case "play" : readingQueue.getCurrentTrack().play(); break;
			case "pause" : readingQueue.getCurrentTrack().pause(); break;
			case "mute" : readingQueue.getCurrentTrack().mute(); break;
			case "restart" : readingQueue.getCurrentTrack().restart(); break;
			case "stop" : readingQueue.getCurrentTrack().stop(); break;
			case "loop" : readingQueue.getCurrentTrack().loop(); break;
			case "next" : readingQueue.next(); break;
			case "prev" : readingQueue.prev(); break;
			case "random" : readingQueue.getCurrentTrack().random(); break;
			default :
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
