package network;

import java.io.BufferedReader;
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
	private BufferedReader bufferRead; //Seulement pour test
	private Client currentClient;
	
	public Server() throws IOException {
		server = new ServerSocket(SERVER_PORT);
		listClients = new ArrayList<Client>(NB_MAX_CLIENTS);
		readingQueue = new ReadingQueue();
		init_reading_queue();
	}
	
	//Attente d'une connexion cliente et création du buffer de lecture
	public void waitConnection() throws IOException, InterruptedException {
		while (true) {
			Socket socket = server.accept();
			System.out.println("Client " + socket.getInetAddress() + " has connected !");
			Client newClient = new Client(socket);
			listClients.add(newClient);
			
			sendReadingQueueToRemote(newClient); //Envoi de la liste de lecture du serveur
			getTreatmentFromRemote(newClient); //Récupération du traitement
		}
	}
	
	//Utilisation d'un nouveau thread pour permettre d'effectuer les tests en parallèle
	public void waitConnectionForTest() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Socket socket = server.accept();
					System.out.println("Client " + socket.getInetAddress() + " has connected !");
					Client newClient = new Client(socket);
					listClients.add(newClient);
					currentClient = newClient;
					//setBufferReader(buffer);
					while (true);
				} catch (IOException e) {
					e.printStackTrace();
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
	
	//Reçoit des chaines de caractères venant du client, tant que "exit" n'a pas été reçu
	public synchronized void getTreatmentFromRemote(Client c) throws IOException, InterruptedException 
	{
		String constant;
		do {
			constant = c.readString();
			execute(readingQueue, constant);
		} while (!constant.equals("exit"));
		System.out.println("Fin du traitement client " + c.getSocket());
	}
	
	public void execute(AudioFile file, String constant) throws IOException 
	{
		switch (constant)
		{
			case "play" : file.play(); break;
			case "pause" : file.pause(); break;
			case "mute" : file.mute(); break;
			case "restart" : file.restart(); break;
			case "stop" : file.stop(); break;
			case "loop" : file.loop(); break;
			default : 
		}
		System.out.println("Received " + constant + " from the client, processing...");
	}
	
	public void execute(ReadingQueue readingQueue, String constant) throws IOException 
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
			default : 
		}
		System.out.println("Received " + constant + " from the client, processing...");
	}
	
	public String getStringFromRemote() {
		if (bufferRead == null)
			return null;
		try {
			return (bufferRead.readLine());
		} catch (IOException e) { e.printStackTrace(); }
		return null;
	}
	
	public void setBufferReader(BufferedReader br) {
		bufferRead = br;
	}
	
	public Client getCurrentClient() {
		return this.currentClient;
	}
}
