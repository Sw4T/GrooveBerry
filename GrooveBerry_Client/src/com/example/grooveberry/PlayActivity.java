package com.example.grooveberry;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class PlayActivity extends ActionBarActivity implements OnClickListener {
	
	private ImageButton play, stop, pause, previous, next;
	private TextView musicStatus,musicName;
	private boolean isPlaying = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play);
		
		this.play = (ImageButton) findViewById(R.id.playButton);
		this.stop = (ImageButton) findViewById(R.id.stopButton);
		this.next = (ImageButton) findViewById(R.id.nextButton);
		this.previous = (ImageButton) findViewById(R.id.previousButton);
		this.musicStatus = (TextView) findViewById(R.id.textView1);
		this.musicName = (TextView) findViewById(R.id.textView2);
		
		this.play.setOnClickListener(this);
		this.stop.setOnClickListener(this);
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
			intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);  
		}
		if (id == R.id.action_connect) {
			intent = new Intent(this,ConnectActivity.class);
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
			if (!this.isPlaying)
			{
				this.play.setImageResource(R.drawable.ic_tab_play_selected);
				this.musicStatus.setText("Playing !");
				this.musicName.setTextColor(Color.GREEN);
				this.isPlaying = true;
			}
			else
			{
				this.play.setImageResource(R.drawable.ic_tab_pause_selected);
				this.musicStatus.setText("Pause.");
				this.musicName.setTextColor(Color.YELLOW);
				this.isPlaying = false;;
			}
			break;
		case R.id.stopButton :
			this.musicStatus.setText("Music stopped.");
			this.musicName.setTextColor(Color.RED);
			this.isPlaying = false;
			this.play.setImageResource(R.drawable.ic_tab_play_selected);
			break;
		}
		
			
	}
}
