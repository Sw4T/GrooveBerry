	package com.example.grooveberry;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class ConnectActivity extends ActionBarActivity implements OnClickListener {

	private ImageButton wifiConnect, wifiDisconnect;
	private Connection connection;
	private TextView connectionStatus;
	//private String serverIpAddress = "";
	//private boolean connected = false;
	static Thread cThread;
	private Handler handler = new Handler ();
	private MusicList musicList;
	//public static PrintWriter printer;
	//public final static String fileToTest = "audio/Bob Marley - Jammin.mp3";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connect);
		
		//this.serverIpAddress = "192.168.1.12";
		this.connection = new Connection ("192.168.1.12");
		this.musicList = new MusicList();
		
		this.wifiConnect = (ImageButton) findViewById(R.id.wifiConnectButton);
		this.wifiDisconnect = (ImageButton) findViewById(R.id.wifiDisconnectButton);
		this.wifiConnect.setBackgroundColor(Color.TRANSPARENT);
		this.wifiDisconnect.setBackgroundColor(Color.TRANSPARENT);
		this.connectionStatus = (TextView) findViewById(R.id.connectionStatus);
		
		this.wifiConnect.setOnClickListener(this);
		this.wifiDisconnect.setOnClickListener(this);
	}

	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.connect, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		Intent intent;
		if (id == R.id.action_settings) {
			intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);  
		}
		if (id == R.id.action_play) {
			intent = new Intent(this,PlayActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			//finish();
			
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.wifiConnectButton :
//			if (!connected) {
//                if (!serverIpAddress.equals("")) {
//                	this.cThread = new Thread(new ClientThread());
//                    this.cThread.start();
//                    
//                    this.connectionStatus.setText("Connected to "+this.serverIpAddress);
//                }
			
			if (!this.connection.getConnected() && !this.connection.getServerIpAddress().equals("")) {
				ConnectActivity.cThread = new Thread(new ClientThread());
				ConnectActivity.cThread.start();
				this.connectionStatus.setText("Connected to "+this.connection.getServerIpAddress());
			}
			break;
		case R.id.wifiDisconnectButton :
//				if (connected) {
//					printer.println("exit");
//					this.connected = false;
//					this.connectionStatus.setText("Disconnected");
//				}
			if (this.connection.getConnected()) {
				this.connection.getPrinter().println("exit");
				this.connection.setConnected(true);
				this.connectionStatus.setText("Disconnected");
			}
				
			break;
		}
	}
	
	public class ClientThread implements Runnable {
		 
        public void run() {
            try {
            	connection.connectToServer();
            	if (connection.getConnected()) {
            		try {
            			connection.setPrinter(new PrintWriter(new BufferedWriter(new OutputStreamWriter(connection.getSocket().getOutputStream())), true));
            			//connection.getPrinter().println(connection.getFilePlaying());
            			Log.d("ConnectActivity", "CA : Sent " + musicList.getFilePlaying());
	                    while (connection.getConnected()) {
	                    	  connection.setPrinter(new PrintWriter(new BufferedWriter(new OutputStreamWriter(connection.getSocket().getOutputStream())), true));
	                    }
            		}
            		catch (Exception e) {
            			
            		}
            	}
            	stopThread(this);
            	connection.getSocket().close();
            }
            catch (Exception e) {
            	connection.setConnected(false);
            }
        }
        
        private synchronized void stopThread(ClientThread clientThread)
        {
            if (clientThread != null)
            {
                clientThread = null;
            }
        }
        
    }
	
	
}
