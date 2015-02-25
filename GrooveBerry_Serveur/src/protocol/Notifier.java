package protocol;


public abstract class Notifier implements Runnable {
	
	protected Object [] toSend;
	
	public Notifier(Object [] send) {
		this.toSend = send;
	}
	
	@Override
	public abstract void run();
}
