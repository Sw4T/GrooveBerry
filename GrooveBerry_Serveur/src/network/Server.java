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
	
	public Server() throws IOException {
		serveur = new ServerSocket(SERVER_PORT);
	}
	
	public void waitConnection() throws IOException {
		socketCliente = serveur.accept();
		System.out.println("Client " + socketCliente.getInetAddress() + " has connected !");
	}
	
	public void getTreatmentFromRemote() throws IOException, InterruptedException 
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
		String audioFileName = br.readLine(); //Lecture du fichier audio allant être utilisé
		String constant;
		System.out.println("File used : " + audioFileName);
		
		AudioFile audioFileToPlay = new AudioFile(audioFileName);
		do {
			constant = br.readLine();
			execute(audioFileToPlay, constant);
		} while (!constant.equals("exit"));
		
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
	
	
	
}
