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
	private Server server;
	
	public Authenticator(Socket socketSimple, Socket socketObject, Server server) {
		this.socketSimple = socketSimple;
		this.socketObject = socketObject;
		this.server = server;
	}
	
	@Override
	public void run() {
		try {
			System.out.print("Authentification du client " + socketSimple.getInetAddress());
			AuthenticationSystem authSystem = new AuthenticationSystem();
			PrintWriter printer = new PrintWriter(socketSimple.getOutputStream(), true);
			BufferedReader buffer = new BufferedReader(new InputStreamReader(socketSimple.getInputStream()));
			
			//Vérification du mot de passe 
			printer.println("#AUTH");
			try {
				String passwordReceived = buffer.readLine();
				boolean passwordOK = authSystem.verifyPassword(passwordReceived);
				if (passwordOK) {
					Client newClient = new Client(socketSimple, socketObject, server);
					newClient.setBuffers(printer, buffer);
					new Thread(newClient).start();
					System.out.println(" OK...");
				} else
					System.out.println(" FAILED...");
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Erreur lors de la réception du mot de passe venant du client !");
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println();
		}
		
	}

}
