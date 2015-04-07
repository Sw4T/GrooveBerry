package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Authenticator implements Runnable {

	private Socket socketSimple;
	private Socket socketObject;
	
	public Authenticator(Socket socketSimple, Socket socketObject) {
		this.socketSimple = socketSimple;
		this.socketObject = socketObject;
	}
	
	@Override
	public void run() {
		try {
			PrintWriter printer = new PrintWriter(socketSimple.getOutputStream());
			BufferedReader buffer = new BufferedReader(new InputStreamReader(socketSimple.getInputStream()));
		//TODO Authentification du client
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
