package files;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;


public class ReadingQueue implements AudioListener, Serializable {
	private static final long serialVersionUID = -1233259350112363601L;
	
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
		if (trackIndex + 1 < this.queue.size() || this.currentTrack.isRandomised()) {
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
		//Booleen de transfert de statut
		boolean muted, looped, randomised;
		//Test des status de la piste
		if (this.currentTrack.isPlaying()) {
			if (this.currentTrack.isPaused()) {
				this.currentTrack.pause();
			}
			this.currentTrack.stop();
		}
		if (muted = this.currentTrack.isMuted()) {
			this.currentTrack.mute();
		}
		if(looped = this.currentTrack.isLooping()){
			this.currentTrack.loop();
		}
		if(randomised = this.currentTrack.isRandomised()){
			this.currentTrack.random();
		}
		//Gestion du decalage
		int shiftInt;
		if(forward){
			if(randomised){
				Random rand = new Random();
				shiftInt = rand.nextInt(queue.size() - 1);
			}
			else{
				shiftInt =trackIndex + 1;
			}	
		}
		else{
			shiftInt = trackIndex - 1;
		}
		this.currentTrack = this.queue.get(shiftInt);
		this.currentTrackIndex = shiftInt;
		this.currentTrack.addListener(this);
		//Transmition de l'etat a la nouvelle piste
		if (muted) {
			this.currentTrack.mute();
		}
		this.currentTrack.play();
		
		if(looped){
			this.currentTrack.loop();
		}
		if(randomised){
			this.currentTrack.random();
		}
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
		if ((trackIndex + 1 < this.queue.size()) || this.currentTrack.isRandomised()) {
			next();
		}
	}
	
	@Override
	public void stopOfPlay() {
		
	}
	
	public void setCurrentTrack(AudioFile track) {
		this.currentTrack = track;		
	}
	
	public void addList(ArrayList<AudioFile> playlist) {
		this.queue.addAll(playlist);
	}
	
	public void addListAt(int index, ArrayList<AudioFile> playlist) {
		this.queue.addAll(index, playlist);
	}
}
