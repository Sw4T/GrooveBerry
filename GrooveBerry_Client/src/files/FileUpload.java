package files;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import android.util.Log;

import com.example.grooveberry.Client;
import com.example.grooveberry.PlayActivity;

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
		try {
			sendFile(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void sendFile(String path) throws IOException {

		File file = new File(path);
		// Get the size of the file
		long length = file.length();
		if (length > Integer.MAX_VALUE) {
			System.out.println("File is too large.");
		}
		byte[] bytes = new byte[100 * 1024 * 1024];
		FileInputStream fis = new FileInputStream(file);
		BufferedInputStream bis = new BufferedInputStream(fis);
		BufferedOutputStream out = new BufferedOutputStream(this.client
				.getSocketFile().getOutputStream());

		int count = -1;

		while ((count = bis.read(bytes)) > 0) {
			out.write(bytes, 0, count);
			Log.d("azdfeazf",""+count);
		}

		Log.d("aaa","Fini");

		out.flush();
		out.close();
		fis.close();
		bis.close();
		PlayActivity.transferDone = true;
		Log.d("adazd", "" + PlayActivity.transferDone);
		// this.client.getSocketFile().close();

	}

}
