package files;

import static org.junit.Assert.*;

import java.lang.ProcessBuilder.Redirect;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ReadingQueueTest {
	private ReadingQueue readingQueue;

	private AudioFile audioFileTest = new AudioFile("audio/Bob Marley - Jammin.mp3");
	private AudioFile audioFileTest2 = new AudioFile("audio/test.wav");

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		this.readingQueue = null;
	}

	@Test
	public void testCreateAEmptyReadingQueue() {
		this.readingQueue = new ReadingQueue();
		assertEquals(true, this.readingQueue.isEmpty());
	}
	
	@Test
	public void testCreateAOneTrackReadingQueue() {
		this.readingQueue = new ReadingQueue(audioFileTest);		
		assertEquals(false, this.readingQueue.isEmpty());
		
		AudioFile firstAudioFile = this.readingQueue.getAudioFileList().getFirst();
		assertEquals(audioFileTest, firstAudioFile);
	}
	
	@Test
	public void testClearReadingQueue() {
		this.readingQueue = new ReadingQueue(audioFileTest);		
		assertEquals(false, this.readingQueue.isEmpty());
		
		this.readingQueue.clearQueue();		
		assertEquals(true, this.readingQueue.isEmpty());
	}
	
	@Test
	public void testAddTrackInLastPositionReadingQueue() {
		this.readingQueue = new ReadingQueue();
		
		this.readingQueue.addLast(audioFileTest);		
		assertEquals(false, this.readingQueue.isEmpty());
		
		AudioFile lastAudioFile = this.readingQueue.getAudioFileList().getLast();
		assertEquals(audioFileTest, lastAudioFile);
	}
	
	@Test
	public void testAddTrackInReadingQueue() {
		this.readingQueue = new ReadingQueue();
		
		this.readingQueue.addLast(audioFileTest);	
		this.readingQueue.addAt(0, audioFileTest2);
		
		AudioFile fistAudioFile = this.readingQueue.getAudioFileList().getFirst();
		assertEquals(audioFileTest2, fistAudioFile);
	}
	
	@Test
	public void testRemove1TrackInReadingQueue() {
		this.readingQueue = new ReadingQueue();
		
		this.readingQueue.addLast(audioFileTest);
		
		this.readingQueue.clearQueue();
		assertEquals(true, readingQueue.isEmpty());
	}
	
	@Test
	public void testRemove2TrackInReadingQueue() {
		this.readingQueue = new ReadingQueue();
		
		this.readingQueue.addLast(audioFileTest);	
		this.readingQueue.addAt(0, audioFileTest2);
		
		this.readingQueue.clearQueue();
		assertEquals(true, readingQueue.isEmpty());
	}
	
	@Test
	public void testPlayingTrackRetrieveReadingQueue() {
		this.readingQueue = new ReadingQueue();
		assertEquals(null, this.readingQueue.getPlayingTrack());
		
		this.readingQueue.addLast(audioFileTest);
		assertEquals(audioFileTest, this.readingQueue.getPlayingTrack());
		
		this.readingQueue.addLast(audioFileTest2);
		assertEquals(audioFileTest, this.readingQueue.getPlayingTrack());
		
		this.readingQueue.next();
		assertEquals(audioFileTest2, this.readingQueue.getPlayingTrack());
		
		this.readingQueue.next();
		assertEquals(audioFileTest, this.readingQueue.getPlayingTrack());
		
		this.readingQueue.remove(0);
		assertEquals(null, this.readingQueue.getPlayingTrack());
		
		this.readingQueue.clearQueue();
		assertEquals(null, this.readingQueue.getPlayingTrack());
	}
	
	@Test
	public void testPlaying() {
		this.readingQueue = new ReadingQueue();
		
		this.readingQueue.addLast(audioFileTest);
		this.readingQueue.addLast(audioFileTest2);
		
		audioFileTest.play();
		this.readingQueue.getPlayingTrack().play();
		
	}
	

}
