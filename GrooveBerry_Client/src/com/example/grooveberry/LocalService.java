package com.example.grooveberry;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import files.ReadingQueue;

public class LocalService extends Service {

	private final IBinder mBinder = new LocalBinder();
	private Client client;
	private ReadingQueue mL;
	private boolean received = false;

	@Override
	public IBinder onBind(Intent arg0) {
		return this.mBinder;
	}

	public class LocalBinder extends Binder {
		LocalService getService() {
			return LocalService.this;
		}
	}

	public void connectToServer(String ip) {
		try {
			this.client = new Client(new Socket(ip, 12347), new Socket(ip,
					12348));
			PlayActivity.connected = true;
			if (PlayActivity.connected) {
				try {

					if (this.client.readObject().equals("#AUTH")) {
						this.client.sendObject("mdp");
					}
					this.client.makeFileStreams();
					if (this.client.readObject().equals("#RQ")) {
						this.client.sendObject("#OK");
						this.mL = (ReadingQueue) client.readSerializable();
					}

				} catch (Exception e) {
					PlayActivity.connected = false;
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("LS", "ERROR");

		}
	}

	public Client getClient() {
		return this.client;
	}

	public ReadingQueue getReadingQueue() {
		return this.mL;
	}

	public boolean getReceived() {
		return this.received;
	}

	public void resetReceived() {
		this.received = false;
	}
}
