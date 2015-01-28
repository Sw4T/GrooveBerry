package network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ClientTest {

	private Socket socket;
	private PrintWriter printer;
	public final static String fileToTest = "audio/Bob Marley - Jammin.mp3";
	
	public void connectionServer() {
		try {
			this.socket = new Socket("localhost", Server.SERVER_PORT);
			this.printer = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
			this.socket = null;
		}
	}
	
	public void sendString(String str) {
		if (this.printer != null)
			printer.println(str);
	}
	
	//Main du client Android ici simulé 
	public static void main (String [] args) {
		Socket socket = null;
		Scanner scan = new Scanner(System.in);
		int entreeUser = 0; String treatment = null;
		
		try {
			//Connexion au serveur
			socket = new Socket("localhost", Server.SERVER_PORT);
			if (socket.isConnected())
				System.out.println("Client : Je me suis bien connecté au serveur ! youhou!");
			
			//Envoi de la chaine correspondant au chemin du fichier pouvant etre lu sur le serveur
			PrintWriter printer = new PrintWriter(socket.getOutputStream(), true);
			printer.println(fileToTest);
			
			//Envoi de chaines définissant des constantes au serveur
			do {
				showMenu();
				try {
					entreeUser = scan.nextInt();
					treatment = convertIntToMusicConst(entreeUser);
				} catch (InputMismatchException inputFail) {
					treatment = "";
				}
				if (!treatment.equals(""))
					printer.println(treatment);
			} while (entreeUser != 7);
			
			//Fermeture de la connexion avec le serveur
			if (socket != null) {
				printer.close();
				socket.close();
				scan.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
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
	}
	
	public Socket getSocket() {
		return this.socket;
	}
}
