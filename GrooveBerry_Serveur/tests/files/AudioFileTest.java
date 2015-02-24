package files;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import files.AudioFile;

public class AudioFileTest {

	private AudioFile audioFile;
	private static final String FILE_TEST = "audio/test.wav";
	
	@Before
	public void setUp() throws Exception {
		audioFile = new AudioFile(FILE_TEST); //Durée de moins de 5 secondes
	}

	@After
	public void tearDown() throws Exception {
		audioFile = null;
	}

	@Test
	public void test_load() {
		audioFile = new AudioFile();
		assertEquals(false, audioFile.isLoaded());
		audioFile.loadFile(FILE_TEST);
		assertEquals(true, audioFile.isLoaded());
	}
	
	@Test
	public void test_mute() {
		audioFile.play();
		assertEquals(true, audioFile.isPlaying());
		audioFile.mute();
		assertEquals(true, audioFile.isMuted());
		audioFile.mute();
		assertEquals(false, audioFile.isMuted());
	}
	
	@Test
	public void test_play() throws InterruptedException {
		audioFile.play();
		assertEquals(true, audioFile.isPlaying());
		Thread.sleep(5000);
		assertEquals(false, audioFile.isPlaying());
	}
	
	@Test
	public void test_loop() throws InterruptedException {
		audioFile.play();
		assertEquals(true, audioFile.isPlaying());
		audioFile.loop();
		assertEquals(true, audioFile.isPlaying());
		assertEquals(true, audioFile.isLooping());
		Thread.sleep(5000);
		assertEquals(true, audioFile.isPlaying());
	}
}
