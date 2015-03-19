package com.example.grooveberry;

import java.io.IOException;
import java.net.Socket;

import protocol.Protocol;
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
	private Protocol p;

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
			Log.d("LocalService", "LS : in method");
			//
			// Inet4Address.getByName("WAFFLES-PC");
			this.client = new Client(new Socket(ip, 12345));

			Log.d("LocalService", "LS : là ?");
			PlayActivity.connected = true;
			if (PlayActivity.connected) {
				Log.d("LocalService", "LS : Connected");
				try {

					// Reception du fil de lecture depuis le serveur
					if (this.client.readObject().equals("#RQ")) {
						this.client.sendObject("#OK");
						this.mL = (ReadingQueue) client.readSerializable();
					}

				} catch (Exception e) {
					Log.d("LS", "LS : 1st catch ");
					PlayActivity.connected = false;
					e.printStackTrace();
				}
			}
		} catch (Exception e) {

			Log.d("Client", "C : 2nd catch");
			e.printStackTrace();

		}
	}

	public void disconnectFromServer() {
		PlayActivity.connected = false;
		try {
			this.client.getSocket().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Client getClient() {
		return this.client;
	}

	public ReadingQueue getReadingQueue() {
		return this.mL;
	}

	public void listenServer() {
		
	}

	public boolean getReceived() {
		return this.received;
	}

	public void resetReceived() {
		this.received = false;
	}
}
