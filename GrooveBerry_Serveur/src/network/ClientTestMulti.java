package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;

import protocol.Protocol;
import files.AudioFile;
import files.ReadingQueue;

public class ClientTestMulti {
	
	static Scanner scannerDL = new Scanner(System.in);
	static ObjectOutputStream objectOut = null;
	static ObjectInputStream objectIn = null;
	static ObjectOutputStream fileOut = null;
	static ObjectInputStream fileIn = null;
	
	//Main du client Android ici simulÃ© 
	public static void main (String [] args) throws InterruptedException {
		Socket socketSimple = null, socketFile = null;
		int entreeUser = 0; String treatment = null;
		ReadingQueue listReading;
		
		try {
			//Connexion au serveur socket simple
			socketSimple = new Socket("localhost", Server.SERVER_PORT_SIMPLE);
			socketFile = new Socket("localhost", Server.SERVER_PORT_OBJECT);
			if (socketSimple.isConnected() && socketSimple.isBound()) {
				System.out.println("CLIENT : Je me suis bien connecté au serveur !");
				objectOut = new ObjectOutputStream(socketSimple.getOutputStream());
				objectIn = new ObjectInputStream(socketSimple.getInputStream());
				System.out.println("Flux d'objets initialisé !");
			} else {
				System.out.println("CLIENT : Socket simple cliente non connectée");
				System.exit(1);
			}
			
			//Authentification cliente
			String messageRecu = (String) objectIn.readObject();
			if (messageRecu.equals("#AUTH")) {
				objectOut.writeObject("mdp"); //Défini en dur dans auth.txt pour l'instant
				System.out.println("Mot de passe envoyé !");
			} else
				System.out.println("Echec lors de la phase d'authentification ! Recu : " + messageRecu);
			
			//Connexion au serveur socket objet
			if (socketFile.isConnected() && socketFile.isBound()) {
				fileOut = new ObjectOutputStream(socketFile.getOutputStream());
				fileIn = new ObjectInputStream(socketFile.getInputStream());
			} else {
				System.out.println("CLIENT : Socket objet cliente non connectée");
				System.exit(2);
			}
			
			messageRecu = (String) objectIn.readObject();
			
			//Reception du fil de lecture depuis le serveur
			if (messageRecu.equals("#RQ")) {
				objectOut.writeObject("#OK");
				objectOut.flush();
				listReading = (ReadingQueue) objectIn.readObject();
				if (listReading != null)
					showReadingQueue(listReading);
			} else {
				System.out.println("Erreurs de synchronisation serveur ! Recu : " + messageRecu);
				System.exit(3);
			}

			threadReceive(objectIn);
			
			//Envoi de chaines dÃ©finissant des constantes au serveur
			Scanner scan = new Scanner(System.in);
			do {
				showMenu();
				try {
					entreeUser = scan.nextInt();
					treatment = convertIntToMusicConst(entreeUser);
					System.out.println("CLIENT : Traitement envoyé : " + treatment);
				} catch (InputMismatchException inputFail) {
					treatment = "";
				}
				if (!treatment.equals("")) {
					objectOut.writeObject(treatment);
					objectOut.flush();
				}
			} while (entreeUser != 7);
			
			//Fermeture de la connexion avec le serveur
			if (socketSimple != null) {
				objectIn.close();
				objectOut.close();
				socketSimple.close();
				socketFile.close();
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
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	public static void receiveRQ(ObjectInputStream is) throws ClassNotFoundException, IOException {
		synchronized (is) {
			Protocol protocol = (Protocol) is.readObject();
			System.out.println("********RECEIVED*******\nProtocole : " + protocol);
			if (protocol == Protocol.MODIFY_READING_QUEUE) {
				ReadingQueue rq = (ReadingQueue) is.readObject();
				System.out.println("Current track : " + rq.getCurrentTrack().getName());	
			} else if (protocol == Protocol.MODIFY_VOLUME) {
				Integer volume = (Integer) is.readObject();
				System.out.println("Le volume a été modifié de " + volume + "%");
			}
			
		}
	}
	
	public static void downloadTest() throws IOException, ClassNotFoundException, InterruptedException {
		System.out.println("Entrez le nom du fichier à télécharger sur le serveur");
		String file = scannerDL.nextLine();
		
		objectOut.writeObject("download$" + file);
		objectOut.flush();
		fileIn.readObject();
		new Thread(new FileDownload(fileIn)).start();
	}
	
	public static void uploadTest() throws IOException, ClassNotFoundException, InterruptedException {
		System.out.println("Entrez le nom du fichier Ã  mettre sur le serveur");
		String file = scannerDL.nextLine();
		
		objectOut.writeObject("upload$" + file);
		objectOut.flush();
		fileIn.readObject();
		new Thread(new FileUpload(fileOut, file)).start();
	}
	
	public static String convertIntToMusicConst(int input) throws ClassNotFoundException, IOException, InterruptedException 
	{
		String toReturn = "";
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
			case 13 : downloadTest(); break;
			case 14 : uploadTest(); break;
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
		System.out.println("13. Download");
		System.out.println("14. Upload");
	}
	
	public static void showReadingQueue(ReadingQueue list) 
	{
		System.out.println("Liste de lecture du serveur : ");
		for (AudioFile file : list.getAudioFileList()) {
			System.out.println("\t" + file.getName());
		}
	}
}
