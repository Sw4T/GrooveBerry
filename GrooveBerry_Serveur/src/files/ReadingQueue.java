package files;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/**
 * ReadingQueue permet la gestion d'un fil de lecture.
 * Elle permet une gestion automatisée de 
 * 
 * @see AudioFile, AudioListener
 * 
 * @author Nicolas Symphorien, Enzo Alunni Bagarelli
 * @version 3.0
 */

public class ReadingQueue implements AudioListener, Serializable {
	private static final long serialVersionUID = -1233259350112363601L;
	
	private	AudioFile currentTrack;
	private int currentTrackIndex;
	
	private LinkedList<AudioFile> queue;
	
	/**
	 * Construit un fil de lecture vide.
	 *
	 */
	public ReadingQueue() {
		this.queue = new LinkedList<>();
		this.currentTrackIndex = -1;
	}
	
	/**
	 * Construit un fil de lecture avec un seul morceau de musique.
	 *
	 */
	public ReadingQueue(AudioFile track) {
		this.queue = new LinkedList<>();
		this.queue.add(track);
		
		this.currentTrack = track;
		this.currentTrack.addListener(this);
		this.currentTrackIndex = 0;
	}
	
	/**
	 * Construit un fil de lecture avec une liste de morceau de musique.
	 *
	 */
	public ReadingQueue(ArrayList<AudioFile> audioFileList) {
		this.queue = new LinkedList<>();
		for (AudioFile audioFile : audioFileList) {
			this.addLast(audioFile);
		}
	}
	
	/**
	 * @return true si le file de lecture est vide, false sinon.
	 */
	public boolean isEmpty() {
		return this.queue.isEmpty();
	}
	
	/**
	 * @return le nombre de morceau dans du fil de lecture.
	 */	
	public int size() {
		return this.queue.size();
	}
	
	/**
	 * Ajoute un morceau à la fin du fil de lecture.
	 * 
	 * @param track le morceau à ajouter
	 */
	public void addLast(AudioFile track){
		if (this.isEmpty()) {
			this.currentTrack = track;
			this.currentTrackIndex = 0;
			this.currentTrack.addListener(this);
		}
		this.queue.add(track);
	}
	
	/** 
	 * Ajoute un morceau à une postion specifique dans le fil
	 * de lecture.
	 * 
	 * @param index la postion où ajouter le morceau
	 * @param track le morceau à ajouter
	 */
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
	
	/**
	 * Ajoute l'ensemble d'une playlist à la fin du fil de lecture.
	 * 
	 * @param playlist la playlist à ajouter
	 */
	public void addList(ArrayList<AudioFile> playlist) {
		this.queue.addAll(playlist);
	}
	
	/** 
	 * Ajoute l'ensemble d'une playlist à une postion specifique dans le fil
	 * de lecture.
	 * 
	 * @param index la postion où ajouter la playlist
	 * @param playlist la playlist à ajouter
	 */
	public void addListAt(int index, ArrayList<AudioFile> playlist) {
		this.queue.addAll(index, playlist);
	}

	/**
	 * Supprime un morceau à une postion specifique dans le fil
	 * de lecture.
	 * 
	 * @param index la position où supprimer le morceau
	 */
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
	
	/**
	 * Supprime tout les morceaux contenue dans le fil de lecture.
	 */
	public void clearQueue() {
		this.queue.removeAll(this.queue);
		this.currentTrack = null;
		this.currentTrackIndex = -1;
	}
	
	/**
	 * Passe au morceau suivant dans le fil de lecture
	 */
	public void next() {
		int trackIndex = getCurrentTrackPosition();
		if (trackIndex + 1 < this.queue.size() || this.currentTrack.isRandomised()) {
			changeTrack(true,trackIndex);
		}
	}
	
	/**
	 * Passe au morceau precédent dans le fil de lecture
	 */
	public void prev() {
		int trackIndex = getCurrentTrackPosition();
		if (trackIndex - 1 >= 0) {
			changeTrack(false, trackIndex);
		}
	}
	
	//TODO Refactor enzo please !
	private void changeTrack(boolean forward, int trackIndex){
		//Booleen de transfert de statut
		boolean muted, looped, randomised;
		//Test des statuts de la piste
		if (this.currentTrack.isPlaying()) {
			if (this.currentTrack.isPaused()) {
				this.currentTrack.pause();
			}
			this.currentTrack.stop();
		}
		if (muted = this.currentTrack.isMuted()) {
			this.currentTrack.mute();
		}
		if (looped = this.currentTrack.isLooping()){
			this.currentTrack.loop();
		}
		if (randomised = this.currentTrack.isRandomised()){
			this.currentTrack.random();
		}
		//Gestion du decalage
		int shiftInt;
		if(forward){
			if(randomised){
				Random rand = new Random();
				shiftInt = rand.nextInt(queue.size() - 1);
			} else {
				shiftInt = trackIndex + 1;
			}	
		} else {
			shiftInt = trackIndex - 1;
		}
		this.currentTrack = this.queue.get(shiftInt);
		this.currentTrackIndex = shiftInt;
		this.currentTrack.addListener(this);
		//Transmission de l'etat à la nouvelle piste
		if (muted) {
			this.currentTrack.mute();
		}
		this.currentTrack.play();
		
		if (looped){
			this.currentTrack.loop();
		}
		if (randomised){
			this.currentTrack.random();
		}
	}	
	
	/**
	 * 
	 * @return la position du morceau en cours de lecture
	 */
	public int getCurrentTrackPosition() {
		return this.currentTrackIndex;
	}
	
	/**
	 * Change le morceau en cours de lecture.
	 * @param index la position du morceau
	 */
	public void setCurrentTrackPostion(int index) {
		this.currentTrackIndex = index;
		
	}

	/**
	 * 
	 * @return la liste des morceaux sous forme de linkedList d'AudioFile
	 * @see LikedList, Audiofile
	 */
	public LinkedList<AudioFile> getAudioFileList() {
		return this.queue;
	}

	/**
	 * 
	 * @return le morceau en cours de lecture
	 */
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
}
