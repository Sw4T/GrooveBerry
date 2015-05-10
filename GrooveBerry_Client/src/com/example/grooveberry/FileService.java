package com.example.grooveberry;

import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import files.FileDownload;
import files.FileUpload;

public class FileService extends Service {

	private Client client;
	private final IBinder mBinder = new FileBinder();

	public FileService() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public class FileBinder extends Binder {
		FileService getService() {
			return FileService.this;
		}
	}

	public void uploadFile(String filePath) {
		new Thread(new FileUpload(this.client.getFileOut(), filePath, this.client))
				.start();
	}

	public void downloadFile(String filePath) {
		try {
			this.client.readObject();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		this.client.sendObject("#OK");
		new Thread(new FileDownload(this.client.getFileIn())).start();
	}

}
