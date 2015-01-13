package reseau;

import java.io.IOException;

public class Main {

	//Main du serveur JAVA
	public static void main (String [] args) {
		try {
			Serveur serveur = new Serveur();
			serveur.attenteConnexionCliente();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
