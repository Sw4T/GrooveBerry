package com.example.grooveberry;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class LocalService extends Service{
	

	private final IBinder mBinder = new LocalBinder();
	private Client client;
	private MusicList mL;

	@Override
	public IBinder onBind(Intent arg0) {
		return this.mBinder;
	}
	
	public class LocalBinder extends Binder {
        LocalService getService() {
            return LocalService.this;
        }
	}
	
	public void connectToServer (String ip) {
		try {
			Log.d("LocalService", "LS : in method");
			//
			//Inet4Address.getByName("WAFFLES-PC");
	    	this.client = new Client(new Socket(ip, 12345));
	    	
	    	Log.d("LocalService", "LS : là ?");
	    	PlayActivity.connected = true;
        	if (PlayActivity.connected) {
        		Log.d("LocalService", "LS : Connected");
        		try {
        			
        			//Reception du fil de lecture depuis le serveur
        			if (this.client.getIn().readUTF().equals("#RQ")) {
        				this.client.sendString("#OK");
        				this.mL = new MusicList((ArrayList<String>) client.getIn().readObject());
        			}
        			Log.d("LocalService", "LS : mL = "+mL.getFilePlaying());

        		}
        		catch (Exception e) {
        			Log.d("LS", "LS : 1st catch ");
        			PlayActivity.connected = false;
        		}
        	}
        }
        catch (Exception e) {
        	
        	Log.d("Client", "C : 2nd catch");
        	e.printStackTrace();
        	
        }
	}

	
	public void disconnectFromServer () {
		PlayActivity.connected = false;
		try {
			this.client.getSocket().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public Client getClient () {
		return this.client;
	}
	
	public MusicList getMusicList () {
		return this.mL;
	}
}
