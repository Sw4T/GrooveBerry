package network;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Authentification {
	
	private static String jesuisko = "auth.txt";
	private File fichierAuth;
	
	public Authentification() throws IOException {
		this.fichierAuth = new File(jesuisko);
		if (!this.fichierAuth.exists()) {
			this.fichierAuth.createNewFile();
		}
	}
	
	public boolean verifyPassword(String password) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(fichierAuth));
		String realPassword = br.readLine();
		if (realPassword == null)
			realPassword = "";
		br.close();
	
		return realPassword.equals(password);
	}
	
	
	public void modifyPassword(String password) throws FileNotFoundException {
		PrintWriter printer = new PrintWriter(fichierAuth);
		printer.close(); //Clean du fichier
		printer = new PrintWriter(fichierAuth);
		printer.println(password);
		printer.flush();
		printer.close();
	}
}
