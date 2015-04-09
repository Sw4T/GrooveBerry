package files;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class LibraryTest {
	public static final String DEFAULT_PATHNAME = "res/library.xml";

	private Library library;
	
	private AudioFile bob;
	private AudioFile test;
	private AudioFile leNeuf;
	private AudioFile aol;
	
	private File file;

	private XML_DocBuilder xBuilder;

	private ArrayList<AudioFile> audioFileListTest;
	
	private DocumentBuilderFactory factory;
	private DocumentBuilder builder;
	private Document doc;

	@Before
	public void setUp() {
		this.bob = new AudioFile("audio/Bob Marley - Jammin.mp3");
		this.test = new AudioFile("audio/test.wav");
		this.leNeuf = new AudioFile("audio/9.wav");
		this.aol = new AudioFile("audio/aol.wav");
		this.audioFileListTest = new ArrayList<AudioFile>();
		this.audioFileListTest.add(bob);
		this.audioFileListTest.add(leNeuf);
		this.audioFileListTest.add(test);
		this.audioFileListTest.add(aol);
		xBuilder = new XML_DocBuilder(this.audioFileListTest);
		
		try{
			xBuilder.createXMLDoc(DEFAULT_PATHNAME);
			this.factory = DocumentBuilderFactory.newInstance();
			this.builder= factory.newDocumentBuilder();
			this.library = new Library();			
			
		}
		catch(IOException | ParserConfigurationException | CannotReadException | ReadOnlyFileException | InvalidAudioFrameException e){
			e.printStackTrace();
		}
	}

	@After
	public void tearDown() throws Exception {
		this.library = null;
		
		this.bob = null;
		this.test = null;
		this.leNeuf = null;
		this.aol = null;
		this.xBuilder = null;
		this.audioFileListTest = null;
		this.factory = null;
		this.builder = null;
		this.doc = null;
		
		//if (this.file.exists()) {
			//this.file.delete();
		//}
		//this.file = null;
	}
	

//	@Test
//	public void testDefaultConstructorCreateAEmptyLibrary() throws IOException {
//		//this.file.delete();
//		this.library = new Library();
//		
//		assertTrue(this.library.isEmpty());
//	}
	
//	@Test
//	public void testFileLibraryExistInSystem() {
//		File file = new File("res/library.xml");
//		assertTrue(file.exists());
//	}

	@Test
	public void testConstructorCreateALibraryWithAudioFileList() throws IOException, XPathExpressionException, SAXException {
		this.doc = builder.parse(DEFAULT_PATHNAME);
		this.library = new Library(audioFileListTest);
		
		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpath = xPathfactory.newXPath();
		
		XPathExpression expr = (XPathExpression) xpath.compile("/library/artist/track[@title='Jammin']/fileName/text()");
		String title = (String) expr.evaluate(this.doc,XPathConstants.STRING);
		assertEquals("Bob Marley - Jammin.mp3", title);
		expr = (XPathExpression) xpath.compile("//fileName/text()");
		NodeList fileName = (NodeList) expr.evaluate(this.doc,XPathConstants.NODESET);
		title = fileName.item(0).getNodeValue();
		assertEquals("9.wav", title);
		expr = (XPathExpression) xpath.compile("/library/noArtist/track[fileName='test.wav']/fileName/text()");
		title = (String) expr.evaluate(this.doc,XPathConstants.STRING);
		assertEquals("test.wav", title);
		expr = (XPathExpression) xpath.compile("/library/noArtist/track[fileName='aol.wav']/fileName/text()");
		title = (String) expr.evaluate(this.doc,XPathConstants.STRING);
		assertEquals("aol.wav", title);
		
		ArrayList<AudioFile> audioFileList  = this.library.getAudioFileList();
		assertEquals(audioFileList.size(), this.audioFileListTest.size());
		for (int i = 0; i < audioFileList.size(); i++) {
			assertEquals(audioFileListTest.get(i).getName(), audioFileList.get(i).getName());
			assertEquals(audioFileListTest.get(i).getAbsolutePath(), audioFileList.get(i).getAbsolutePath());
		}
	}
/*
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
	
	@Test
	public void testCreateAndSearchHeyJoe() throws IOException {
		AudioFileScanner libraryScanner = new AudioFileScanner("audioTest/");
		ArrayList<AudioFile> audioFileList = libraryScanner.getAudioFileList();
		this.library = new Library(audioFileList);
		
		ArrayList<AudioFile> audioFileTest = new ArrayList<>();
		audioFileTest.add(audioFileList.get(3));
		assertEquals(audioFileTest.get(0), this.library.getTrackByName("Hey Joe").get(0));
	}
	
	@Test
	public void testCreateAndSearchMp3() throws IOException {
		AudioFileScanner libraryScanner = new AudioFileScanner("audioTest/");
		ArrayList<AudioFile> audioFileList = libraryScanner.getAudioFileList();
		this.library = new Library(audioFileList);
		
		ArrayList<AudioFile> audioFileTest = new ArrayList<>();
		audioFileTest.add(audioFileList.get(0));
		audioFileTest.add(audioFileList.get(2));
		audioFileTest.add(audioFileList.get(3));
		assertEquals(audioFileTest.get(0), this.library.getTrackByName("mp3").get(0));
		assertEquals(audioFileTest.get(1), this.library.getTrackByName("mp3").get(1));
		assertEquals(audioFileTest.get(2), this.library.getTrackByName("mp3").get(2));
	}
*/
}
