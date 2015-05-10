package files;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import network.Server;
import protocol.NotifierReadingQueue;

/**
 * <p>La classe <code>ReadingQueue</code> permet la gestion d'un fil de lecture
 * de maniére automatisée en gérant les operations suivantes :<br/>
 *  - Lire le morceaux suivant ;<br/>
 *  - Lire le morceaux précédent ;<br/>
 *  - Ajouter des morceaux de musique ;<br/>
 *  - Supprimer des morceaux de musique.</p>
 * 
 * @see AudioFile, AudioListener
 * 
 * @author Nicolas Symphorien, Enzo Alunni Bagarelli
 * @version 1.0
 */

public class ReadingQueue implements Observer, Serializable {
	private static final long serialVersionUID = -1233259350112363601L;
	
	private	AudioFile currentTrack;
	private int currentTrackIndex;
	private boolean randomised;
	
	private LinkedList<AudioFile> queue;
	
	/**
	 * Construire un fil de lecture vide.
	 *
	 */
	public ReadingQueue() {
		this.queue = new LinkedList<>();
		this.currentTrackIndex = -1;
	}
	
	/**
	 * Construire un fil de lecture avec un seul morceau de musique.
	 *
	 */
	public ReadingQueue(AudioFile track) {
		this.queue = new LinkedList<>();
		this.queue.add(track);
		
		this.currentTrack = track;
		this.currentTrack.addObserver(this);
		this.currentTrack.addObserver(Server.getInstance());
		this.currentTrackIndex = 0;
	}
	
	/**
	 * Construire un fil de lecture avec une liste de morceau de musique.
	 *
	 */
	public ReadingQueue(ArrayList<AudioFile> audioFileList) {
		this.queue = new LinkedList<>();
		for (AudioFile audioFile : audioFileList) {
			this.addLast(audioFile);
		}
	}
	
	/**
	 * Verifier si le fil de lecture est vide
	 * 
	 * @return
	 * 		<code>true</code> si le fil de lecture est vide, <code>false</code> sinon.
	 */
	public boolean isEmpty() {
		return this.queue.isEmpty();
	}
	
	/**
	 * Recuperer le nombre de morceaux du fil de lecture.
	 * 
	 * @return
	 * 		le nombre de morceaux dans du fil de lecture.
	 */	
	public int size() {
		return this.queue.size();
	}
	
	/**
	 * Ajouter un morceau à la fin du fil de lecture.
	 * 
	 * @param
	 * 		track le morceau à ajouter
	 */
	public void addLast(AudioFile track){
		if (this.isEmpty()) {
			this.currentTrack = track;
			this.currentTrackIndex = 0;
			this.currentTrack.deleteObserver(this);
			this.currentTrack.addObserver(this);
			this.currentTrack.addObserver(Server.getInstance());
		}
		this.queue.add(track);
	}
	
	/** 
	 * Ajouter un morceau à une postion specifique dans le fil
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
			this.currentTrack.deleteObserver(this);
			this.currentTrack.addObserver(this);
			this.currentTrack.addObserver(Server.getInstance());
		}
		if (index <= this.currentTrackIndex && !this.isEmpty()) {
			this.currentTrackIndex++;
		}
		this.queue.add(index, track);
	}
	
	/**
	 * Ajouter l'ensemble d'une playlist à la fin du fil de lecture.
	 * 
	 * @param playlist
	 * 		la playlist à ajouter
	 */
	public void addList(ArrayList<AudioFile> playlist) {
		this.queue.addAll(playlist);
	}
	
	/** 
	 * Ajouter l'ensemble d'une playlist à une postion specifique dans le fil
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
	 * Supprimer un morceau à une position specifique dans le fil
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
	 * Supprimer tout les morceaux contenue dans le fil de lecture.
	 */
	public void clearQueue() {
		this.queue.removeAll(this.queue);
		this.currentTrack = null;
		this.currentTrackIndex = -1;
	}
	
	/**
	 * Passer au morceau suivant dans le fil de lecture
	 */
	public void next() {
		int trackIndex = getCurrentTrackPosition();
		if (trackIndex + 1 < this.queue.size() || this.isRandomised()) {
			changeTrack(true);
		}
	}
	
	/**
	 * Passer au morceau précédent dans le fil de lecture
	 */
	public void prev() {
		int trackIndex = getCurrentTrackPosition();
		if (trackIndex - 1 >= 0) {
			changeTrack(false);
		}
	}
	/**
	 * Activer/desactiver le passage aléatoire à un morceau
	 */
	public void rand() {
		this.randomised = (this.randomised) ? false : true;
	}

	/**
	 * Recuperer le morceau en cours de lecture.
	 * 
	 * @return
	 * 		le morceau en cours de lecture
	 */
	public AudioFile getCurrentTrack() {
		return this.currentTrack;
	}

