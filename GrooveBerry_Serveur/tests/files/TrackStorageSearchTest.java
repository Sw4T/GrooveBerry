package files;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

public class TrackStorageSearchTest {
	private TrackStorageSearch search;
	private Library library;
	
	@Test
	public void testCreateAndSearchHeyJoe() throws IOException {
		AudioFileScanner libraryScanner = new AudioFileScanner("audioTest/");
		ArrayList<AudioFile> audioFileList = libraryScanner.getAudioFileList();
		this.library = new Library(audioFileList);
		this.search = new TrackStorageSearch(this.library);
		
		ArrayList<AudioFile> audioFileTest = new ArrayList<>();
		audioFileTest.add(audioFileList.get(3));
		assertEquals(audioFileTest.get(0), this.search.getTrackByName("Hey Joe").get(0));
	}
	
	@Test
	public void testCreateAndSearchMp3() throws IOException {
		AudioFileScanner libraryScanner = new AudioFileScanner("audioTest/");
		ArrayList<AudioFile> audioFileList = libraryScanner.getAudioFileList();
		this.library = new Library(audioFileList);
		this.search = new TrackStorageSearch(this.library);
		
		ArrayList<AudioFile> audioFileTest = new ArrayList<>();
		audioFileTest.add(audioFileList.get(0));
		audioFileTest.add(audioFileList.get(2));
		audioFileTest.add(audioFileList.get(3));
		assertEquals(audioFileTest.get(0), this.search.getTrackByName("mp3").get(0));
		assertEquals(audioFileTest.get(1), this.search.getTrackByName("mp3").get(1));
		assertEquals(audioFileTest.get(2), this.search.getTrackByName("mp3").get(2));
	}

}
