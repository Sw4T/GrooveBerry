package com.example.grooveberry;


import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grooveberry.LocalService.LocalBinder;

public class PlayActivity extends ActionBarActivity implements OnClickListener {
	
	public static boolean connected;
	
	
	private ImageButton play, pause, previous, next;
	private TextView musicStatus, musicName, musicTimer, notConnectedWarning;
	private ImageView warningLogo;
	private boolean onPause = false, isPlaying=false;
	private HashMap <ImageButton, Boolean> buttonState;
	private Client client;
	private MusicList musicList;
	private LocalService mService;
	private boolean mBound,ready;
	private String ip = "";
    private AlertDialog.Builder alert;
	
    private String[] mMusicTitles = {"Test", "chocolat"};
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play);
		
		createViewItems();
		
		
		this.play.setOnClickListener(this);
		this.pause.setOnClickListener(this);
		this.next.setOnClickListener(this);
		this.previous.setOnClickListener(this);
		this.mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		
		//Pour les tests sans wifi
		mAdapter = new ArrayAdapter<String>(this,R.layout.item_list, mMusicTitles);	
		mDrawerList.setAdapter(mAdapter);
		this.musicList = new MusicList(null);
		//
		
		
		disableAllButtons();
		
	}
	
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            Toast.makeText(PlayActivity.this, ((TextView)view).getText(), Toast.LENGTH_LONG).show();
            String musicChosen = (String) ((TextView)view).getText();
            Log.d("PA", musicChosen);
            musicList.setNextMusicName(musicChosen);
            mDrawerLayout.closeDrawer(mDrawerList);
            
        }
    }
	


	public void createViewItems() {
		this.play = (ImageButton) findViewById(R.id.playButton);
		this.pause = (ImageButton) findViewById(R.id.pauseButton);
		this.next = (ImageButton) findViewById(R.id.nextButton);
		this.previous = (ImageButton) findViewById(R.id.previousButton);
		this.musicStatus = (TextView) findViewById(R.id.textView1);
		this.musicName = (TextView) findViewById(R.id.textView2);
		this.musicTimer = (TextView) findViewById(R.id.textView3);
		this.notConnectedWarning = (TextView) findViewById(R.id.textView4);
		this.warningLogo = (ImageView) findViewById(R.id.imageView2);
		this.mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        this.mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
	}
	

	public void enableAllButtons() {
		if (PlayActivity.connected) {
			
			this.client = this.mService.getClient();
			this.musicList = this.mService.getMusicList();
			this.notConnectedWarning.setVisibility(View.INVISIBLE);
			this.warningLogo.setVisibility(View.INVISIBLE);
			this.play.setEnabled(true);
			this.pause.setEnabled(true);
			this.previous.setEnabled(true);
			this.next.setEnabled(true);
			//this.mMusicTitles = musicList.convertToStringTab();
			
			if (this.musicList.getMusicNb() == this.musicList.countMusicInList())
				this.next.setEnabled(false);
			if (this.musicList.getMusicNb() == 0)
				this.previous.setEnabled(false);	
			
			mAdapter = new ArrayAdapter<String>(this,R.layout.item_list, mMusicTitles);	
			mDrawerList.setAdapter(mAdapter);
		}
	}
	
	public void disableAllButtons() {
		if (this.client == null || !PlayActivity.connected) {
			this.notConnectedWarning.setVisibility(View.VISIBLE);
			this.warningLogo.setVisibility(View.VISIBLE);
			this.play.setEnabled(false);
			this.pause.setEnabled(false);
			this.previous.setEnabled(false);
			this.next.setEnabled(false);
			this.musicName.invalidate();
			this.musicStatus.invalidate();
			this.musicTimer.invalidate();
			PlayActivity.connected = false;
		}
	}

	public void saveButtonsState() {
		this.buttonState = new HashMap <ImageButton, Boolean>();
		this.buttonState.put(this.play,(Boolean)this.play.isEnabled());
		this.buttonState.put(this.pause,(Boolean)this.pause.isEnabled());
		this.buttonState.put(this.next,(Boolean)this.next.isEnabled());
		this.buttonState.put(this.previous,(Boolean)this.previous.isEnabled());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		if (!PlayActivity.connected)
			getMenuInflater().inflate(R.menu.play, menu);
		else
			getMenuInflater().inflate(R.menu.playconnected, menu);
		return true;
	}
	
	@SuppressLint("NewApi")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
 
		}
		if (id == R.id.action_connect) {
			
			Intent service = new Intent(this,LocalService.class);
			bindService(service, mConnection, Context.BIND_AUTO_CREATE);

			alert = new AlertDialog.Builder(this);

			alert.setTitle("Who should I connect ?");
			alert.setMessage("Enter the server IP : ");

			// Set an EditText view to get user input 
			final EditText input = new EditText(this);
			alert.setView(input);

			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			  ip = input.getText().toString();
			  startConnection(ip);
			  invalidateOptionsMenu();
			  
			  }
			});

			alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			  public void onClick(DialogInterface dialog, int whichButton) {

			  }
			});
			alert.show();
		}
		if (id == R.id.action_disconnect) {
			this.client.sendString("exit");
			if (mBound) {
	            unbindService(mConnection);
	            mBound = false;
	        }
			this.mService.disconnectFromServer();
			this.mService = null;
			this.ready = false;
			createViewItems();
			disableAllButtons();
			invalidateOptionsMenu();
			
		}
		if (id == R.id.action_exit) {
			finish();
			System.exit(0);
		}

		return super.onOptionsItemSelected(item);
	}
	
	private void startConnection(final String ip) {
		if (mBound) {
			new Thread (new Runnable() {
				
				@Override
				public void run() {
					mService.connectToServer(ip);
					Log.d("PA", "PA : connected");
					ready = true;
					
				}
			}).start();
			while (!ready);
			client = mService.getClient();
			musicList = mService.getMusicList();
			mMusicTitles = musicList.convertToStringTab();
			enableAllButtons();
			
		}
		
	}

	@SuppressLint("NewApi")
	public void onClick (View view) {
		
		switch(view.getId())
		{
		case R.id.playButton :
			action_play();
			break;
		case R.id.pauseButton :
			action_pause();
			break;
		
		case R.id.nextButton :
			action_next();
			break;
		case R.id.previousButton : 
			action_previous();
			break;
		}
			
	}
	

	private void action_previous() {
		this.musicList.previousMusic();
		this.client.sendString("prev");
		
		Log.d("PlayActivity", "PA : prev");
		this.client.sendString(this.musicList.getFilePlaying());
			this.musicStatus.setText("Playing : ");
			this.musicName.setText(this.musicList.getFilePlaying());
			this.musicName.setTextColor(Color.GREEN);
			this.play.setEnabled(false);
			this.pause.setEnabled(true);
		
		
		if (this.musicList.getMusicNb() < this.musicList.countMusicInList())
			this.next.setEnabled(true);
		else
			this.next.setEnabled(false);
		
		if (this.musicList.getMusicNb() > 0)
			this.previous.setEnabled(true);
		else
			this.previous.setEnabled(false);
		
		
	}

	private void action_next() {
		this.musicList.nextMusic();
		this.client.sendString("next");
		Log.d("PlayActivity", "PA : next");
		this.client.sendString(this.musicList.getFilePlaying());
		this.musicStatus.setText("Playing : ");
		this.musicName.setText(this.musicList.getFilePlaying());
		this.musicName.setTextColor(Color.GREEN);
		this.play.setEnabled(false);
		this.pause.setEnabled(true);

		if (this.musicList.getMusicNb() < this.musicList.countMusicInList())
			this.next.setEnabled(true);
		else
			this.next.setEnabled(false);
		
		if (this.musicList.getMusicNb() > 0)
			this.previous.setEnabled(true);
		else
			this.previous.setEnabled(false);
	}

	private void action_play() {
		this.client.sendString(this.musicList.getFilePlaying());
		if (!this.onPause)
		{
			this.client.sendString("play");
			Log.d("PlayActivity", "PA : play sent");
		}
		else 
		{
			this.client.sendString("pause");
			Log.d("PlayActivity", "PA : unpause sent");
		}
		this.musicStatus.setText("Playing : ");
		this.musicName.setText(this.musicList.getFilePlaying());
		this.musicName.setTextColor(Color.GREEN);
		this.play.setEnabled(false);
		this.pause.setEnabled(true);
		this.isPlaying = true;
	}

	private void action_pause() {
		this.musicStatus.setText("Music paused.");
		this.client.sendString("pause");
		Log.d("PlayActivity", "PA : pause sent");
		this.musicName.setTextColor(Color.RED);
		this.play.setEnabled(true);
		this.pause.setEnabled(false);
		this.onPause = true;
		this.isPlaying = false;
	}
	
	/** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
        	Log.d("CA", "CA : mBound = " + mBound);
        	mBound = true;
            LocalBinder binder = (LocalBinder) service;
            mService = binder.getService();
            
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
}
