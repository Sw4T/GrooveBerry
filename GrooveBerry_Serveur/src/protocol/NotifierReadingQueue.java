package protocol;

import java.io.IOException;
import java.util.ArrayList;

import network.Client;
import files.ReadingQueue;

public class NotifierReadingQueue extends Notifier {

	public NotifierReadingQueue(ArrayList<Client> list, Object ... send) {
		super(list, send);
	}

	public void run() {
		synchronized (this) {
			for (Client c : listClients) {
				if (c != null) {
					try {
						c.getOut().writeObject(Protocol.MODIFY_READING_QUEUE);
						ReadingQueue readingQueue = (ReadingQueue) toSend[0];
						c.getOut().writeObject(readingQueue); //Fil de lecture 
						c.getOut().writeObject(readingQueue.getCurrentTrackPosition());
						c.getOut().flush();
						c.getOut().reset();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
