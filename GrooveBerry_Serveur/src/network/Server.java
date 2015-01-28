package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import files.AudioFile;

public class Server {

	public static final int SERVER_PORT = 12345;
	private ServerSocket serveur;
	private Socket socketCliente;
	private BufferedReader bufferRead;
	
	public Server() throws IOException {
		serveur = new ServerSocket(SERVER_PORT);
	}
	
	//Attente d'une connexion cliente et création du buffer de lecture
	public void waitConnection() throws IOException {
		socketCliente = serveur.accept();
		System.out.println("Client " + socketCliente.getInetAddress() + " has connected !");
		bufferRead = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
	}
	
	//Utilisation d'un nouveau thread pour permettre d'effectuer les tests en parallèle
	public void waitConnectionForTest() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					socketCliente = serveur.accept();
					System.out.println("Client " + socketCliente.getInetAddress() + " has connected !");
					BufferedReader buffer = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
					setBufferReader(buffer);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	//Reçoit des chaines de caractères venant du client, tant que "exit" n'a pas été reçu
	public void getTreatmentFromRemote() throws IOException, InterruptedException 
	{
		if (bufferRead == null)
			return;
		String audioFileName = bufferRead.readLine(); //Lecture du fichier audio allant être utilisé
		String constant;
		System.out.println("File used : " + audioFileName);
		
		AudioFile audioFileToPlay = new AudioFile(audioFileName);
		do {
			constant = bufferRead.readLine();
			execute(audioFileToPlay, constant);
		} while (!constant.equals("exit"));
		System.out.println("Serveur d�connect� !");
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
	
	public String getStringFromRemote() {
		if (bufferRead == null)
			return null;
		try {
			return (bufferRead.readLine());
		} catch (IOException e) { e.printStackTrace(); }
		return null;
	}
	
	public Socket getSocket () {
		return this.socketCliente;
	}
	
	public void setBufferReader(BufferedReader br) {
		bufferRead = br;
	}
}
