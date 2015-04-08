package com.example.grooveberry;

import java.io.IOException;

import protocol.Protocol;
import android.annotation.SuppressLint;
import android.os.Handler;
import android.util.Log;
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
		Log.d("PA", "2eme thread");
		try {
			while (PlayActivity.connected) {
				Protocol p = (Protocol) this.client.readObject();
				this.readingQueue = (ReadingQueue) this.client.readObject();

				this.mHandler.post(new Runnable() {

					@SuppressLint("NewApi")
					@Override
					public void run() {
						playActivity.reloadActivityElements(readingQueue);
					}
				});

			}
		} catch (Exception e) {
			if (e instanceof IOException) {
				Log.e("THL", "Server's busy");
			} else {
				Log.e("THL", "Error");
				PlayActivity.connected = false;
			}

		}
	}
}
