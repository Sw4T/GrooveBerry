package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;

import protocol.Protocol;
import files.AudioFile;
import files.ReadingQueue;

public class ClientTestMulti {
	
	//Main du client Android ici simul√© 
	public static void main (String [] args) {
		Socket socketSimple = null, socketObject = null;
		Scanner scan = new Scanner(System.in);
		BufferedReader bufferIn = null;
		PrintWriter bufferOut = null;
		ObjectOutputStream objectOut = null;
		ObjectInputStream objectIn = null;
		int entreeUser = 0; String treatment = null;
		ReadingQueue listReading;
		
		try {
			//Connexion au serveur socket simple
			socketSimple = new Socket("localhost", Server.SERVER_PORT_SIMPLE);
			if (socketSimple.isConnected() && socketSimple.isBound()) {
				System.out.println("Client : Je me suis bien connect√© au serveur ! youhou!");
				bufferOut = new PrintWriter(socketSimple.getOutputStream(), true);
				bufferIn = new BufferedReader(new InputStreamReader(socketSimple.getInputStream()));
			} else 
				System.out.println("CLIENT : Socket simple cliente HS");
			
			socketObject = new Socket("localhost", Server.SERVER_PORT_OBJECT);
			
			//Authentification client
			String messageRecu = bufferIn.readLine();
			if (messageRecu.equals("#AUTH")) {
				bufferOut.println("mdp");
				System.out.println("Mot de passe envoyÈ !");
			} else
				System.out.println("Echec lors de la phase d'authentification ! Recu : " + messageRecu);
			
			//Connexion au serveur socket objet
			if (socketObject.isConnected() && socketObject.isBound()) {
				objectOut = new ObjectOutputStream(socketObject.getOutputStream());
				objectOut.flush();
				objectIn = new ObjectInputStream(socketObject.getInputStream());
				System.out.println("Flux d'objets initialisÈ !");
			} else
				System.out.println("CLIENT : Socket objet cliente HS");
			
			messageRecu = (String) objectIn.readObject();
			//Reception du fil de lecture depuis le serveur
			if (messageRecu.equals("#RQ")) {
				objectOut.writeObject("#OK");
				objectOut.flush();
				listReading = (ReadingQueue) objectIn.readObject();
				if (listReading != null)
					showReadingQueue(listReading);
			} else
				System.out.println("Erreurs de synchronisation serveur ! Recu : " + messageRecu);
			
			threadReceive(objectIn);
			//Envoi de chaines d√©finissant des constantes au serveur
			do {
				showMenu();
				try {
					entreeUser = scan.nextInt();
					treatment = convertIntToMusicConst(entreeUser);
				} catch (InputMismatchException inputFail) {
					treatment = "";
				}
				if (!treatment.equals("")) {
					objectOut.writeObject(treatment);
					objectOut.flush();
				}
				//receiveRQ(in);
			} while (entreeUser != 7);
			
			//Fermeture de la connexion avec le serveur
			if (socketSimple != null) {
				objectIn.close();
				objectOut.close();
				socketSimple.close();
				socketObject.close();
				scan.close();
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		} 
	}
	
	public static synchronized void threadReceive(final ObjectInputStream is) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (true)
						receiveRQ(is);
				} catch (ClassNotFoundException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	public static void receiveRQ(ObjectInputStream is) throws ClassNotFoundException, IOException {
		//Reception de la nouvelle liste
		Protocol prot = (Protocol) is.readObject();
		ReadingQueue rq = (ReadingQueue) is.readObject();
		System.out.println("********RECEIVED*******\nprotocole : " + prot);
		System.out.println("current track : " + rq.getCurrentTrack().getName());	
	}
	
	public static String convertIntToMusicConst(int input) 
	{
		String toReturn;
		switch (input) {
			case 1 : toReturn = "play"; break;
			case 2 : toReturn = "pause"; break;
			case 3 : toReturn = "mute"; break;
			case 4 : toReturn = "restart"; break;
			case 5 : toReturn = "stop"; break;
			case 6 : toReturn = "loop"; break;
			case 7 : toReturn = "exit"; break;
			case 8 : toReturn = "next"; break;
			case 9 : toReturn = "prev"; break;
			case 10 : toReturn = "random"; break;
			case 11 : toReturn = "+"; break;
			case 12 : toReturn = "-"; break;
			default : toReturn = "";
		}
		return toReturn;
	}
	
	public static void showMenu() 
	{
		System.out.println("1. Play");
		System.out.println("2. Pause/Unpause");
		System.out.println("3. Mute/Unmute");
		System.out.println("4. Restart");
		System.out.println("5. Stop");
		System.out.println("6. Loop");
		System.out.println("7. Exit");
		System.out.println("8. Next");
		System.out.println("9. Prev");
		System.out.println("10. Random");
	}
	
	public static void showReadingQueue(ReadingQueue list) 
	{
		System.out.println("Liste de lecture du serveur : ");
		for (AudioFile file : list.getAudioFileList()) {
			System.out.println("\t" + file.getName());
		}
	}
}
