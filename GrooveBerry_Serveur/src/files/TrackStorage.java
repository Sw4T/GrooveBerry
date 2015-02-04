package files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class TrackStorage {

	public static final String DELIMITER = "#";
	
	protected ArrayList<AudioFile> audioFileList;
	protected File file;
	
	public TrackStorage(String filePath) {
		this.audioFileList = new ArrayList<>();
		this.file = new File(filePath);
		if (!this.file.exists()) {
			try {
				this.file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public TrackStorage(ArrayList<AudioFile> audioFileList, String filePath) {
		this(filePath);
		this.audioFileList = audioFileList;
	}
	
	//TODO 
	public ArrayList<AudioFile> getAudioFileList() {
		//return new ArrayList<>(this.audioFileList);
		return this.audioFileList;
	}
		
	public boolean isEmpty() {
		return this.audioFileList.isEmpty();
	}
	
	public void setTrackStorageFilePathName(String pathname) {
		this.file = new File(pathname);	
	}
	
	public void add(String filePath) throws FileNotFoundException {
		AudioFile audioFile = new AudioFile(filePath);
		this.audioFileList.add(audioFile);
		
		PrintWriter printWriterOutputFile = new PrintWriter(new FileOutputStream(this.file),true);
		printWriterOutputFile.println(audioFile.getName() + DELIMITER + audioFile.getAbsolutePath());
		printWriterOutputFile.close();
	}
	
	// TODO Optimize update
	public void updateLibrary() throws FileNotFoundException {;
		this.audioFileList.clear();
		Scanner fileScanner = new Scanner(this.file);
		while(fileScanner.hasNextLine()) {
			String line = fileScanner.nextLine();
			String filePath = line.split(DELIMITER)[1];
			this.audioFileList.add(new AudioFile(filePath));
		}
		fileScanner.close();
	}
	
	
}
