package files;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class AudioFileScannerTest {
	private AudioFileScanner audioFileScanner;

	@Test
	public void testGetAudioFileListToAudioDirectory() {
		audioFileScanner = new AudioFileScanner("audioTest/");
		ArrayList<AudioFile> audioFileList = audioFileScanner.getAudioFileList();
		
		assertEquals(5, audioFileList.size());
		
		ArrayList<AudioFile> audioFileListTest = new ArrayList<>();
		audioFileListTest.add(new AudioFile("audioTest/01 Clandestino.mp3"));
		audioFileListTest.add(new AudioFile("audioTest/9.wav"));
		audioFileListTest.add(new AudioFile("audioTest/04 Hey Joe.mp3"));
		audioFileListTest.add(new AudioFile("audioTest/12 Bold as Love.mp3"));
		audioFileListTest.add(new AudioFile("audioTest/free.wav"));
		for (int i = 0 ; i < audioFileListTest.size() ; i++) {
			assertEquals(audioFileListTest.get(i).getPath(), audioFileList.get(i).getPath());
		}
		
		audioFileScanner = new AudioFileScanner("audioTest");
		for (int i = 0 ; i < audioFileListTest.size() ; i++) {
			assertEquals(audioFileListTest.get(i).getPath(), audioFileList.get(i).getPath());
		}
	}

}
