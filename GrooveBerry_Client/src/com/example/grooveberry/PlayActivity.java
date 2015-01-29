package com.example.grooveberry;


import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class PlayActivity extends ActionBarActivity implements OnClickListener {
	
	private ImageButton play, pause, previous, next;
	private TextView musicStatus, musicName, musicTimer, notConnectedWarning;
	private ImageView warningLogo;
	private boolean onPause = false, isPlaying=false;
	private HashMap <ImageButton, Boolean> buttonState;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play);
		
		this.play = (ImageButton) findViewById(R.id.playButton);
		this.pause = (ImageButton) findViewById(R.id.pauseButton);
		this.next = (ImageButton) findViewById(R.id.nextButton);
		this.previous = (ImageButton) findViewById(R.id.previousButton);
		this.musicStatus = (TextView) findViewById(R.id.textView1);
		this.musicName = (TextView) findViewById(R.id.textView2);
		this.musicTimer = (TextView) findViewById(R.id.textView3);
		this.notConnectedWarning = (TextView) findViewById(R.id.textView4);
		this.warningLogo = (ImageView) findViewById(R.id.imageView2);
		
		
		this.play.setOnClickListener(this);
		this.pause.setOnClickListener(this);
		this.next.setOnClickListener(this);
		this.previous.setOnClickListener(this);
		
		if (!Connection.getConnected()) {
			this.notConnectedWarning.setVisibility(View.VISIBLE);
			this.warningLogo.setVisibility(View.VISIBLE);
			this.play.setEnabled(false);
			this.pause.setEnabled(false);
			this.previous.setEnabled(false);
			this.next.setEnabled(false);
		}
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (Connection.getConnected()) {
			this.notConnectedWarning.setVisibility(View.INVISIBLE);
			this.warningLogo.setVisibility(View.INVISIBLE);
			this.play.setEnabled(true);
			this.pause.setEnabled(true);
			this.previous.setEnabled(true);
			this.next.setEnabled(true);
			
			
			if (MusicList.getMusicNb() == MusicList.countMusicInList())
				this.next.setEnabled(false);
			if (MusicList.getMusicNb() == 0)
				this.previous.setEnabled(false);
		}
		if (this.buttonState != null) {
			this.play.setEnabled(this.buttonState.get(this.play));
			this.pause.setEnabled(this.buttonState.get(this.pause));
			this.next.setEnabled(this.buttonState.get(this.next));
			this.previous.setEnabled(this.buttonState.get(this.previous));
		}
		
		
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if (Connection.getConnected()) {
			this.buttonState = new HashMap <ImageButton, Boolean>();
			this.buttonState.put(this.play,(Boolean)this.play.isEnabled());
			this.buttonState.put(this.pause,(Boolean)this.pause.isEnabled());
			this.buttonState.put(this.next,(Boolean)this.next.isEnabled());
			this.buttonState.put(this.previous,(Boolean)this.previous.isEnabled());
		}
		
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.play, menu);
		return true;
	}
	
	@SuppressLint("NewApi")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		Intent intent;
		if (id == R.id.action_settings) {
//			intent = new Intent(this, SettingsActivity.class);
//            startActivity(intent);  
		}
		if (id == R.id.action_connect) {
			intent = new Intent(this,ConnectActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
		if (id == R.id.action_exit) {
			finish();
		}

		return super.onOptionsItemSelected(item);
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
		MusicList.previousMusic();
		Connection.getPrinter().println("prev");
		Log.d("PlayActivity", "PA : prev");
		Connection.getPrinter().println(MusicList.getFilePlaying());
		if (this.isPlaying) {
			this.musicStatus.setText("Playing : ");
			this.musicName.setText(MusicList.getFilePlaying());
			this.musicName.setTextColor(Color.GREEN);
			this.play.setEnabled(false);
			this.pause.setEnabled(true);
		}
		else {
			this.musicStatus.setText("Music paused.");
			this.musicName.setText(MusicList.getFilePlaying());
			this.musicName.setTextColor(Color.RED);
			this.play.setEnabled(true);
			this.pause.setEnabled(false);
		}
		
		
		if (MusicList.getMusicNb() < MusicList.countMusicInList())
			this.next.setEnabled(true);
		else
			this.next.setEnabled(false);
		
		if (MusicList.getMusicNb() > 0)
			this.previous.setEnabled(true);
		else
			this.previous.setEnabled(false);
		
		
	}

	private void action_next() {
		MusicList.nextMusic();
		Connection.getPrinter().println("next");
		Log.d("PlayActivity", "PA : next");
		Connection.getPrinter().println(MusicList.getFilePlaying());
		this.musicStatus.setText("Playing : ");
		this.musicName.setText(MusicList.getFilePlaying());
		this.musicName.setTextColor(Color.GREEN);
		this.play.setEnabled(false);
		this.pause.setEnabled(true);

		if (MusicList.getMusicNb() < MusicList.countMusicInList())
			this.next.setEnabled(true);
		else
			this.next.setEnabled(false);
		
		if (MusicList.getMusicNb() > 0)
			this.previous.setEnabled(true);
		else
			this.previous.setEnabled(false);
	}

	private void action_play() {
		Connection.getPrinter().println(MusicList.getFilePlaying());
		if (!this.onPause)
		{
			Connection.getPrinter().println("play");
			Log.d("PlayActivity", "PA : play sent");
		}
		else 
		{
			//ConnectActivity.printer.println("pause");
			Connection.getPrinter().println("pause");
			Log.d("PlayActivity", "PA : unpause sent");
		}
		this.musicStatus.setText("Playing : ");
		this.musicName.setText(MusicList.getFilePlaying());
		this.musicName.setTextColor(Color.GREEN);
		this.play.setEnabled(false);
		this.pause.setEnabled(true);
		this.isPlaying = true;
	}

	private void action_pause() {
		this.musicStatus.setText("Music paused.");
		Connection.getPrinter().println("pause");
		Log.d("PlayActivity", "PA : pause sent");
		this.musicName.setTextColor(Color.RED);
		this.play.setEnabled(true);
		this.pause.setEnabled(false);
		this.onPause = true;
		this.isPlaying = false;
	}
}
