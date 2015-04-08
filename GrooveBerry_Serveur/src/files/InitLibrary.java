package files;

import java.io.IOException;
import java.util.ArrayList;

/*
 * Use once to initialize library file : res/library.txt
 */

public class InitLibrary {
	public static void main(String[] args) {
		ArrayList<AudioFile> audioFileList = new ArrayList<>();
		audioFileList.add(new AudioFile("audio/04 Hey Joe.mp3"));
		audioFileList.add(new AudioFile("audio/05 Mentira.mp3"));
		audioFileList.add(new AudioFile("audio/Bob Marley - Jammin.mp3"));
		audioFileList.add(new AudioFile("audio/free.wav"));
		audioFileList.add(new AudioFile("audio/aol.wav"));
		audioFileList.add(new AudioFile("audio/banane2.wav"));
		
		try {
			new Library(audioFileList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
