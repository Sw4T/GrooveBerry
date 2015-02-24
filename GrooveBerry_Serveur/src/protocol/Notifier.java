package protocol;

import java.util.ArrayList;

import network.Client;

public abstract class Notifier implements Runnable {
	
	protected ArrayList<Client> listClients;
	protected Object [] toSend;
	
	public Notifier(ArrayList<Client> list, Object ... send) {
		this.listClients = list;
		this.toSend = send;
	}
	
	@Override
	public abstract void run();
}
