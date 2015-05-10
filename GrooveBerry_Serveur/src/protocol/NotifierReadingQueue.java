package protocol;

import java.io.IOException;

import network.Client;
import network.Server;

public class NotifierReadingQueue extends Notifier {

	public NotifierReadingQueue(Object [] send) {
		super(send);
	}

	public void run() {
		synchronized (this) {
			for (Client c : Server.getInstance().getClients()) {
				if (c != null) {
					try {
						c.getOut().writeObject(Protocol.MODIFY_READING_QUEUE);
						c.getOut().writeObject(Server.getInstance().getReadingQueue()); //Fil de lecture 
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
