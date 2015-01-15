package tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import files.AudioFile;

public class AudioFileTest {

	private AudioFile audioFile;
	
	@Before
	public void setUp() throws Exception {
		audioFile = new AudioFile("audio/Bob Marley - Jammin.mp3");
	}

	@After
	public void tearDown() throws Exception {
		audioFile = null;
	}

	@Test
	public void test_mute() {
		audioFile.mute();
		assertEquals(true, audioFile.isMuted());
		audioFile.mute();
		assertEquals(false, audioFile.isMuted());
	}
	
	@Test
	public void test_play() {
		audioFile.play();
		assertEquals(true, audioFile.isPlaying());
		audioFile.stop();
		assertEquals(false, audioFile.isPlaying());
	}

}
