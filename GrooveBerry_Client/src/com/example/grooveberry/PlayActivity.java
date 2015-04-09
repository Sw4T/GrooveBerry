package com.example.grooveberry;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grooveberry.FileService.FileBinder;
import com.example.grooveberry.LocalService.LocalBinder;

import files.ReadingQueue;

public class PlayActivity extends ActionBarActivity implements OnClickListener {

	private final static String STORETEXT = "storetext.txt";
	public static boolean connected;
	public static boolean transferDone;

	private ImageButton play, previous, next, random, loop, upload, download;
	private TextView musicName, notConnectedWarning;
	private ImageView warningLogo;
	private Client client;
	private ReadingQueue musicList;
	private LocalService mService;
	private FileService mFileService;
	private boolean mFileBound;
	private boolean mBound, ready;
	private String ip = "";
	private AlertDialog.Builder alert;
	private Handler mHandler = new Handler();
	private ThreadServerListener tSL;
	private String[] mMusicTitles;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ArrayAdapter<String> mAdapter;
	private EditText inputSearch;
	private String fileToSendURI;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.activity_play);
		createViewItems();
		setAllListeners();
		disableAllButtons();
		inputSearch.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				PlayActivity.this.mAdapter.getFilter().filter(cs);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});

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

			client.sendSerializable(musicList.getIndexByName(mDrawerList
					.getItemAtPosition(position).toString()));
			reloadActivityElements(musicList);
			mDrawerLayout.closeDrawers();
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
		this.inputSearch = (EditText) findViewById(R.id.inputSearch);
		OnFocusChangeListener ofcListener = new MyFocusChangeListener();
		this.inputSearch.setOnFocusChangeListener(ofcListener);

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

	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!PlayActivity.connected)
			getMenuInflater().inflate(R.menu.play, menu);
		else {
			getMenuInflater().inflate(R.menu.playconnected, menu);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_connect) {
			WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
			if (wifi.isWifiEnabled()) {
				connectPopup();
			} else {
				Toast.makeText(this,
						"Your wifi is off ! Please start it to connect.",
						Toast.LENGTH_LONG).show();
			}
		}

		if (id == R.id.action_disconnect) {
			stopConnection();

		}

		if (id == R.id.action_exit) {
			finish();
			System.exit(0);
		}

		return super.onOptionsItemSelected(item);
	}

	private void connectPopup() {
		final Intent service = new Intent(this, LocalService.class);
		bindService(service, mConnection, Context.BIND_AUTO_CREATE);

		alert = new AlertDialog.Builder(this);
		alert.setTitle("Who should I connect ?");
		alert.setMessage("Enter the server IP : ");

		final AutoCompleteTextView input = new AutoCompleteTextView(this);
		alert.setView(input);

		String[] adresses = readFile().toArray(new String[readFile().size()]);
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

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@SuppressLint("NewApi")
			public void onClick(DialogInterface dialog, int whichButton) {
				ip = input.getText().toString();
				if (!ip.equals("")) {
					try {
						startConnection(ip);
						reloadActivityElements(musicList);
						invalidateOptionsMenu();
						try {
							OutputStreamWriter out = new OutputStreamWriter(
									openFileOutput(STORETEXT, 0));
							out.write(ip);
							out.close();
						} catch (Throwable t) {
							System.out.println("Exception: " + t.toString());
						}
					} catch (Exception e) {
						Toast.makeText(
								PlayActivity.this,
								"You entered an invalid address.\nPlease, re-try with an other one.",
								Toast.LENGTH_LONG).show();
						mService.stopService(service);
						connectPopup();

					}

				} else {
					Toast.makeText(PlayActivity.this,
							"You must put a valid address !", Toast.LENGTH_LONG)
							.show();
				}
			}
		});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

					}
				});

		alert.show();
	}

	@SuppressLint("NewApi")
	public void stopConnection() {
		this.tSL.interrupt();
		this.client.sendObject("exit");
		if (mBound) {
			unbindService(mConnection);
			mBound = false;
		}
		if (mFileBound) {
			unbindService(mConnectionFile);
			mFileBound = false;
		}
		this.mService = null;
		this.ready = false;
		PlayActivity.connected = false;
		createViewItems();
		disableAllButtons();
		invalidateOptionsMenu();
	}

	private void startConnection(final String ip) {
		if (mBound) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					mService.connectToServer(ip);
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
			this.music_player_actions("next");
			break;

		case R.id.previousButton:
			this.music_player_actions("prev");
			break;

		case R.id.btnRepeat:
			this.music_player_actions("loop");
			break;

		case R.id.btnShuffle:
			this.music_player_actions("random");
			break;

		case R.id.upload_button:
			action_upload();
			break;

		case R.id.download_button:
			action_download();
			break;
		}

	}

	/** UPLOAD RELATED **/
	

	private void action_upload() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("audio/*");
		
		final Intent fileservice = new Intent(this, FileService.class);
		bindService(fileservice, mConnectionFile, Context.BIND_AUTO_CREATE);
		startActivityForResult(Intent.createChooser(intent, "Select music"), 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0 && resultCode == RESULT_OK) {
			PlayActivity.transferDone = false;
			Uri uri = data.getData();
			this.fileToSendURI = this.getPath(uri);
			if (this.fileToSendURI != null) {
				File fileToSend = new File(this.fileToSendURI);
				Toast.makeText(this, "Sending : " + fileToSend.getName(),
						Toast.LENGTH_LONG).show();
				this.client.sendObject("upload$" + fileToSend.getName());
				this.mFileService.uploadFile(this.getPath(uri));
				Log.d("PA",""+PlayActivity.transferDone);
				while(!PlayActivity.transferDone);
				Log.d("PA",""+PlayActivity.transferDone);
				this.reloadActivityElements(this.musicList);
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	@SuppressLint("NewApi")
	public String getPath(Uri contentUri) {
		String wholeID = DocumentsContract.getDocumentId(contentUri);
		String id = wholeID.split(":")[1];
		String[] column = { MediaStore.Audio.Media.DATA };
		String sel = MediaStore.Audio.Media._ID + "=?";
		Cursor cursor = getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, column, sel,
				new String[] { id }, null);
		String filePath = "";
		int columnIndex = cursor.getColumnIndex(column[0]);
		if (cursor.moveToFirst()) {
			filePath = cursor.getString(columnIndex);
		}

		cursor.close();
		return filePath;
	}

	/** DOWNLOAD RELATED **/

	private void action_download() {
		final Intent fileservice = new Intent(this, FileService.class);
		bindService(fileservice, mConnectionFile, Context.BIND_AUTO_CREATE);
		
		this.client.sendObject("download$"
				+ this.musicList.getCurrentTrack().getName());
		this.mFileService.downloadFile("GrooveBerry/audio/");
		Toast.makeText(
				this,
				Environment.getExternalStorageDirectory().toURI().toString()
						+ "GrooveBerry/audio/", Toast.LENGTH_LONG).show();
		this.reloadActivityElements(this.musicList);

	}

	/** MUSIC PLAYER RELATED **/
	
	private void music_player_actions(String command) {
		this.client.sendObject(command);
		this.reloadActivityElements(this.musicList);
	}

	private void action_play() {
		if (!this.musicList.getCurrentTrack().isPlaying()) {
			this.music_player_actions("play");
		} else {
			this.music_player_actions("pause");
		}

	}

	/** HARDWARE BUTTON RELATED **/
	
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

	/** REFRESH OF THE MAIN UI **/
	
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
			}
			if (musicList.getCurrentTrack().isPaused()) {
				this.play.setImageResource(R.drawable.btn_play);
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
		}
	}

	/** KEYBOARD RELATED **/

	private class MyFocusChangeListener implements OnFocusChangeListener {

		public void onFocusChange(View v, boolean hasFocus) {

			if (v.getId() == R.id.inputSearch && !hasFocus) {

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

			}
		}
	}
	
	/** SERVICES RELATED **/
	/** Defines callbacks for service binding, passed to bindService() */
	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			mBound = true;
			LocalBinder binder = (LocalBinder) service;
			mService = binder.getService();

		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			mBound = false;
		}
	};
	
	private ServiceConnection mConnectionFile = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			mFileBound = true;
			FileBinder binder = (FileBinder) service;
			mFileService = binder.getService();
			if (mFileService != null) {
				mFileService.setClient(client);
			}

		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			mFileBound = false;
		}
	};

}
