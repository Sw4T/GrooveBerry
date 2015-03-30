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

	private Scanner fileScanner;

	@Before
	public void setUp() throws Exception {
		this.library = new Library();
		
		this.bob = new AudioFile("audio/Bob Marley - Jammin.mp3");
		this.test = new AudioFile("audio/test.wav");
		this.leNeuf = new AudioFile("audio/9.wav");
		this.aol = new AudioFile("audio/aol.wav");
		
		this.file = new File("res/library.txt");
		if (!this.file.exists()) {
			this.file.createNewFile();
		}
		
		this.fileScanner = new Scanner(this.file);
	}

	@After
	public void tearDown() throws Exception {
		this.library = null;
		
		this.bob = null;
		this.test = null;
		this.leNeuf = null;
		this.aol = null;
		
		this.fileScanner = null;
		
		if (this.file.exists()) {
			this.file.delete();
		}
		this.file = null;
	}
	

	@Test
	public void testDefaultConstructorCreateAEmptyLibrary() throws IOException {
		this.file.delete();
		this.library = new Library();
		
		assertTrue(this.library.isEmpty());
	}
	
	@Test
	public void testFileLibraryExistInSystem() {
		File file = new File("res/library.txt");
		assertTrue(file.exists());
	}

	@Test
	public void testConstructorCreateALibraryWithAudioFileList() throws IOException {
		ArrayList<AudioFile> audioFileListTest = new ArrayList<>();
		audioFileListTest.add(bob);
		audioFileListTest.add(leNeuf);
		audioFileListTest.add(test);
		audioFileListTest.add(aol);
		
		this.library = new Library(audioFileListTest);
		
		String userName = System.getProperty("user.name");
		String line = this.fileScanner.nextLine();
		assertEquals("Bob Marley - Jammin.mp3#/home/" + userName + "/git/GrooveBerry/GrooveBerry_Serveur/audio/Bob Marley - Jammin.mp3", line);
		line = this.fileScanner.nextLine();
		assertEquals("9.wav#/home/" + userName + "/git/GrooveBerry/GrooveBerry_Serveur/audio/9.wav", line);
		line = this.fileScanner.nextLine();
		assertEquals("test.wav#/home/" + userName + "/git/GrooveBerry/GrooveBerry_Serveur/audio/test.wav", line);
		line = this.fileScanner.nextLine();
		assertEquals("aol.wav#/home/" + userName + "/git/GrooveBerry/GrooveBerry_Serveur/audio/aol.wav", line);
		
		ArrayList<AudioFile> audioFileList  = this.library.getAudioFileList();
		assertEquals(audioFileList.size(), audioFileListTest.size());
		for (int i = 0; i < audioFileList.size(); i++) {
			assertEquals(audioFileListTest.get(i).getName(), audioFileList.get(i).getName());
			assertEquals(audioFileListTest.get(i).getAbsolutePath(), audioFileList.get(i).getAbsolutePath());
		}
	}
	
	@Test
	public void testAddDuplicationIntoLibrary() throws FileNotFoundException
	{
		ArrayList<AudioFile> audioFileListTest = new ArrayList<>();
		audioFileListTest.add(bob);
		audioFileListTest.add(leNeuf);
		audioFileListTest.add(test);
		
		this.library.add("audio/Bob Marley - Jammin.mp3");
		this.library.add("audio/9.wav");
		this.library.add("audio/test.wav");
		this.library.add("audio/Bob Marley - Jammin.mp3");
		
		String userName = System.getProperty("user.name");
		String line = this.fileScanner.nextLine();
		assertEquals("Bob Marley - Jammin.mp3#/home/" + userName + "/git/GrooveBerry/GrooveBerry_Serveur/audio/Bob Marley - Jammin.mp3", line);
		line = this.fileScanner.nextLine();
		assertEquals("9.wav#/home/" + userName + "/git/GrooveBerry/GrooveBerry_Serveur/audio/9.wav", line);
		line = this.fileScanner.nextLine();
		assertEquals("test.wav#/home/" + userName + "/git/GrooveBerry/GrooveBerry_Serveur/audio/test.wav", line);
		
		ArrayList<AudioFile> audioFileList  = this.library.getAudioFileList();
		assertEquals(audioFileListTest.size(), audioFileList.size());
		for (int i = 0; i < audioFileList.size(); i++) {
			assertEquals(audioFileListTest.get(i).getName(), audioFileList.get(i).getName());
			assertEquals(audioFileListTest.get(i).getAbsolutePath(), audioFileList.get(i).getAbsolutePath());
		}
		
	}

	@Test
	public void testUpdateLibraryBaseOnFile() throws IOException {
		ArrayList<AudioFile> audioFileListTest = new ArrayList<>();
		audioFileListTest.add(bob);
		audioFileListTest.add(leNeuf);
		audioFileListTest.add(test);
		audioFileListTest.add(aol);
		
		PrintWriter printWriterOutputFile = new PrintWriter(new FileOutputStream(this.file, true));
		String userName = System.getProperty("user.name");
		printWriterOutputFile.println("Bob Marley - Jammin.mp3#/home/" + userName + "/git/GrooveBerry/GrooveBerry_Serveur/audio/Bob Marley - Jammin.mp3");
		printWriterOutputFile.println("9.wav#/home/" + userName + "/git/GrooveBerry/GrooveBerry_Serveur/audio/9.wav");
		printWriterOutputFile.println("test.wav#/home/" + userName + "/git/GrooveBerry/GrooveBerry_Serveur/audio/test.wav");
		printWriterOutputFile.print("aol.wav#/home/" + userName + "/git/GrooveBerry/GrooveBerry_Serveur/audio/aol.wav");
		printWriterOutputFile.close();
		
		this.library.updateLibrary();
		
		String line = this.fileScanner.nextLine();
		assertEquals("Bob Marley - Jammin.mp3#/home/" + userName + "/git/GrooveBerry/GrooveBerry_Serveur/audio/Bob Marley - Jammin.mp3", line);
		line = this.fileScanner.nextLine();
		assertEquals("9.wav#/home/" + userName + "/git/GrooveBerry/GrooveBerry_Serveur/audio/9.wav", line);
		line = this.fileScanner.nextLine();
		assertEquals("test.wav#/home/" + userName + "/git/GrooveBerry/GrooveBerry_Serveur/audio/test.wav", line);
		line = this.fileScanner.nextLine();
		assertEquals("aol.wav#/home/" + userName + "/git/GrooveBerry/GrooveBerry_Serveur/audio/aol.wav", line);
		
		ArrayList<AudioFile> audioFileList  = this.library.getAudioFileList();
		assertEquals(audioFileListTest.size(), audioFileList.size());
		for (int i = 0; i < audioFileListTest.size(); i++) {
			assertEquals(audioFileListTest.get(i).getName(), audioFileList.get(i).getName());
			assertEquals(audioFileListTest.get(i).getAbsolutePath(), audioFileList.get(i).getAbsolutePath());
		}
		
		
	}
	
	@Test
	public void testUpdateLibraryBaseOnFileWithError() throws IOException {
		ArrayList<AudioFile> audioFileListTest = new ArrayList<>();
		audioFileListTest.add(bob);
		audioFileListTest.add(leNeuf);
		audioFileListTest.add(aol);
		
		PrintWriter printWriterOutputFile = new PrintWriter(new FileOutputStream(this.file, true));
		String userName = System.getProperty("user.name");
		printWriterOutputFile.println("Bob Marley - Jammin.mp3#/home/" + userName + "/git/GrooveBerry/GrooveBerry_Serveur/audio/Bob Marley - Jammin.mp3");
		printWriterOutputFile.println("9.wav#/home/" + userName + "/git/GrooveBerry/GrooveBerry_Serveur/audio/9.wav");
		printWriterOutputFile.println("teGGGst.wav#/home/" + userName + "/git/GrooveBerry/GrooveBerry_Serveur/audio/teGGGst.wav");
		printWriterOutputFile.print("aol.wav#/home/" + userName + "/git/GrooveBerry/GrooveBerry_Serveur/audio/aol.wav");
		printWriterOutputFile.close();
		
		this.library.updateLibrary();
		
		String line = this.fileScanner.nextLine();
		assertEquals("Bob Marley - Jammin.mp3#/home/" + userName + "/git/GrooveBerry/GrooveBerry_Serveur/audio/Bob Marley - Jammin.mp3", line);
		line = this.fileScanner.nextLine();
		assertEquals("9.wav#/home/" + userName + "/git/GrooveBerry/GrooveBerry_Serveur/audio/9.wav", line);
		line = this.fileScanner.nextLine();
		assertEquals("aol.wav#/home/" + userName + "/git/GrooveBerry/GrooveBerry_Serveur/audio/aol.wav", line);
		
		ArrayList<AudioFile> audioFileList  = this.library.getAudioFileList();
		assertEquals(audioFileListTest.size(), audioFileList.size());
		for (int i = 0; i < audioFileListTest.size(); i++) {
			assertEquals(audioFileListTest.get(i).getName(), audioFileList.get(i).getName());
			assertEquals(audioFileListTest.get(i).getAbsolutePath(), audioFileList.get(i).getAbsolutePath());
		}
		
	}
	
	@Test
	public void testRemoveFileInLibrary() throws FileNotFoundException {
		this.library.add("audio/Bob Marley - Jammin.mp3");
		this.library.add("audio/test.wav");
		this.library.add("audio/9.wav");
		
		this.library.remove("audio/test.wav");
		
		ArrayList<AudioFile> audioFileListTest = new ArrayList<>();
		audioFileListTest.add(bob);
		audioFileListTest.add(leNeuf);
		
		ArrayList<AudioFile> audioFileList  = this.library.getAudioFileList();
		assertEquals(2, audioFileList.size());
		for (int i = 0; i < audioFileListTest.size(); i++) {
			assertEquals(audioFileListTest.get(i).getName(), audioFileList.get(i).getName());
			assertEquals(audioFileListTest.get(i).getAbsolutePath(), audioFileList.get(i).getAbsolutePath());
		}
		
		
	}
	
	//
	// Test testMajFileLibrary() fonctionnel avec les chemins windows
	//
	
	@Test
	public void testAddMusicFileToLibrary() throws IOException {
		this.library = new Library();
		
		this.library.add("audio/01 Clandestino.mp3");
		this.library.add("audio/01 Clandestino.mp3");
		
		ArrayList<AudioFile> audioFileList  = this.library.getAudioFileList();
		assertEquals("01 Clandestino.mp3", audioFileList.get(0).getName());
		String userName = System.getProperty("user.name");
		assertEquals("/home/"+ userName + "/git/GrooveBerry/GrooveBerry_Serveur/audio/01 Clandestino.mp3", audioFileList.get(0).getAbsolutePath());
		
		String libraryFirstLineContent = this.fileScanner.nextLine();
		
		assertEquals("01 Clandestino.mp3#/home/"+ userName + "/git/GrooveBerry/GrooveBerry_Serveur/audio/01 Clandestino.mp3", libraryFirstLineContent);
		
		fileScanner.close();
	}
	
	@Test
	public void testMajFileLibrary() throws IOException {
		ArrayList<AudioFile> audioFileList = new ArrayList<>();
		audioFileList.add(bob);
		audioFileList.add(leNeuf);
		audioFileList.add(test);
		audioFileList.add(aol);
		
		this.library = new Library(audioFileList);
		
		File libraryTestFile = new File("res/libraryTest.txt");
		PrintWriter printWriterOutputFile = new PrintWriter(new FileOutputStream(libraryTestFile, true));
		String userName = System.getProperty("user.name");
		printWriterOutputFile.println("Bob Marley - Jammin.mp3#/home/" + userName + "/git/GrooveBerry/GrooveBerry_Serveur/audio/Bob Marley - Jammin.mp3");
		printWriterOutputFile.println("9.wav#/home/" + userName + "/git/GrooveBerry/GrooveBerry_Serveur/audio/9.wav");
		printWriterOutputFile.println("test.wav#/home/" + userName + "/git/GrooveBerry/GrooveBerry_Serveur/audio/test.wav");
		printWriterOutputFile.print("aol.wav#/home/" + userName + "/git/GrooveBerry/GrooveBerry_Serveur/audio/aol.wav");
		printWriterOutputFile.close();

		
		String libraryTestFileContent = "";		
		Scanner fileTestScanner = new Scanner(libraryTestFile);
		while(fileTestScanner.hasNext()) {
			libraryTestFileContent += fileTestScanner.next();
		}
		fileTestScanner.close();
		
		String libraryFileContent = "";	
		while(this.fileScanner.hasNext()) {
			libraryFileContent += this.fileScanner.next();
		}
		
		assertEquals(libraryTestFileContent, libraryFileContent);
		
		libraryTestFile.delete();
	} 

}
