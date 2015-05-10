package protocol;

import java.io.IOException;

import network.Client;
import network.Server;

public class NotifierVolume extends Notifier {

	private Integer volume;

	public NotifierVolume(Object[] send) {
		super(send);
		this.volume = (Integer) send[0];
	}

	@Override
	public void run() {
		synchronized (this) {
			for (Client c : Server.getInstance().getClients()) {
				if (c != null) {
					try {
						c.getOut().writeObject(Protocol.MODIFY_VOLUME);
						c.getOut().writeObject(volume); //Fil de lecture 
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
