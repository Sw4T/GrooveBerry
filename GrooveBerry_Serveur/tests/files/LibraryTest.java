package files;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LibraryTest {
	private Library library;

	@Before
	public void setUp() throws Exception {
		this.library = new Library();
	}

	@After
	public void tearDown() throws Exception {
		this.library = null;
	}

	@Test
	public void testDefaultConstructorCreateAnEmptyLibrary() {
		this.library = new Library();
		
		assertTrue(this.library.isEmpty());
	}
	
	@Test
	public void testDefaultConstructorCreateEmptyFileInSystem() {
		
	}
	
	@Test
	public void testCreateALibraryWithAudioFile() {
		ArrayList<AudioFile> audioFileList = new ArrayList<AudioFile>();
		this.library = new Library(audioFileList);
		
		assertEquals(audioFileList, this.library.getAudioFileList());
	}

}
