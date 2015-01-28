package files;

import java.util.LinkedList;

public class ReadingQueue implements AudioListener {
	private	AudioFile currentTrack;
	private int currentTrackIndex = -1;
	
	private LinkedList<AudioFile> queue;
	
	public ReadingQueue() {
		this.queue = new LinkedList<>();

	}
	
	public ReadingQueue(AudioFile track) {
		this.queue = new LinkedList<>();
		this.queue.add(track);
		
		this.currentTrack = track;
		this.currentTrack.addListener(this);
		this.currentTrackIndex = 0;
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
			if (this.currentTrack.isPlaying()) {
				this.currentTrack.stop();
				next();
				this.currentTrack.play();
			}
			else {
				this.currentTrack = this.queue.get(trackIndex + 1);
				this.currentTrackIndex = trackIndex + 1;
				this.currentTrack.addListener(this);
			} 
		}
	}
	
	// TODO
	public void prev() {
		int trackIndex = getCurrentTrackPosition();
		if (trackIndex - 1 >= 0) {
			this.currentTrack.stop();
			this.currentTrack = this.queue.get(trackIndex - 1);
			this.currentTrackIndex = trackIndex - 1;
			this.currentTrack.addListener(this);
			this.currentTrack.play();
		}
	}
	
	public int getCurrentTrackPosition() {
		return this.currentTrackIndex;
	}

	public LinkedList<AudioFile> getAudioFileList() {
		return new LinkedList<>(this.queue);
	}

	public AudioFile getCurrentTrack() {
		return this.currentTrack;
	}	

	@Override
	public void endOfPlay() {
		int trackIndex = getCurrentTrackPosition();
		if (trackIndex + 1 < this.queue.size()) {
			next();
			this.currentTrack.play();
		}
	}
	
	@Override
	public void stopOfPlay() {
		
	}

	public void setCurrentTrack(AudioFile track) {
		this.currentTrack = track;		
	}



}
