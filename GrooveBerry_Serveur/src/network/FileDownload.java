package network;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;

public class FileDownload implements Runnable {
	private ObjectInputStream in;
	private boolean finish;

	public FileDownload(ObjectInputStream in) {
		this.in = in;
	}
	
	@Override
	public void run() {
		try {
			finish = false;
			File file = (File) this.in.readObject();
			file.renameTo(new File("download/" + file.getName()));
			file.createNewFile();
			if (file.exists())
				finish = true;
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}	
	}
	
	public boolean getFinish() {
		return finish;
	}
}
