package files;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ReadingQueueTest {
	private ReadingQueue readingQueue;

	private AudioFile bob;
	private AudioFile test;
	private AudioFile leNeuf;
	private AudioFile aol;

	@Before
	public void setUp() throws Exception {
		this.bob = new AudioFile("audio/Bob Marley - Jammin.mp3");
		this.test = new AudioFile("audio/test.wav");
		this.leNeuf = new AudioFile("audio/9.wav");
		this.aol = new AudioFile("audio/aol.wav");
	}

	@After
	public void tearDown() throws Exception {
		this.readingQueue = null;
		
		this.bob = null;
		this.test = null;
		this.leNeuf = null;
		this.aol = null;
	}

	@Test
	public void testCreateAEmptyReadingQueue() {
		this.readingQueue = new ReadingQueue();
		assertEquals(true, this.readingQueue.isEmpty());
	}
	
	@Test
	public void testAReadingQueueWithAudioFileList() {
		ArrayList<AudioFile> audioFileList = new ArrayList<>();
		audioFileList.add(bob);
		audioFileList.add(leNeuf);
		audioFileList.add(test);
		audioFileList.add(aol);
		
		this.readingQueue = new ReadingQueue(audioFileList);
		assertEquals(audioFileList, this.readingQueue.getAudioFileList());
	}
	
	@Test
	public void testCreateAOneTrackReadingQueue() {
		this.readingQueue = new ReadingQueue(bob);		
		assertEquals(false, this.readingQueue.isEmpty());
		
		AudioFile firstAudioFile = this.readingQueue.getAudioFileList().get(0);
		assertEquals(bob, firstAudioFile);
		assertEquals(0, this.readingQueue.getCurrentTrackPosition());
	}
	
	@Test
	public void testClearReadingQueue() {
		this.readingQueue = new ReadingQueue(bob);		
		assertEquals(false, this.readingQueue.isEmpty());
		
		this.readingQueue.clearQueue();		
		assertEquals(true, this.readingQueue.isEmpty());
		assertEquals(null, this.readingQueue.getCurrentTrack());
		assertEquals(-1, this.readingQueue.getCurrentTrackPosition());
	}
	
	@Test
	public void testAddTrackInLastPositionInEmptyReadingQueue() {
		this.readingQueue = new ReadingQueue();
		
		this.readingQueue.addLast(bob);
		assertEquals(false, this.readingQueue.isEmpty());
		
		AudioFile lastAudioFile = this.readingQueue.getAudioFileList().get(this.readingQueue.getAudioFileList().size()-1);
		assertEquals(bob, lastAudioFile);
		assertEquals(bob, this.readingQueue.getCurrentTrack());
		assertEquals(0, this.readingQueue.getCurrentTrackPosition());
	}
	
	@Test
	public void testAddLastInNotEmptyReadingQueue() {
		this.readingQueue = new ReadingQueue();
		
		this.readingQueue.addLast(bob);
		AudioFile lastAudioFile = this.readingQueue.getAudioFileList().get(this.readingQueue.getAudioFileList().size()-1);
		assertEquals(bob, lastAudioFile);
		
		this.readingQueue.addLast(test);
		lastAudioFile = this.readingQueue.getAudioFileList().get(this.readingQueue.getAudioFileList().size()-1);
		assertEquals(test, lastAudioFile);
		
		this.readingQueue.addLast(leNeuf);
		lastAudioFile = this.readingQueue.getAudioFileList().get(this.readingQueue.getAudioFileList().size()-1);
		assertEquals(leNeuf, lastAudioFile);
		assertEquals(0, this.readingQueue.getCurrentTrackPosition());
		
		assertEquals(bob, this.readingQueue.getCurrentTrack());
	}
	
	@Test
	public void testAddLastWithNoCurrentTrack() {
		this.readingQueue = new ReadingQueue();
		
		this.readingQueue.addLast(bob);
		this.readingQueue.addLast(test);
		this.readingQueue.addLast(leNeuf);
		this.readingQueue.addLast(bob);
		
		assertEquals(bob, this.readingQueue.getCurrentTrack());
		assertEquals(0, this.readingQueue.getCurrentTrackPosition());
	}
	
	@Test
	public void testAddAtFirstPositionTrackInReadingQueue() {
		this.readingQueue = new ReadingQueue();
		
		this.readingQueue.addLast(bob);	
		this.readingQueue.addAt(0, test);
		
		AudioFile firstAudioFile = this.readingQueue.getAudioFileList().get(0);
		assertEquals(test, firstAudioFile);
		
		assertEquals(bob, this.readingQueue.getCurrentTrack());
		assertEquals(1, this.readingQueue.getCurrentTrackPosition());
	}
	
	@Test
	public void testAddAtCentralPositionTrackInReadingQueue() {
		this.readingQueue = new ReadingQueue();
		
		this.readingQueue.addLast(bob);
		this.readingQueue.addLast(aol);	
		this.readingQueue.addAt(1, test);
		
		AudioFile audioFile = this.readingQueue.getAudioFileList().get(1);
		assertEquals(test, audioFile);
		
		assertEquals(bob, this.readingQueue.getCurrentTrack());
		assertEquals(0, this.readingQueue.getCurrentTrackPosition());
	}
	
	@Test
	public void testAddTrackInEmptyReadingQueue() {
		this.readingQueue = new ReadingQueue();
		
		this.readingQueue.addAt(0, test);
		assertEquals(test, this.readingQueue.getCurrentTrack());
		assertEquals(0, this.readingQueue.getCurrentTrackPosition());
		
		AudioFile fistAudioFile = this.readingQueue.getAudioFileList().get(0);
		assertEquals(test, fistAudioFile);
	}

	
	@Test
	public void testRemoveAllTrackInReadingQueue() {
		this.readingQueue = new ReadingQueue();
		
		this.readingQueue.addLast(bob);
		this.readingQueue.addLast(test);
		this.readingQueue.addLast(leNeuf);
		this.readingQueue.addLast(aol);
		
		this.readingQueue.clearQueue();
		assertEquals(true, readingQueue.isEmpty());
		assertEquals(null, this.readingQueue.getCurrentTrack());
		assertEquals(-1, this.readingQueue.getCurrentTrackPosition());
	}
	
	@Test
	public void testRemoveNoCurrentTrackInReadingQueue() {
		this.readingQueue = new ReadingQueue();
		
		this.readingQueue.addLast(bob);
		this.readingQueue.addLast(test);
		this.readingQueue.addLast(leNeuf);
		
		this.readingQueue.remove(2);
		assertEquals(bob, this.readingQueue.getCurrentTrack());
		assertEquals(0, this.readingQueue.getCurrentTrackPosition());
	}
	
	@Test
	public void testRemove1TrackInReadingQueue() {
		this.readingQueue = new ReadingQueue();
		
		this.readingQueue.addLast(bob);
		
		this.readingQueue.remove(0);
		assertEquals(true, readingQueue.isEmpty());
		assertEquals(null, this.readingQueue.getCurrentTrack());
		assertEquals(-1, this.readingQueue.getCurrentTrackPosition());
	}
	
	@Test
	public void testRemoveNoLastCurrentTrack() {
		this.readingQueue = new ReadingQueue();
		
		this.readingQueue.addLast(bob);
		this.readingQueue.addLast(test);
		this.readingQueue.addLast(leNeuf);
		
		this.readingQueue.remove(0);
		assertEquals(test, this.readingQueue.getCurrentTrack());
		assertEquals(0, this.readingQueue.getCurrentTrackPosition());
	}
	
	@Test
	public void testRemoveLastCurrentTrack() {
		this.readingQueue = new ReadingQueue();
		
		this.readingQueue.addLast(bob);
		this.readingQueue.addLast(test);
		this.readingQueue.addLast(leNeuf);
		
		this.readingQueue.setCurrentTrack(leNeuf);
		
		this.readingQueue.remove(2);
		assertEquals(null, this.readingQueue.getCurrentTrack());
		assertEquals(-1, this.readingQueue.getCurrentTrackPosition());
	}
	
	
	
	// Use Case
	@Test
	public void testChangeCurrentTrack_2Next1Prev2Next() {
		this.readingQueue = new ReadingQueue();
		
		this.readingQueue.addLast(test);
		this.readingQueue.addLast(leNeuf);
		this.readingQueue.addLast(aol);
		this.readingQueue.addLast(test);
		
		assertEquals(test, this.readingQueue.getCurrentTrack());
		assertEquals(0, this.readingQueue.getCurrentTrackPosition());
		
		this.readingQueue.next();
		assertEquals(leNeuf, this.readingQueue.getCurrentTrack());
		assertEquals(1, this.readingQueue.getCurrentTrackPosition());
		
		this.readingQueue.next();
		assertEquals(aol, this.readingQueue.getCurrentTrack());
		assertEquals(2, this.readingQueue.getCurrentTrackPosition());
		
		this.readingQueue.prev();
		assertEquals(leNeuf, this.readingQueue.getCurrentTrack());
		assertEquals(1, this.readingQueue.getCurrentTrackPosition());
		
		this.readingQueue.next();
		assertEquals(aol, this.readingQueue.getCurrentTrack());
		assertEquals(2, this.readingQueue.getCurrentTrackPosition());
		
		this.readingQueue.next();
		assertEquals(test, this.readingQueue.getCurrentTrack());
		assertEquals(3, this.readingQueue.getCurrentTrackPosition());
		
		this.readingQueue.next();
		assertEquals(test, this.readingQueue.getCurrentTrack());
		assertEquals(3, this.readingQueue.getCurrentTrackPosition());
		
	}
	
	@Test
	public void testChangeCurrentTrack_2Next3Prev() {
		this.readingQueue = new ReadingQueue();
		
		this.readingQueue.addLast(test);
		this.readingQueue.addLast(leNeuf);
		this.readingQueue.addLast(aol);
		
		assertEquals(test, this.readingQueue.getCurrentTrack());
		assertEquals(0, this.readingQueue.getCurrentTrackPosition());
		
		this.readingQueue.next();
		assertEquals(leNeuf, this.readingQueue.getCurrentTrack());
		assertEquals(1, this.readingQueue.getCurrentTrackPosition());
		
		this.readingQueue.next();
		assertEquals(aol, this.readingQueue.getCurrentTrack());
		assertEquals(2, this.readingQueue.getCurrentTrackPosition());
		
		this.readingQueue.prev();
		assertEquals(leNeuf, this.readingQueue.getCurrentTrack());
		assertEquals(1, this.readingQueue.getCurrentTrackPosition());
		
		this.readingQueue.prev();
		assertEquals(test, this.readingQueue.getCurrentTrack());
		assertEquals(0, this.readingQueue.getCurrentTrackPosition());
		
		this.readingQueue.prev();
		assertEquals(test, this.readingQueue.getCurrentTrack());
		assertEquals(0, this.readingQueue.getCurrentTrackPosition());
		
	}
	
	@Test
	public void testChangeCurrentTrack_3Next() {
		this.readingQueue = new ReadingQueue();
		
		this.readingQueue.addLast(test);
		this.readingQueue.addLast(leNeuf);
		this.readingQueue.addLast(aol);
		
		assertEquals(test, this.readingQueue.getCurrentTrack());
		assertEquals(0, this.readingQueue.getCurrentTrackPosition());
		
		this.readingQueue.next();
		assertEquals(leNeuf, this.readingQueue.getCurrentTrack());
		assertEquals(1, this.readingQueue.getCurrentTrackPosition());
		
		this.readingQueue.next();
		assertEquals(aol, this.readingQueue.getCurrentTrack());
		assertEquals(2, this.readingQueue.getCurrentTrackPosition());
		
		this.readingQueue.next();
		assertEquals(aol, this.readingQueue.getCurrentTrack());
		assertEquals(2, this.readingQueue.getCurrentTrackPosition());
	}
	
	@Test //TODO 
	public void testPlaying() throws InterruptedException {
		this.readingQueue = new ReadingQueue();
		this.readingQueue.addLast(test);
		this.readingQueue.addLast(bob);
		this.readingQueue.addLast(leNeuf);
		this.readingQueue.addLast(test);
		
		for (AudioFile audioFile : this.readingQueue.getAudioFileList()) {
			System.out.println(audioFile.hashCode());
		}
		
		this.readingQueue.getCurrentTrack().play();
		Thread.sleep(5000);
		this.readingQueue.next();
		
		Thread.sleep(5000);
		
		assertEquals(test, this.readingQueue.getCurrentTrack());
		assertEquals(3, this.readingQueue.getCurrentTrackPosition());
	
	
	}
	

}
