package com.example.grooveberry;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grooveberry.LocalService.LocalBinder;

import files.ReadingQueue;

public class PlayActivity extends ActionBarActivity implements OnClickListener {

	private final static String STORETEXT = "storetext.txt";
	public static boolean connected;

	private ImageButton play, previous, next, random, loop, upload, download;
	private TextView musicName, notConnectedWarning;
	private ImageView warningLogo;
	private HashMap<ImageButton, Boolean> buttonState;
	private Client client;
	private ReadingQueue musicList;
	private LocalService mService;
	private boolean mBound, ready;
	private String ip = "";
	private AlertDialog.Builder alert;
	private Handler mHandler = new Handler();
	private ThreadServerListener tSL;

	private String[] mMusicTitles;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ArrayAdapter<String> mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play);
		createViewItems();
		setAllListeners();
		disableAllButtons();

	}

	private void setAllListeners() {
		this.play.setOnClickListener(this);
		this.next.setOnClickListener(this);
		this.previous.setOnClickListener(this);
		this.random.setOnClickListener(this);
		this.loop.setOnClickListener(this);
		this.upload.setOnClickListener(this);
		this.download.setOnClickListener(this);
		this.mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		if (this.musicList == null) {
			this.musicName.setText("LISTEN! HEY LISTEN!");
		}
	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView parent, View view, int position,
				long id) {
			client.sendSerializable(position);
			reloadActivityElements(musicList);
			Log.d("PA", "" + position);
			mDrawerLayout.closeDrawer(mDrawerList);
		}

	}

	public void createViewItems() {
		this.play = (ImageButton) findViewById(R.id.playButton);
		this.next = (ImageButton) findViewById(R.id.nextButton);
		this.previous = (ImageButton) findViewById(R.id.previousButton);
		this.random = (ImageButton) findViewById(R.id.btnShuffle);
		this.loop = (ImageButton) findViewById(R.id.btnRepeat);
		this.musicName = (TextView) findViewById(R.id.textView2);
		this.notConnectedWarning = (TextView) findViewById(R.id.textView4);
		this.warningLogo = (ImageView) findViewById(R.id.imageView2);
		this.mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		this.mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
		this.upload = (ImageButton) findViewById(R.id.upload_button);
		this.download = (ImageButton) findViewById(R.id.download_button);
	}

	public void enableAllButtons() {
		if (PlayActivity.connected) {

			this.client = this.mService.getClient();
			this.musicList = this.mService.getReadingQueue();
			this.notConnectedWarning.setVisibility(View.INVISIBLE);
			this.warningLogo.setVisibility(View.INVISIBLE);
			this.play.setEnabled(true);
			this.previous.setEnabled(true);
			this.next.setEnabled(true);
			this.random.setEnabled(true);
			this.loop.setEnabled(true);
			this.download.setEnabled(true);
			this.upload.setEnabled(true);

			if (this.musicList.getCurrentTrackPosition() == this.musicList
					.getAudioFileList().size())
				this.next.setEnabled(false);
			if (this.musicList.getCurrentTrackPosition() == 0)
				this.previous.setEnabled(false);

			this.mAdapter = new ArrayAdapter<String>(this, R.layout.item_list,
					mMusicTitles);
			this.mDrawerList.setAdapter(mAdapter);

		}
	}

	public void disableAllButtons() {
		if (this.client == null || !PlayActivity.connected) {
			this.notConnectedWarning.setVisibility(View.VISIBLE);
			this.warningLogo.setVisibility(View.VISIBLE);
			this.play.setEnabled(false);
			this.previous.setEnabled(false);
			this.next.setEnabled(false);
			this.random.setEnabled(false);
			this.loop.setEnabled(false);
			this.musicName.invalidate();
			this.download.setEnabled(false);
			this.upload.setEnabled(false);
			PlayActivity.connected = false;
		}
	}

	public void saveButtonsState() {
		this.buttonState = new HashMap<ImageButton, Boolean>();
		this.buttonState.put(this.play, (Boolean) this.play.isEnabled());
		this.buttonState.put(this.next, (Boolean) this.next.isEnabled());
		this.buttonState
				.put(this.previous, (Boolean) this.previous.isEnabled());
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		if (!PlayActivity.connected)
			getMenuInflater().inflate(R.menu.play, menu);
		else {
			getMenuInflater().inflate(R.menu.playconnected, menu);
		}
		return true;
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_connect) {
			WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
			if (wifi.isWifiEnabled()) {

				Intent service = new Intent(this, LocalService.class);
				bindService(service, mConnection, Context.BIND_AUTO_CREATE);

				alert = new AlertDialog.Builder(this);
				alert.setTitle("Who should I connect ?");
				alert.setMessage("Enter the server IP : ");

				final AutoCompleteTextView input = new AutoCompleteTextView(
						this);
				alert.setView(input);

				String[] adresses = readFile().toArray(
						new String[readFile().size()]);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
						android.R.layout.simple_list_item_1, adresses);
				input.setAdapter(adapter);

				input.setOnTouchListener(new View.OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						input.showDropDown();
						return false;
					}
				});

				alert.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								ip = input.getText().toString();
								if (!ip.equals("")) {
									startConnection(ip);
									reloadActivityElements(musicList);
									invalidateOptionsMenu();
									try {
										OutputStreamWriter out = new OutputStreamWriter(
												openFileOutput(STORETEXT, 0));
										out.write(ip);
										out.close();
									} catch (Throwable t) {
										System.out.println("Exception: "
												+ t.toString());
									}

								} else {
									Toast.makeText(PlayActivity.this,
											"You must put a valid address !",
											Toast.LENGTH_LONG).show();
								}
							}
						});

				alert.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

							}
						});

				alert.show();
			} else {
				Toast.makeText(this,
						"Your wifi is off ! Please start it to connect.",
						Toast.LENGTH_LONG).show();
			}
		}

		if (id == R.id.action_disconnect) {
			this.tSL.interrupt();
			this.client.sendObject("exit");
			if (mBound) {
				unbindService(mConnection);
				mBound = false;
			}
			this.mService.disconnectFromServer();
			this.mService = null;
			this.ready = false;
			PlayActivity.connected = false;
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
			new Thread(new Runnable() {

				@Override
				public void run() {
					mService.connectToServer(ip);
					Log.d("PA", "PA : connected");
					ready = true;

				}
			}).start();
			while (!ready)
				;
			client = mService.getClient();
			musicList = mService.getReadingQueue();
			mMusicTitles = musicList.convertToStringTab();
			waitForServer();
			enableAllButtons();

		}

	}

	private void waitForServer() {
		this.tSL = new ThreadServerListener(this.client, mHandler, this);
		this.tSL.start();
	}

	public ArrayList<String> readFile() {
		String str;
		ArrayList<String> buf = new ArrayList<String>();
		try {
			InputStream in = openFileInput(STORETEXT);
			InputStreamReader tmp = new InputStreamReader(in);
			BufferedReader reader = new BufferedReader(tmp);
			if (in != null) {
				while ((str = reader.readLine()) != null) {
					buf.add(str);
				}
				in.close();
			}
		} catch (Throwable t) {

		}
		return buf;
	}

	@SuppressLint("NewApi")
	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.playButton:
			action_play();
			break;

		case R.id.nextButton:
			action_next();
			break;

		case R.id.previousButton:
			action_previous();
			break;

		case R.id.btnRepeat:
			this.client.sendObject("loop");
			this.reloadActivityElements(this.musicList);
			break;

		case R.id.btnShuffle:
			this.client.sendObject("random");
			this.reloadActivityElements(this.musicList);
			break;

		case R.id.upload_button:
			action_upload();
			break;

		case R.id.download_button:
			action_download();
			break;
		}

	}

	private void action_download() {
		this.client.sendObject("download");
		Log.d("PlayActivity", "PA : download");
		this.reloadActivityElements(this.musicList);

	}

	private void action_upload() {
		this.client.sendObject("upload");
		Log.d("PlayActivity", "PA : upload");
		this.reloadActivityElements(this.musicList);

	}

	private void action_previous() {
		this.client.sendObject("prev");
		Log.d("PlayActivity", "PA : prev");
		this.play.setImageResource(R.drawable.btn_pause);
		this.reloadActivityElements(this.musicList);

	}

	private void action_next() {
		this.client.sendObject("next");
		Log.d("PlayActivity", "PA : next");
		this.play.setImageResource(R.drawable.btn_pause);
		this.reloadActivityElements(this.musicList);

	}

	private void action_play() {
		if (!this.musicList.getCurrentTrack().isPlaying()) {
			this.client.sendObject("play");
			Log.d("PlayActivity", "PA : play sent");
		} else {
			this.client.sendObject("pause");
			Log.d("PlayActivity", "PA : unpause sent");
		}

		this.reloadActivityElements(this.musicList);

	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {

			this.client.sendObject("+");
			this.reloadActivityElements(this.musicList);
		}
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
			this.client.sendObject("-");
			this.reloadActivityElements(this.musicList);
		}
		return true;
	}

	public void reloadActivityElements(ReadingQueue musicList) {

		this.musicList = musicList;

		if (PlayActivity.connected) {
			if (musicList.getCurrentTrackPosition() < musicList
					.getAudioFileList().size())
				this.next.setEnabled(true);
			else
				this.next.setEnabled(false);

			if (musicList.getCurrentTrackPosition() > 0)
				this.previous.setEnabled(true);
			else
				this.previous.setEnabled(false);

			if (musicList.getCurrentTrack().isPlaying()) {
				this.play.setImageResource(R.drawable.btn_pause);
				Log.d("PA", "btn_pause");
			}
			if (musicList.getCurrentTrack().isPaused()) {
				this.play.setImageResource(R.drawable.btn_play);
				Log.d("PA", "btn_play");
			}
			if (this.musicList.getCurrentTrack().isLooping()) {
				this.loop.setImageResource(R.drawable.img_btn_repeat_pressed);
			} else {
				this.loop.setImageResource(R.drawable.img_btn_repeat);
			}
			if (this.musicList.isRandomised()) {
				this.random
						.setImageResource(R.drawable.img_btn_shuffle_pressed);
			} else {
				this.random.setImageResource(R.drawable.img_btn_shuffle);
			}

			this.musicName.setText(musicList.getCurrentTrack().getName());
			Log.d("PA1", musicList.getCurrentTrack().getName());
		}

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
