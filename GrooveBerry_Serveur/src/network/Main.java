package network;

import java.io.IOException;

public class Main {

	//Main du serveur JAVA
	public static void main (String [] args) {
		try {
			Server serveur = Server.getInstance();
			serveur.waitConnection();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
