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
	
	public boolean isEmpty() {
		return this.audioFileList.isEmpty();
	}

	public ArrayList<AudioFile> getAudioFileList() {
		return this.audioFileList;
	}
	
	//TODO Optimize
	protected void updateLibraryFile() throws FileNotFoundException {
		PrintWriter printWriterOutputFile = new PrintWriter(new FileOutputStream(this.file, true), false);
		for (AudioFile audioFile : audioFileList) {
			printWriterOutputFile.println(audioFile.getName() + DELIMITER + audioFile.getAbsolutePath());
		}
		printWriterOutputFile.close();
	}
	
	public void updateLibrary() throws FileNotFoundException {
		//this.audioFileList.clear();
		Scanner fileScanner = new Scanner(this.file);
		while(fileScanner.hasNextLine()) {
			String line = fileScanner.nextLine();
			if (!line.equals("")) {
				String filePath = line.split(DELIMITER)[1];
				AudioFile audioFile = new AudioFile(filePath);
				if (audioFile.isLoaded() && !this.audioFileList.contains(audioFile)) {
					this.audioFileList.add(new AudioFile(filePath));
				}
			}
		}
		fileScanner.close();
	}
	
	public void add(String filePath) throws FileNotFoundException {
		AudioFile audioFile = new AudioFile(filePath);
		this.audioFileList.add(audioFile);
		updateLibraryFile();
	}
	
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
