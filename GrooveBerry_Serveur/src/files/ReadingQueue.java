package files;

import java.util.LinkedList;

public class ReadingQueue {

	private	AudioFile playingTrack;
	
	private LinkedList<AudioFile> queue = new LinkedList<>();
	
	public ReadingQueue() {
		
	}
	
	public ReadingQueue(AudioFile track) {
		this.queue.add(track);
		
		this.playingTrack = track;
	}
	
	public void clearQueue() {
		this.queue.removeAll(this.queue);
		this.playingTrack = null;
	}
	
	public void addLast(AudioFile track){
		if (isEmpty()) {
			this.playingTrack = track;
		}
		this.queue.add(track);
	}
	
	public void addAt(int index, AudioFile track) {
		if (isEmpty()) {
			this.playingTrack = track;
		}
		this.queue.add(index, track);
	} 
	
	public void remove(int index) {
		AudioFile removeFile = this.queue.get(index);
		this.queue.remove(index);
		if (isEmpty() || removeFile == this.playingTrack) {
			this.playingTrack = null;
		}
	}
	
	public void next() {
		int trackIndex = this.queue.indexOf(playingTrack);
		
		if (trackIndex + 1 < this.queue.size()) {
			this.playingTrack = this.queue.get(trackIndex + 1);
		} else {
			this.playingTrack = this.queue.getFirst();
		}
		
	}
	
	public void prev() {
		int trackIndex = this.queue.indexOf(playingTrack);
		
		if (trackIndex - 1 <= 0) {
			this.playingTrack = this.queue.get(trackIndex - 1);
		} else {
			this.playingTrack = this.queue.getLast();
		}
		
	}

	public boolean isEmpty() {
		return this.queue.isEmpty();
	}

	public LinkedList<AudioFile> getAudioFileList() {
		return new LinkedList<>(this.queue);
	}

	public AudioFile getPlayingTrack() {
		return playingTrack;
	}
	
	
}
