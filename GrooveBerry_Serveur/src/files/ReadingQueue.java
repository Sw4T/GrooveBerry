package files;

import java.util.ArrayList;
import java.util.LinkedList;

public class ReadingQueue implements AudioListener {
	private	AudioFile currentTrack;
	private int currentTrackIndex;
	
	private LinkedList<AudioFile> queue;
	
	public ReadingQueue() {
		this.queue = new LinkedList<>();
		this.currentTrackIndex = -1;
	}
	
	public ReadingQueue(AudioFile track) {
		this.queue = new LinkedList<>();
		this.queue.add(track);
		
		this.currentTrack = track;
		this.currentTrack.addListener(this);
		this.currentTrackIndex = 0;
	}
	
	public ReadingQueue(ArrayList<AudioFile> audioFileList) {
		this.queue = new LinkedList<>();
		for (AudioFile audioFile : audioFileList) {
			this.addLast(audioFile);
		}
	}

	public boolean isEmpty() {
		return this.queue.isEmpty();
	}

	public void addLast(AudioFile track){
		if (this.isEmpty()) {
			this.currentTrack = track;
			this.currentTrackIndex = 0;
			this.currentTrack.addListener(this);
		}
		this.queue.add(track);
	}
	
	public void addAt(int index, AudioFile track) {
		if (this.isEmpty()) {
			this.currentTrack = track;
			this.currentTrackIndex = 0;
			this.currentTrack.addListener(this);
		}
		if (index <= this.currentTrackIndex && !this.isEmpty()) {
			this.currentTrackIndex++;
		}
		this.queue.add(index, track);
	} 
	
	public void remove(int index) {
		AudioFile removeFile = this.queue.get(index);
		if (removeFile == this.currentTrack) {
			if (this.queue.getLast() != this.currentTrack) {
				next();
			} else {
				this.currentTrack = null;
				this.currentTrackIndex = -1;
			}
		}
		this.queue.remove(index);
		if (index <= this.currentTrackIndex) {
			this.currentTrackIndex--;
		}
	}
	
	public void clearQueue() {
		this.queue.removeAll(this.queue);
		this.currentTrack = null;
		this.currentTrackIndex = -1;
	}

	public void next() {
		int trackIndex = getCurrentTrackPosition();
		if (trackIndex + 1 < this.queue.size()) {
			changeTrack(true,trackIndex);
		}
	}
	
	public void prev() {
		int trackIndex = getCurrentTrackPosition();
		if (trackIndex - 1 >= 0) {
			changeTrack(false, trackIndex);
		}
	}
	
	private void changeTrack(boolean forward, int trackIndex){
		boolean muted = false;
		if (this.currentTrack.isPlaying()) {
			if (this.currentTrack.isPaused()) {
				this.currentTrack.pause();
			}
			if (this.currentTrack.isMuted()) {
				muted = true;
				this.currentTrack.mute();
			}
			this.currentTrack.stop();
		}
		
		if(forward){
			this.currentTrack = this.queue.get(trackIndex + 1);
			this.currentTrackIndex = trackIndex + 1;
		}
		else{
			this.currentTrack = this.queue.get(trackIndex - 1);
			this.currentTrackIndex = trackIndex - 1;
		}
		this.currentTrack.addListener(this);
		if (muted) {
			this.currentTrack.mute();
		}
		this.currentTrack.play();
	}
	
	
	
	
	public int getCurrentTrackPosition() {
		return this.currentTrackIndex;
	}

	public LinkedList<AudioFile> getAudioFileList() {
		return this.queue;
	}

	public AudioFile getCurrentTrack() {
		return this.currentTrack;
	}	

	@Override
	public void endOfPlay() {
		int trackIndex = getCurrentTrackPosition();
		if (trackIndex + 1 < this.queue.size()) {
			next();
		}
	}
	
	@Override
	public void stopOfPlay() {
		
	}
	
	//TODO 
	public void setCurrentTrack(AudioFile track) {
		this.currentTrack = track;		
	}



}
