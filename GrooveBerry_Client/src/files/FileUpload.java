package files;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;

import com.example.grooveberry.Client;

public class FileUpload implements Runnable {
	private ObjectOutputStream out;
	private String path;
	private Client client;
	
	public FileUpload(ObjectOutputStream out, String path, Client client) {
		this.out = out;
		this.path = path;
		this.client = client;
	}


	@Override
	public void run() {
		try {
			this.client.readObject();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		sendFile(path);
	}


	private void sendFile(String path) {
		File file = new File(path);
		if (file.exists()) {
			System.out.println("le fichier au chemin " + path + " existe !");
			try {
				this.out.writeObject(file);
				this.out.flush();
				this.out.reset();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
			System.out.println("le fichier n'existe pas");
		
	}

}
