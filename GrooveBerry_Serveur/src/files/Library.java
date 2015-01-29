package files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Library {
	public static final String DEFAULT_PATHNAME = "res/library.txt";
	ArrayList<AudioFile> audioFileList;
	File file;

	public Library() {
		this.audioFileList = new ArrayList<>();
		this.file = new File(DEFAULT_PATHNAME);
		if (this.file.exists()) {
			try {
				this.file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Library(ArrayList<AudioFile> audioFileList) {
		this.audioFileList = audioFileList;
	}

	public boolean isEmpty() {
		return this.audioFileList.isEmpty();
	}

	public ArrayList<AudioFile> getAudioFileList() {
		return new ArrayList<>(this.audioFileList);
	}

	public void setLibraryFilePathName(String pathname) {
		this.file = new File(pathname);	
	}

	public void updateLibraryFile() throws FileNotFoundException {
		PrintWriter printWriterOutputFile = new PrintWriter(new FileOutputStream(this.file));
		for (AudioFile audioFile : audioFileList) {
			printWriterOutputFile.println(audioFile.getName() + "#" + audioFile.getAbsolutePath());
		}
		printWriterOutputFile.close();
	}
	
	// TODO Optimize update
	public void updateLibrary() throws FileNotFoundException {;
		this.audioFileList.clear();
		Scanner fileScanner = new Scanner(this.file);
		while(fileScanner.hasNext()) {
			String line = fileScanner.nextLine();
			String filePath = line.split("@")[1];
			this.audioFileList.add(new AudioFile(filePath));
		}
		fileScanner.close();
	}

}
