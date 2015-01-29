package files;

import java.util.ArrayList;

public class Library {
	ArrayList<AudioFile> audioFileList;

	public Library() {
		this.audioFileList = new ArrayList<>();
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

}
