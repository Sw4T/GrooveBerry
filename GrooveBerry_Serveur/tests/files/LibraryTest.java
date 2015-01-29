package files;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LibraryTest {
	private Library library;
	
	private AudioFile bob;
	private AudioFile test;
	private AudioFile leNeuf;
	private AudioFile aol;

	@Before
	public void setUp() throws Exception {
		this.library = new Library();
		
		this.bob = new AudioFile("audio/Bob Marley - Jammin.mp3");
		this.test = new AudioFile("audio/test.wav");
		this.leNeuf = new AudioFile("audio/9.wav");
		this.aol = new AudioFile("audio/aol.wav");
	}

	@After
	public void tearDown() throws Exception {
		this.library = null;
		
		this.bob = null;
		this.test = null;
		this.leNeuf = null;
		this.aol = null;
	}

	@Test
	public void testDefaultConstructorCreateAnEmptyLibrary() {
		this.library = new Library();
		
		assertTrue(this.library.isEmpty());
	}
	
	@Test
	public void testFileLibraryExistInSystem() {
		File file = new File("res/library.txt");
		assertTrue(file.exists());
	}
	
	@Test
	public void testMajFileLibrary() throws FileNotFoundException {
		ArrayList<AudioFile> audioFileList = new ArrayList<>();
		audioFileList.add(bob);
		audioFileList.add(leNeuf);
		audioFileList.add(test);
		audioFileList.add(aol);
		
		this.library = new Library(audioFileList);
		this.library.setLibraryFilePathName("res/library.txt");
		this.library.updateLibraryFile();
		
		String libraryTestFileContent = "", libraryFileContent = "";
		
		File libraryTestFile = new File("res/libraryTest.txt");
		Scanner fileScanner = new Scanner(libraryTestFile);
		while(fileScanner.hasNext()) {
			libraryTestFileContent += fileScanner.next();
		}
		
		File libraryFile = new File("res/library.txt");
		fileScanner.close();
		fileScanner = new Scanner(libraryFile);
		while(fileScanner.hasNext()) {
			libraryFileContent += fileScanner.next();
		}
		
		assertEquals(libraryTestFileContent, libraryFileContent);
		fileScanner.close();
	}
	
	@Test
	public void testMajLibrary() throws FileNotFoundException {
		ArrayList<AudioFile> audioFileList = new ArrayList<>();
		audioFileList.add(bob);
		audioFileList.add(leNeuf);
		audioFileList.add(test);
		audioFileList.add(aol);
		
		this.library = new Library();
		this.library.updateLibrary();
		
		assertEquals(audioFileList, this.library.getAudioFileList());
	}
	
	@Test
	public void testFileLibrary() {
		
	}
	
	@Test
	public void testCreateALibraryWithAudioFile() {
		ArrayList<AudioFile> audioFileList = new ArrayList<AudioFile>();
		this.library = new Library(audioFileList);
		
		assertEquals(audioFileList, this.library.getAudioFileList());
	}

}
