package files;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
	
	private File file;

	@Before
	public void setUp() throws Exception {
		this.library = new Library();
		
		this.bob = new AudioFile("audio/Bob Marley - Jammin.mp3");
		this.test = new AudioFile("audio/test.wav");
		this.leNeuf = new AudioFile("audio/9.wav");
		this.aol = new AudioFile("audio/aol.wav");
		
		this.file = new File("res/library.txt");
	}

	@After
	public void tearDown() throws Exception {
		this.library = null;
		
		this.bob = null;
		this.test = null;
		this.leNeuf = null;
		this.aol = null;
		
		this.file.delete();
		this.file = null;
	}
	

	@Test
	public void testDefaultConstructorCreateAEmptyLibrary() {
		this.library = new Library();
		
		assertTrue(this.library.isEmpty());
	}
	
	@Test
	public void testFileLibraryExistInSystem() {
		File file = new File("res/library.txt");
		assertTrue(file.exists());
	}

	@Test
	public void testConstructorCreateALibraryWithAudioFileList() throws FileNotFoundException {
		ArrayList<AudioFile> audioFileListTest = new ArrayList<>();
		audioFileListTest.add(bob);
		audioFileListTest.add(leNeuf);
		audioFileListTest.add(test);
		audioFileListTest.add(aol);
		
		this.library = new Library(audioFileListTest);
		
		ArrayList<AudioFile> audioFileList  = this.library.getAudioFileList();
		assertEquals(audioFileList.size(), audioFileListTest.size());
		for (int i = 0; i < audioFileList.size(); i++) {
			assertEquals(audioFileListTest.get(i).getName(), audioFileList.get(i).getName());
			assertEquals(audioFileListTest.get(i).getAbsolutePath(), audioFileList.get(i).getAbsolutePath());
		}
	} 

	@Test
	public void testUpdateLibraryBaseOnFile() throws FileNotFoundException {
		ArrayList<AudioFile> audioFileListTest = new ArrayList<>();
		audioFileListTest.add(bob);
		audioFileListTest.add(leNeuf);
		audioFileListTest.add(test);
		audioFileListTest.add(aol);
		
		PrintWriter printWriterOutputFile = new PrintWriter(new FileOutputStream(this.file, true));
		printWriterOutputFile.println("Bob Marley - Jammin.mp3#H:\\git\\GrooveBerry\\GrooveBerry_Serveur\\audio\\Bob Marley - Jammin.mp3");
		printWriterOutputFile.println("9.wav#H:\\git\\GrooveBerry\\GrooveBerry_Serveur\\audio\\9.wav");
		printWriterOutputFile.println("test.wav#H:\\git\\GrooveBerry\\GrooveBerry_Serveur\\audio\\test.wav");
		printWriterOutputFile.print("aol.wav#H:\\git\\GrooveBerry\\GrooveBerry_Serveur\\audio\\aol.wav");
		
		printWriterOutputFile.close();
		
		this.library.updateLibrary();
		ArrayList<AudioFile> audioFileList  = this.library.getAudioFileList();
		assertEquals(audioFileListTest.size(), audioFileList.size());
		for (int i = 0; i < audioFileListTest.size(); i++) {
			assertEquals(audioFileListTest.get(i).getName(), audioFileList.get(i).getName());
			assertEquals(audioFileListTest.get(i).getAbsolutePath(), audioFileList.get(i).getAbsolutePath());
		}
		
		
	}
	
	@Test
	public void testUpdateLibraryBaseOnFileWithError() throws FileNotFoundException {
		ArrayList<AudioFile> audioFileListTest = new ArrayList<>();
		audioFileListTest.add(bob);
		audioFileListTest.add(leNeuf);
		audioFileListTest.add(aol);
		
		PrintWriter printWriterOutputFile = new PrintWriter(new FileOutputStream(this.file, true));
		printWriterOutputFile.println("Bob Marley - Jammin.mp3#H:\\git\\GrooveBerry\\GrooveBerry_Serveur\\audio\\Bob Marley - Jammin.mp3");
		printWriterOutputFile.println("9.wav#H:\\git\\GrooveBerry\\GrooveBerry_Serveur\\audio\\9.wav");
		printWriterOutputFile.println("teGGGt.wav#H:\\git\\GrooveBerry\\GrooveBerry_Serveur\\audio\\teGGGst.wav");
		printWriterOutputFile.println("aol.wav#H:\\git\\GrooveBerry\\GrooveBerry_Serveur\\audio\\aol.wav");
		
		printWriterOutputFile.close();
		
		this.library.updateLibrary();
		ArrayList<AudioFile> audioFileList  = this.library.getAudioFileList();
		assertEquals(audioFileListTest.size(), audioFileList.size());
		for (int i = 0; i < audioFileListTest.size(); i++) {
			assertEquals(audioFileListTest.get(i).getName(), audioFileList.get(i).getName());
			assertEquals(audioFileListTest.get(i).getAbsolutePath(), audioFileList.get(i).getAbsolutePath());
		}
		
	}
	
	//
	// Test testAddMusicFileToLibrary() et testMajFileLibrary() fonctionnel avec les chemins windows
	//
	
	@Test
	public void testAddMusicFileToLibrary() throws FileNotFoundException {
		this.library = new Library();
		
		this.library.add("audio/01 Clandestino.mp3");
		this.library.add("audio/01 Clandestino.mp3");
		
		ArrayList<AudioFile> audioFileList  = this.library.getAudioFileList();
		assertEquals("01 Clandestino.mp3", audioFileList.get(0).getName());
		assertEquals("H:\\git\\GrooveBerry\\GrooveBerry_Serveur\\audio\\01 Clandestino.mp3", audioFileList.get(0).getAbsolutePath());
		
		File libraryFile = new File("res/library.txt");
		Scanner fileScanner = new Scanner(libraryFile);
		String libraryFirstLineContent = fileScanner.nextLine();
		
		assertEquals("01 Clandestino.mp3#H:\\git\\GrooveBerry\\GrooveBerry_Serveur\\audio\\01 Clandestino.mp3", libraryFirstLineContent);
		
		fileScanner.close();
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

}
