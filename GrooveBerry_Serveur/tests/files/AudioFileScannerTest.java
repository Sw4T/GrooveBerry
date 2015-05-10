package files;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;

import org.junit.Test;

public class AudioFileScannerTest {
	private AudioFileScanner audioFileScanner;

	@Test
	public void testGetAudioFileListToAudioDirectory() {
		audioFileScanner = new AudioFileScanner("audioTest/");
		ArrayList<AudioFile> audioFileList = audioFileScanner.getAudioFileList();
		
		assertEquals(5, audioFileList.size());
		
		ArrayList<AudioFile> audioFileListTest = new ArrayList<>();
		
		File[] list = new File("audioTest/").listFiles();
		
		audioFileListTest.add(new AudioFile(list[0].getPath()));
		audioFileListTest.add(new AudioFile(list[1].getPath()));
		audioFileListTest.add(new AudioFile(list[2].getPath()));
		audioFileListTest.add(new AudioFile(list[3].getPath()));
		audioFileListTest.add(new AudioFile(list[4].getPath()));
		
		for (int i = 0 ; i < audioFileListTest.size() ; i++) {
			assertEquals(audioFileListTest.get(i).getPath(), audioFileList.get(i).getPath());
		}
		
		audioFileScanner = new AudioFileScanner("audioTest");
		for (int i = 0 ; i < audioFileListTest.size() ; i++) {
			assertEquals(audioFileListTest.get(i).getPath(), audioFileList.get(i).getPath());
		}
	}

}
