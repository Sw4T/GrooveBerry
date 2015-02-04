package files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Library {
	public static final String DEFAULT_PATHNAME = "res/library.txt";
	public static final String DELIMITER = "#";
	
	//private ArrayList<Playlist>
	
	private ArrayList<AudioFile> audioFileList;
	private File file;

	public Library() {
		this.audioFileList = new ArrayList<>();
		this.file = new File(DEFAULT_PATHNAME);
		if (!this.file.exists()) {
			try {
				this.file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Library(ArrayList<AudioFile> audioFileList) {
		this();
		this.audioFileList = new ArrayList<>(audioFileList);
	}

	public boolean isEmpty() {
		return this.audioFileList.isEmpty();
	}

	public ArrayList<AudioFile> getAudioFileList() {
		return this.audioFileList;
	}

	public void setLibraryFilePathName(String filePath) {
		this.file = new File(filePath);	
	}

	public void updateLibraryFile() throws FileNotFoundException {
		PrintWriter printWriterOutputFile = new PrintWriter(new FileOutputStream(this.file, true), false);
		for (AudioFile audioFile : audioFileList) {
			printWriterOutputFile.println(audioFile.getName() + DELIMITER + audioFile.getAbsolutePath());
		}
		printWriterOutputFile.close();
	}
	
	// TODO Optimize update
	public void updateLibrary() throws FileNotFoundException {
		this.audioFileList.clear();
		Scanner fileScanner = new Scanner(this.file);
		while(fileScanner.hasNextLine()) {
			String line = fileScanner.nextLine();
			if (!line.equals("")) {
				String filePath = line.split(DELIMITER)[1];
				this.audioFileList.add(new AudioFile(filePath));
			}
		}
		fileScanner.close();
	}
	
	// TODO with update
	public void add(String filePath) throws FileNotFoundException {
		AudioFile audioFile = new AudioFile(filePath);
		this.audioFileList.add(audioFile);
		
		PrintWriter printWriterOutputFile = new PrintWriter(new FileOutputStream(this.file, true), true);
		printWriterOutputFile.println(audioFile.getName() + DELIMITER + audioFile.getAbsolutePath());
		
		printWriterOutputFile.close();
	}
	
	public void remove(){}
	
	public boolean isExist(AudioFile audioFile){
		return this.audioFileList.contains(audioFile);
	}
	
}
