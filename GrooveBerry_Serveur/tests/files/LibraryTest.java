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
			this.doc = builder.parse(DEFAULT_PATHNAME);
			this.library = new Library();		
		}
		catch(IOException | ParserConfigurationException | CannotReadException | ReadOnlyFileException | InvalidAudioFrameException | SAXException e){
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
		this.library = null;
	}

	@Test
	public void testConstructorCreateALibraryWithAudioFileList() throws IOException, XPathExpressionException, SAXException {
		this.audioFileListTest.add(test);
		this.library = new Library(this.audioFileListTest);
		
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
}