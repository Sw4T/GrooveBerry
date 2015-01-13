package reseau;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Serveur {

	public static final int PORT_SERVEUR = 12345;
	private ServerSocket serveur;
	private Socket socketCliente;
	
	public Serveur() throws IOException {
		serveur = new ServerSocket(PORT_SERVEUR);
	}
	
	public void attenteConnexionCliente() throws IOException {
		while (true) {
			socketCliente = serveur.accept();
			System.out.println("Client " + socketCliente.getInetAddress() + " s'est connecte");
		}
	}
	
	
	
}
