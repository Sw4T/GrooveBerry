package files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class AuthenticationSystem {
	
	private static String fileAuthentication = "auth.txt";
	private File fichierAuth;
	
	public AuthenticationSystem() throws IOException {
		this.fichierAuth = new File(fileAuthentication);
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
