package files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * TrackStorage est une classe abstraite qui spécifie un
 * conteneur de fichier audio.
 * <br> Elle genere un fichier de sauvgarde pour concerver l'etat
 * du conteneur
 * 
 * @see AudioFile
 * 
 * @author Nicolas Symphorien, Enzo Alunni Bagarelli
 * @version 1.0
 */

public abstract class TrackStorage {

	public static final String DELIMITER = "#";
	
	protected ArrayList<AudioFile> audioFileList;
	protected File file;
	
	public TrackStorage(String filePath) throws IOException {
		this.audioFileList = new ArrayList<>();
		
		this.file = new File(filePath);
		if (!this.file.exists()) {
			try {
				this.file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			updateLibrary();
		}
	}
	
	/**
	 * Construit une classe abstraite de stockage de fichier audio à partir d'une liste de
	 * fichier audio. <br>
	 * Cette classe génere un fichier de sauvegarde de la liste des fichiers audio sur le
	 * disque dur au chemin <b>filePath</b>.
	 * @param audioFileList
	 * 		la liste des fichiers audio
	 * @param filePath
	 * 		le chemin du fichier de sauvegarde
	 * @throws IOException
	 */
	public TrackStorage(ArrayList<AudioFile> audioFileList, String filePath) throws IOException {
		this(filePath);
		this.audioFileList = audioFileList;
	}
	
	/**
	 * 
	 * @return
	 * 		true si le conteneur est vide, false sinon.
	 */
	public boolean isEmpty() {
		return this.audioFileList.isEmpty();
	}
	
	/**
	 * 
	 * @return
	 * 		la liste des fichiers audio sous forme d'ArrayList d'AudioFile.
	 * @see LikedList, Audiofile
	 */
	public ArrayList<AudioFile> getAudioFileList() {
		return this.audioFileList;
	}
	/**
	 * Met-à-jour le fichier de sauvegarde 
	 * 
	 * @throws FileNotFoundException
	 */
	protected void updateLibraryFile() throws FileNotFoundException {
		PrintWriter printWriterOutputFile = new PrintWriter(new FileOutputStream(this.file, false), true);
		for (AudioFile audioFile : audioFileList) {
			printWriterOutputFile.println(audioFile.getName() + DELIMITER + audioFile.getAbsolutePath());
		}
		printWriterOutputFile.close();
	}
	
	/**
	 * 
	 * @param filePath
	 * 		le chemin du fichier à vérifier
	 * @return
	 * 		true si le fichier est présent dans le conteneur, false sinon.
	 */
	protected boolean contains(String filePath) {
		boolean contain = false;
		for (AudioFile audioFile : audioFileList) {
			if (audioFile.getAbsolutePath().equals(filePath) || audioFile.getPath().equals(filePath)) {
				contain = true;
				break;
			}
		}
		return contain;
	}
	
	/**
	 * Met-à-jour la liste de fichier en fonction du contenue du fichier
	 * de sauvegarde.
	 * 
	 * @throws IOException
	 */
	public void updateLibrary() throws IOException {
		Scanner fileScanner = new Scanner(this.file);
		while(fileScanner.hasNextLine()) {
			String line = fileScanner.nextLine();
			if (!line.equals("")) {
				String filePath = line.split(DELIMITER)[1]; //TODO Exception
				if (!this.contains(filePath)) { 
					try {
						this.add(filePath);
					} catch (FileNotFoundException e) {
						
					}
				}
			}
		}
		fileScanner.close();
	}
	
	/**
	 * Ajoute le fichier au conteneur.
	 * 
	 * @param filePath
	 * 		le chemin du fichier
	 * @throws FileNotFoundException
	 */
	public void add(String filePath) throws FileNotFoundException {
		AudioFile audioFile = new AudioFile(filePath);
		if (audioFile.isLoaded()) {
			this.audioFileList.add(audioFile);
			updateLibraryFile();
		} else {
			throw new FileNotFoundException();
		}
	}
	
	/**
	 * Supprime le fichier du conteneur.
	 * 
	 * @param filePath
	 * 		le chemin du fichier
	 * @throws FileNotFoundException
	 */
	public void remove(String filePath) throws FileNotFoundException {
		AudioFile compareFile = new AudioFile(filePath);
		for (int i = 0; i < this.audioFileList.size(); i++) {
			if (this.audioFileList.get(i).getPath().equalsIgnoreCase(compareFile.getPath())) {
				this.audioFileList.remove(i);
			}		
		}
		updateLibraryFile();
	}
}
