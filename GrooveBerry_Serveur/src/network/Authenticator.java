package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import files.AuthenticationSystem;

public class Authenticator implements Runnable {

	private Socket socketSimple;
	private Socket socketObject;
	
	public Authenticator(Socket socketSimple, Socket socketObject) {
		this.socketSimple = socketSimple;
		this.socketObject = socketObject;
	}
	
	@Override
	public void run() {
		try {
			System.out.println("Authentification du client " + socketSimple.getInetAddress());
			AuthenticationSystem authSystem = new AuthenticationSystem();
			PrintWriter printer = new PrintWriter(socketSimple.getOutputStream(), true);
			BufferedReader buffer = new BufferedReader(new InputStreamReader(socketSimple.getInputStream()));
			
			//Vérification du mot de passe 
			printer.println("#AUTH");
			try {
				String passwordReceived = buffer.readLine();
				boolean passwordOK = authSystem.verifyPassword(passwordReceived);
				System.out.println("Mot de passe reçu du client " + socketSimple.getInetAddress().getHostAddress() + " : " + passwordReceived);
				Server server = Server.getInstance();
				if (passwordOK) {
					System.out.println("Authentification OK...");
					Client newClient = new Client(socketSimple, socketObject, server);
					newClient.setBuffers(printer, buffer);
					
					sendReadingQueueToRemote(newClient); 
					server.updateClientList(newClient); 
					new Thread(newClient).start(); //Lancement du traitement client
				} else
					System.out.println("Authentification FAILED...");
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Erreur lors de la réception du mot de passe venant du client !");
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Erreur lors de la création de flux avec le client !");
		}
	}
	
	public void sendReadingQueueToRemote(Client c) 
	{
		c.sendSerializable("#RQ"); //Constante pour reading queue
		String rep = (String) c.readSerializable();
		if (rep.equals("#OK")) {
			if (c.sendSerializable(Server.getInstance().getReadingQueue())) {
				System.out.println("Envoi de la reading queue OK...");
			}
		} else
			System.out.println("Erreur lors de l'envoi de la reading queue");
	}

}
