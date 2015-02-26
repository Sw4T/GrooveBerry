package files;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ReadingQueueTest {
	private ReadingQueue readingQueue;
	private ReadingQueue readingQueueTest;
	private ArrayList<AudioFile> audioFileList;

	private AudioFile bob = new AudioFile("audio/Bob Marley - Jammin.mp3");
	private AudioFile test = new AudioFile("audio/test.wav");
	private AudioFile leNeuf = new AudioFile("audio/9.wav");
	private AudioFile aol = new AudioFile("audio/aol.wav");

	@Before
	public void setUp() throws Exception {
		this.bob = new AudioFile("audio/Bob Marley - Jammin.mp3");
		this.test = new AudioFile("audio/test.wav");
		this.leNeuf = new AudioFile("audio/9.wav");
		this.aol = new AudioFile("audio/aol.wav");
		
		this.audioFileList = new ArrayList<>();
		audioFileList.add(bob);
		audioFileList.add(leNeuf);
		audioFileList.add(test);
		audioFileList.add(aol);
		
		this.readingQueue = new ReadingQueue();
		this.readingQueueTest = new ReadingQueue(audioFileList);
	}

	@After
	public void tearDown() throws Exception {
		this.bob = null;
		this.test = null;
		this.leNeuf = null;
		this.aol = null;
		
		this.audioFileList = null;
		
		this.readingQueue = null;
		this.readingQueueTest = null;
	}

	@Test
	public void testCreateAEmptyReadingQueue() {
		assertEquals(true, this.readingQueue.isEmpty());
	}
	
	@Test
	public void testAReadingQueueWithAudioFileList() {
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
		this.readingQueue.addLast(bob);
		assertEquals(false, this.readingQueue.isEmpty());
		
		AudioFile lastAudioFile = this.readingQueue.getAudioFileList().get(this.readingQueue.getAudioFileList().size()-1);
		assertEquals(bob, lastAudioFile);
		assertEquals(bob, this.readingQueue.getCurrentTrack());
		assertEquals(0, this.readingQueue.getCurrentTrackPosition());
	}
	
	@Test
	public void testAddLastInNotEmptyReadingQueue() {
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
		assertEquals(bob, this.readingQueueTest.getCurrentTrack());
		assertEquals(0, this.readingQueueTest.getCurrentTrackPosition());
	}
	
	@Test
	public void testAddAtFirstPositionTrackInReadingQueue() {
		this.readingQueue.addLast(bob);	
		this.readingQueue.addAt(0, test);
		
		AudioFile firstAudioFile = this.readingQueue.getAudioFileList().get(0);
		assertEquals(test, firstAudioFile);
		
		assertEquals(bob, this.readingQueue.getCurrentTrack());
		assertEquals(1, this.readingQueue.getCurrentTrackPosition());
	}
	
	@Test
	public void testAddAtCentralPositionTrackInReadingQueue() {
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
		this.readingQueue.addAt(0, test);
		assertEquals(test, this.readingQueue.getCurrentTrack());
		assertEquals(0, this.readingQueue.getCurrentTrackPosition());
		
		AudioFile fistAudioFile = this.readingQueue.getAudioFileList().get(0);
		assertEquals(test, fistAudioFile);
	}

	
	@Test
	public void testRemoveAllTrackInReadingQueue() {
		this.readingQueueTest.clearQueue();
		assertEquals(true, readingQueueTest.isEmpty());
		assertEquals(null, this.readingQueueTest.getCurrentTrack());
		assertEquals(-1, this.readingQueueTest.getCurrentTrackPosition());
	}
	
	@Test
	public void testRemoveNoCurrentTrackInReadingQueue() {
		this.readingQueue.addLast(bob);
		this.readingQueue.addLast(test);
		this.readingQueue.addLast(leNeuf);
		
		this.readingQueue.remove(2);
		assertEquals(bob, this.readingQueue.getCurrentTrack());
		assertEquals(0, this.readingQueue.getCurrentTrackPosition());
	}
	
	@Test
	public void testRemove1TrackInReadingQueue() {
		this.readingQueue.addLast(bob);
		
		this.readingQueue.remove(0);
		assertEquals(true, readingQueue.isEmpty());
		assertEquals(null, this.readingQueue.getCurrentTrack());
		assertEquals(-1, this.readingQueue.getCurrentTrackPosition());
	}
	
	@Test
	public void testRemoveNoLastCurrentTrack() {
		this.readingQueue.addLast(bob);
		this.readingQueue.addLast(test);
		this.readingQueue.addLast(leNeuf);
		
		this.readingQueue.remove(0);
		assertEquals(test, this.readingQueue.getCurrentTrack());
		assertEquals(0, this.readingQueue.getCurrentTrackPosition());
	}
	
	@Test
	public void testRemoveLastCurrentTrack() {
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
		assertEquals(bob, this.readingQueueTest.getCurrentTrack());
		assertEquals(0, this.readingQueueTest.getCurrentTrackPosition());
		
		this.readingQueueTest.next();
		assertEquals(leNeuf, this.readingQueueTest.getCurrentTrack());
		assertEquals(1, this.readingQueueTest.getCurrentTrackPosition());
		
		this.readingQueueTest.next();
		assertEquals(test, this.readingQueueTest.getCurrentTrack());
		assertEquals(2, this.readingQueueTest.getCurrentTrackPosition());
		
		this.readingQueueTest.prev();
		assertEquals(leNeuf, this.readingQueueTest.getCurrentTrack());
		assertEquals(1, this.readingQueueTest.getCurrentTrackPosition());
		
		this.readingQueueTest.next();
		assertEquals(test, this.readingQueueTest.getCurrentTrack());
		assertEquals(2, this.readingQueueTest.getCurrentTrackPosition());
		
		this.readingQueueTest.next();
		assertEquals(aol, this.readingQueueTest.getCurrentTrack());
		assertEquals(3, this.readingQueueTest.getCurrentTrackPosition());
		
		this.readingQueueTest.next();
		assertEquals(aol, this.readingQueueTest.getCurrentTrack());
		assertEquals(3, this.readingQueueTest.getCurrentTrackPosition());
		
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
	
	@Test 
	public void testPlaying() throws InterruptedException {
		this.readingQueueTest.getCurrentTrack().play();
		Thread.sleep(5000);
		this.readingQueueTest.next();
		
		Thread.sleep(10000);
		
		assertEquals(aol, this.readingQueueTest.getCurrentTrack());
		assertEquals(3, this.readingQueueTest.getCurrentTrackPosition());
	
	
	}
	
	@Test 
	public void testPlayingNextPrev() throws InterruptedException {
		this.readingQueueTest.getCurrentTrack().play();
		Thread.sleep(1000);
		this.readingQueueTest.next();
		
		
		assertEquals(leNeuf, this.readingQueueTest.getCurrentTrack());
		assertEquals(1, this.readingQueueTest.getCurrentTrackPosition());
		assertEquals(true, this.readingQueueTest.getCurrentTrack().isPlaying());
		assertEquals(false, this.readingQueueTest.getCurrentTrack().isPaused());
		this.readingQueueTest.prev();
		assertEquals(bob, this.readingQueueTest.getCurrentTrack());
		assertEquals(0, this.readingQueueTest.getCurrentTrackPosition());
		assertEquals(true, this.readingQueueTest.getCurrentTrack().isPlaying());
		assertEquals(false, this.readingQueueTest.getCurrentTrack().isPaused());
	}
	
	@Test 
	public void testNextPrevPause() throws InterruptedException {
		this.readingQueueTest.getCurrentTrack().play();
		Thread.sleep(1000);
		assertEquals(bob,this.readingQueueTest.getCurrentTrack());
		this.readingQueueTest.getCurrentTrack().pause();
		this.readingQueueTest.next();
		
		assertEquals(leNeuf, this.readingQueueTest.getCurrentTrack());
		assertEquals(1, this.readingQueueTest.getCurrentTrackPosition());
		assertEquals(true, this.readingQueueTest.getCurrentTrack().isPlaying());
		assertEquals(false, this.readingQueueTest.getCurrentTrack().isPaused());
		this.readingQueueTest.getCurrentTrack().pause();
		this.readingQueueTest.prev();
		assertEquals(bob, this.readingQueueTest.getCurrentTrack());
		assertEquals(0, this.readingQueueTest.getCurrentTrackPosition());
		assertEquals(true, this.readingQueueTest.getCurrentTrack().isPlaying());
		assertEquals(false, this.readingQueueTest.getCurrentTrack().isPaused());
	}
	
	@Test 
	public void testNextPrevMute() throws InterruptedException {
		this.readingQueueTest = new ReadingQueue(audioFileList);
		this.readingQueueTest.getCurrentTrack().play();
		this.readingQueueTest.getCurrentTrack().mute();
		this.readingQueueTest.next();
		
		assertEquals(leNeuf, this.readingQueueTest.getCurrentTrack());
		assertEquals(1, this.readingQueueTest.getCurrentTrackPosition());
		assertEquals(true, this.readingQueueTest.getCurrentTrack().isPlaying());
		assertEquals(true, this.readingQueueTest.getCurrentTrack().isMuted());
		this.readingQueueTest.prev();
		
		assertEquals(bob, this.readingQueueTest.getCurrentTrack());
		assertEquals(0, this.readingQueueTest.getCurrentTrackPosition());
		assertEquals(true, this.readingQueueTest.getCurrentTrack().isPlaying());
		assertEquals(true, this.readingQueueTest.getCurrentTrack().isMuted());
		this.readingQueueTest.getCurrentTrack().mute();
		assertEquals(false, this.readingQueueTest.getCurrentTrack().isMuted());
	}
	
	@Test
	public void testNextPrevLoop() throws InterruptedException {
		this.readingQueue = new ReadingQueue();
		this.readingQueue.addLast(aol);
		this.readingQueue.addLast(bob);
		this.readingQueue.addLast(leNeuf);
		this.readingQueue.addLast(test);
		
		this.readingQueue.getCurrentTrack().play();
		this.readingQueue.getCurrentTrack().loop();
		Thread.sleep(5000);
		this.readingQueue.next();
		
		
		assertEquals(bob, this.readingQueue.getCurrentTrack());
		assertEquals(1, this.readingQueue.getCurrentTrackPosition());
		assertEquals(true, this.readingQueue.getCurrentTrack().isPlaying());
		assertEquals(true, this.readingQueue.getCurrentTrack().isLooping());
		
		this.readingQueue.getCurrentTrack().loop();
		assertEquals(false, this.readingQueue.getCurrentTrack().isLooping());
		this.readingQueue.prev();
		
		assertEquals(aol, this.readingQueue.getCurrentTrack());
		assertEquals(0, this.readingQueue.getCurrentTrackPosition());
		assertEquals(true, this.readingQueue.getCurrentTrack().isPlaying());
		assertEquals(false, this.readingQueue.getCurrentTrack().isLooping());
		
		
	}
	
	@Test 
	public void testNextPrevRandom() throws InterruptedException {
		this.readingQueue = new ReadingQueue();
		this.readingQueue.addLast(aol);
		this.readingQueue.addLast(bob);
		this.readingQueue.addLast(leNeuf);
		this.readingQueue.addLast(test);
		
		this.readingQueue.getCurrentTrack().play();
		this.readingQueue.getCurrentTrack().random();
		assertEquals(aol, this.readingQueue.getCurrentTrack());
		assertEquals(0, this.readingQueue.getCurrentTrackPosition());
		assertEquals(true, this.readingQueue.getCurrentTrack().isPlaying());
		assertEquals(true, this.readingQueue.getCurrentTrack().isRandomised());
		this.readingQueue.next();
		assertEquals(true, this.readingQueue.getCurrentTrack().isRandomised());
		this.readingQueue.getCurrentTrack().random();
		assertEquals(false, this.readingQueue.getCurrentTrack().isRandomised());
		this.readingQueue.prev();
		
		assertEquals(true, this.readingQueue.getCurrentTrack().isPlaying());
		assertEquals(false, this.readingQueue.getCurrentTrack().isRandomised());
		
		
	}
	
	@Test
	public void testAddListInEmptyReadingQueue(){
	this.readingQueue = new ReadingQueue();
	ArrayList<AudioFile> playlist = new ArrayList<AudioFile>();
	playlist.add(leNeuf);
	playlist.add(test);
	playlist.add(aol);
	playlist.add(bob);
	this.readingQueue.addList(playlist);
	
	assertEquals(leNeuf,this.readingQueue.getAudioFileList().get(0));
	assertEquals(test,this.readingQueue.getAudioFileList().get(1));
	assertEquals(aol,this.readingQueue.getAudioFileList().get(2));
	assertEquals(bob,this.readingQueue.getAudioFileList().get(3));
	
	}
	
	@Test
	public void testAddListAtEndOfReadingQueue(){
	this.readingQueue = new ReadingQueue();
	ArrayList<AudioFile> playlist = new ArrayList<AudioFile>();
	playlist.add(test);
	playlist.add(aol);
	playlist.add(bob);
	this.readingQueue.addLast(leNeuf);
	this.readingQueue.addList(playlist);
	
	assertEquals(leNeuf,this.readingQueue.getAudioFileList().get(0));
	assertEquals(test,this.readingQueue.getAudioFileList().get(1));
	assertEquals(aol,this.readingQueue.getAudioFileList().get(2));
	assertEquals(bob,this.readingQueue.getAudioFileList().get(3));
	}
	
	@Test
	public void testAddListAtMiddleOfReadingQueue(){
	this.readingQueue = new ReadingQueue();
	ArrayList<AudioFile> playlist = new ArrayList<AudioFile>();
	playlist.add(aol);
	playlist.add(bob);
	this.readingQueue.addLast(leNeuf);
	this.readingQueue.addLast(test);
	this.readingQueue.addListAt(1, playlist);
	
	assertEquals(leNeuf,this.readingQueue.getAudioFileList().get(0));
	assertEquals(aol,this.readingQueue.getAudioFileList().get(1));
	assertEquals(bob,this.readingQueue.getAudioFileList().get(2));
	assertEquals(test,this.readingQueue.getAudioFileList().get(3));
	}
}

