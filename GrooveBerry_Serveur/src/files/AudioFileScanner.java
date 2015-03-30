package files;

import java.util.ArrayList;

//TODO recursive research
public class AudioFileScanner {
	private ArrayList<AudioFile> audioFileList;

	public AudioFileScanner(String directoryFilePath) {
		if (!directoryFilePath.endsWith("/")) {
			directoryFilePath += "/";
		}
		
		this.audioFileList = new ArrayList<>();
		
		String[] fileNameList = new java.io.File(directoryFilePath).list();
        for (int i = 0; i < fileNameList.length; i++)
        {
        	String[] tokens = fileNameList[i].split("\\.");
        	String fileExtension  = tokens[tokens.length - 1];
        	if (fileExtension.equals("mp3") || fileExtension.equals("wav")) {
        		this.audioFileList.add(new AudioFile(directoryFilePath + fileNameList[i]));
        	}
	    }
		
	}

	public ArrayList<AudioFile> getAudioFileList() {
		return this.audioFileList;
	}

}
