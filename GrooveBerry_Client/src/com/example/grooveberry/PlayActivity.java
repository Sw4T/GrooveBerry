package com.example.grooveberry;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class PlayActivity extends ActionBarActivity implements OnClickListener {
	
	Button play, stop;
	TextView musicStatus,musicName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play);
		
		this.play = (Button) findViewById(R.id.button1);
		this.stop = (Button) findViewById(R.id.button2);
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		if (id == R.id.action_exit) {
			android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
		}
		return super.onOptionsItemSelected(item);
	}
	
	@SuppressLint("NewApi")
	public void onClick (View view) {
		
		switch (view.getId()) {
		case R.id.button1 : 
			this.musicStatus.setText("Playing !");
			this.musicName.setTextColor(Color.GREEN);
			
			
			break;
		case R.id.button2 :
			this.musicStatus.setText("Music stopped.");
			this.musicName.setTextColor(Color.RED);
			break;
		}
		
			
	}
}
