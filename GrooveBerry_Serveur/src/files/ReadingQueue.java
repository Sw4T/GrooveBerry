package files;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/**
 * <p>ReadingQueue permet la gestion d'une file de lecture
 * de maniére automatisée en gérant les operations suivantes :
 * <li> Lire le morceaux suivant ;</li>
 * <li> Lire le morceaux précédent ;</li>
 * <li> Ajouter des morceaux de musique ;</li>
 * <li> Supprimer des morceaux de musique.</li></p>
 * 
 * @see AudioFile, AudioListener
 * 
 * @author Nicolas Symphorien, Enzo Alunni Bagarelli
 * @version 1.0
 */

public class ReadingQueue implements AudioListener, Serializable {
	private static final long serialVersionUID = -1233259350112363601L;
	
	private	AudioFile currentTrack;
	private int currentTrackIndex;
	private boolean randomised;
	
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
	 * @return
	 * 		true si le file de lecture est vide, false sinon.
	 */
	public boolean isEmpty() {
		return this.queue.isEmpty();
	}
	
	/**
	 * @return
	 * 		le nombre de morceau dans du fil de lecture.
	 */	
	public int size() {
		return this.queue.size();
	}
	
	/**
	 * Ajoute un morceau à la fin du fil de lecture.
	 * 
	 * @param
	 * 		track le morceau à ajouter
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
	 * @param index
	 * 		la postion où ajouter le morceau
	 * @param track
	 * 		le morceau à ajouter
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
	 * @param playlist
	 * 		la playlist à ajouter
	 */
	public void addList(ArrayList<AudioFile> playlist) {
		this.queue.addAll(playlist);
	}
	
	/** 
	 * Ajoute l'ensemble d'une playlist à une postion specifique dans le fil
	 * de lecture.
	 * 
	 * @param index
	 * 		la postion où ajouter la playlist
	 * @param playlist
	 * 		la playlist à ajouter
	 */
	public void addListAt(int index, ArrayList<AudioFile> playlist) {
		this.queue.addAll(index, playlist);
	}

	/**
	 * Supprime un morceau à une position specifique dans le fil
	 * de lecture.
	 * 
	 * @param index
	 * 		la position où supprimer le morceau
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
		if (trackIndex + 1 < this.queue.size() || this.isRandomised()) {
			changeTrack(true);
		}
	}
	
	/**
	 * Passe au morceau précédent dans le fil de lecture
	 */
	public void prev() {
		int trackIndex = getCurrentTrackPosition();
		if (trackIndex - 1 >= 0) {
			changeTrack(false);
		}
	}
	/**
	 * Active/desactive le passage aléatoire à un morceau
	 */
	public void rand() {
		this.randomised = (this.randomised) ? false : true;
	}

	/**
	 * 
	 * @return
	 * 		le morceau en cours de lecture
	 */
	public AudioFile getCurrentTrack() {
		return this.currentTrack;
	}

	/**
	 * 
	 * @return
	 * 		la position du morceau en cours de lecture
	 */
	public int getCurrentTrackPosition() {
		return this.currentTrackIndex;
	}

	/**
	 * 
	 * @return
	 * 		la liste des morceaux sous forme de linkedList d'AudioFile
	 * @see LikedList, Audiofile
	 */
	public LinkedList<AudioFile> getAudioFileList() {
		return this.queue;
	}

	public boolean isRandomised() {
		return randomised;
	}

	/**
	 * Change le morceau en cours de lecture.
	 * 
	 * @param index
	 * 		la position du morceau
	 */
	public void setCurrentTrackPostion(int index) {
		this.currentTrackIndex = index;
		
	}
	
	/**
	 * Met fin a la lecture du morceau en cours de lecture en fonction de 
	 * <code>trackFlags</code>.
	 * @param trackFlags
	 * 		Le statut d'un fichier audio.
	 * 	
	 * @see AudioFile, TrackFlags
	 */
	private void endCurrentTrack(TrackFlags trackFlags) {
		if (trackFlags.played) {
			if (trackFlags.paused) {
				this.currentTrack.pause();
			}
			this.currentTrack.stop();
		}
		if (trackFlags.muted) {
			this.currentTrack.mute();
		}
		if (trackFlags.looped) {
			this.currentTrack.loop();
		}
	}
	/**
	 * Change le morceau selon <code>forward</code>, <code>isRandomised</code> et
	 * <code>trackFlags</code>.<br/>
	 * 
	 * @param forward
	 * 		Si <code>forward = true</code> alors passe au morceau suivant, sinon passe
	 * 		au morceau précedent. 
	 * @param trackFlags
	 * 		Le statut d'un fichier audio.
	 * 	
	 * @see AudioFile, TrackFlags
	 */
	private void changeCurrentTrack(boolean forward, TrackFlags trackFlags) {
		int shiftInt;
		if (forward) {
			if (this.isRandomised()) {
				Random rand = new Random();
				shiftInt = rand.nextInt(queue.size() - 1);
			} else {
				shiftInt = this.currentTrackIndex + 1;
			}	
		} else {
			shiftInt = this.currentTrackIndex - 1;
		}
		this.currentTrack = this.queue.get(shiftInt);
		this.currentTrackIndex = shiftInt;
		this.currentTrack.addListener(this);
	}
	
	/**
	 * Change le statut du morceau en cours de lecture en fonction de 
	 * <code>trackFlags</code>.
	 * @param trackFlags
	 * 		Le statut d'un fichier audio.
	 * 
	 *  @see AudioFile, TrackFlags
	 */
	private void changeCurrentTrackStatus(TrackFlags trackFlags) {
		if (trackFlags.muted) {
			this.currentTrack.mute();
		}
		if (trackFlags.looped) {
			this.currentTrack.loop();
		}

	}
	
	private void changeTrack(boolean forward){
		TrackFlags previousTrackFlags = new TrackFlags(this.currentTrack);
		
		endCurrentTrack(previousTrackFlags);
		changeCurrentTrack(forward, previousTrackFlags);
		changeCurrentTrackStatus(previousTrackFlags);
		this.currentTrack.play();
	}	
	
	@Override
	public void endOfPlay() {
		
		int trackIndex = getCurrentTrackPosition();
		if ((trackIndex + 1 < this.queue.size()) || this.isRandomised()) {
			next();
		}
	}
	
	@Override
	public void stopOfPlay() {
		
	}
	
	private class TrackFlags {
		public boolean muted;
		public boolean looped;
		public boolean played;
		public boolean paused;
		
		public TrackFlags(AudioFile track) {
			muted = track.isMuted();
			looped = track.isLooping();
			played = track.isPlaying();
			paused = track.isPaused();
		}
	}
}
