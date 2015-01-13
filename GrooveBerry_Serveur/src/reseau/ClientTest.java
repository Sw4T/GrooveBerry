package reseau;

import java.io.IOException;
import java.net.Socket;

public class ClientTest {

	//Main du client Android ici simulé 
	public static void main (String [] args) {
		Socket socket = null;
		try {
			socket = new Socket("localhost", Serveur.PORT_SERVEUR);
			if (socket.isConnected())
				System.out.println("Je me suis bien connecté au serveur ! youhou!");
			
			if (socket != null)
				socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