	/**
	 * Recuperer la position du morceau en cours de lecture.
	 * 
	 * @return
	 * 		la position du morceau en cours de lecture
	 */
	public int getCurrentTrackPosition() {
		return this.currentTrackIndex;
	}

	/**
	 * Recuperer la liste des morceaux.
	 * 
	 * @return
	 * 		la liste des morceaux sous forme de linkedList d'AudioFile
	 * @see LikedList, Audiofile
	 */
	public LinkedList<AudioFile> getAudioFileList() {
		return this.queue;
	}
	
	/**
	 * Verifier si le fil de lecture est randomisé.
	 * @return
	 * 		<code>true</code> si le fil de lecture est randomisé, <code>false</code> sinon.
	 */
	public boolean isRandomised() {
		return randomised;
	}

	/**
	 * Changer le morceau en cours de lecture.
	 * 
	 * @param index
	 * 		la position du morceau
	 */
	public void setCurrentTrackPostion(int index) {
		this.currentTrackIndex = index;
		this.currentTrack.stop();
		this.currentTrack = this.queue.get(index);
		this.currentTrack.deleteObserver(this);
		this.currentTrack.addObserver(this);
		this.currentTrack.addObserver(Server.getInstance());
	}
	
	/**
	 * Mettre fin au morceau en cours de lecture en fonction de 
	 * <code>trackFlags</code>.
	 * @param trackFlags
	 * 		Le statut d'un fichier audio.
	 * 	
	 * @see AudioFile, AudioFile.TrackFlags
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
	 * Changer le morceau selon <code>forward</code>, <code>isRandomised</code> et
	 * <code>trackFlags</code>.<br/>
	 * 
	 * @param forward
	 * 		Si <code>forward = true</code> alors passe au morceau suivant, sinon passe
	 * 		au morceau précedent. 
	 * @param trackFlags
	 * 		Le statut d'un fichier audio.
	 * 	
	 * @see AudioFile, AudioFile.TrackFlags
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
		this.currentTrack.deleteObserver(this);
		this.currentTrack.addObserver(this);
		this.currentTrack.addObserver(Server.getInstance());
	}
	
	/**
	 * Changer le statut du morceau en cours de lecture en fonction de 
	 * <code>trackFlags</code>.
	 * @param trackFlags
	 * 		Le statut d'un fichier audio.
	 * 
	 * @see AudioFile, AudioFile.TrackFlags
	 */
	private void changeCurrentTrackStatus(TrackFlags trackFlags) {
		if (trackFlags.muted) {
			this.currentTrack.mute();
		}
		if (trackFlags.looped) {
			this.currentTrack.loop();
		}

	}
	
	/**
	 * Changer le morceau selon <code>forward</code>.
	 * 
	 * @param forward
	 * 		Si <code>forward = true</code> alors passe au morceau suivant, sinon passe
	 * 		au morceau précedent. 
	 */
	private void changeTrack(boolean forward){
		TrackFlags previousTrackFlags = new TrackFlags(this.currentTrack);
		
		endCurrentTrack(previousTrackFlags);
		changeCurrentTrack(forward, previousTrackFlags);
		changeCurrentTrackStatus(previousTrackFlags);
		this.currentTrack.play();
	}
	
	/**
	 * Evenement qui survient à la fin de la lecture d'un morceau dans
	 * le fil de lecture.
	 */
	public void endOfPlay() {
		
		int trackIndex = getCurrentTrackPosition();
		if ((trackIndex + 1 < this.queue.size()) || this.isRandomised()) {
			next();
		}
	}
	
	/**
	 * Evenement qui survient à la mise sur stop d'un morceau dans
	 * le fil de lecture.
	 */
	public void stopOfPlay() {
		
	}
	
	/**
	 * Structure de données represantant l'etat d'un fichier audio.
	 * 
	 * @see AudioFile
	 * 
	 * @author Nicolas Symphorien
	 * @version 1.0
	 */
	private class TrackFlags {
		public boolean muted;
		public boolean looped;
		public boolean played;
		public boolean paused;
		
		/**
		 * Construire une structure de données représantant le fichier audio
		 * <code>track</code>.
		 * 
		 * @param track
		 * 		le fichier audio a analyser.
		 * 
		 * @see AudioFile
		 */
		public TrackFlags(AudioFile track) {
			muted = track.isMuted();
			looped = track.isLooping();
			played = track.isPlaying();
			paused = track.isPaused();
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof AudioFile) {
			String state = (String) arg;
			switch (state) {
				case "EndOfPlay" : endOfPlay(); break;
				case "StopOfPlay" : stopOfPlay(); break;
			}
		}
	}

}
