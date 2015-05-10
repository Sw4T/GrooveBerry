package com.example.grooveberry;

import java.net.SocketException;

import protocol.Protocol;
import android.os.Handler;
import android.widget.Toast;
import files.ReadingQueue;

public class ThreadServerListener extends Thread {

	private Client client;
	private Handler mHandler;
	private PlayActivity playActivity;
	private ReadingQueue readingQueue;

	public ThreadServerListener(Client client, Handler mHandler,
			PlayActivity playActivity) {
		this.client = client;
		this.mHandler = mHandler;
		this.playActivity = playActivity;
	}

	public void run() {
		while (PlayActivity.connected) {
			try {
				Protocol p = (Protocol) this.client.readObject();
				this.readingQueue = (ReadingQueue) this.client.readObject();
				this.mHandler.post(new Runnable() {
					@Override
					public void run() {
						playActivity.reloadActivityElements(readingQueue);
					}
				});
			} catch (Exception e) {

				if (e instanceof SocketException) {
					this.mHandler.post(new Runnable() {

						@Override
						public void run() {
							playActivity.stopConnection();

						}
					});
					break;
				} else {

					this.mHandler.post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(
									playActivity,
									"Server is busy. Spamming makes kittens die.",
									Toast.LENGTH_LONG).show();
							playActivity.reloadActivityElements(readingQueue);
						}
					});
					break;
				}
			}
		}

	}
}
