package com.example.grooveberry;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import android.content.Intent;
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
	private TextView connectionStatus;
	private String serverIpAddress = "";
	private boolean connected = false;
	private Handler handler = new Handler ();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connect);
		
		this.serverIpAddress = "192.168.1.10";
		this.wifiConnect = (ImageButton) findViewById(R.id.wifiConnectButton);
		this.wifiDisconnect = (ImageButton) findViewById(R.id.wifiDisconnectButton);
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
			startActivity(intent);
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.wifiConnectButton :
			if (!connected) {
                if (!serverIpAddress.equals("")) {
                	Thread cThread = new Thread(new ClientThread());
                    cThread.start();
                    this.connectionStatus.setText("Connected to "+this.serverIpAddress);
                }
			}
			break;
		case R.id.wifiDisconnectButton :
				this.connected = false;
				this.connectionStatus.setText("Disconnected");
			break;
		}
	}
	
	public class ClientThread implements Runnable {
		 
        public void run() {
            try {
                InetAddress serverAddr = InetAddress.getByName(serverIpAddress);
                Socket socket = new Socket(serverAddr, 12345);
                connected = true;
                while (connected) {
                    try {
                        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                            // WHERE YOU ISSUE THE COMMANDS
                            
                            Log.d("ClientActivity", "C: Sent.");
                        
                    } catch (Exception e) {
                    }
                }
                socket.close();
            } catch (Exception e) {
                connected = false;
            }
        }
        
    }
					
}
