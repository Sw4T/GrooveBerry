package files;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import files.AudioFile;

public class XML_DocBuilder {

	private ArrayList<AudioFile> audioFileList;
	
	public XML_DocBuilder(ArrayList<AudioFile> audioFileList) {
		this.audioFileList = audioFileList;
	}
	
	public void createXMLDoc(String path) throws CannotReadException, IOException, ReadOnlyFileException, InvalidAudioFrameException {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("library");
			doc.appendChild(rootElement);
			Element artist = doc.createElement("noArtist"); //node pour les artistes non tagés 
			rootElement.appendChild(artist);
			for (AudioFile audioFile : this.audioFileList) {
				TagReader tag = new TagReader(audioFile);
				artist = doc.createElement("artist");
				Attr name = doc.createAttribute("name");
				boolean notExist = true;
				
				//Parcour des artistes pour les morceaux tagés afin de voir si l'artiste existe déjà et positionnement dessus si oui
				if (!tag.getArtist().equals("")){
					for (int i=0; i < rootElement.getChildNodes().getLength() && notExist; i++){
						Element currentNode = (Element) rootElement.getChildNodes().item(i);
						if(currentNode.getNodeName().equals("artist") && currentNode.getAttribute("name").equals(tag.getArtist())){
							notExist = false;
							artist = currentNode;
						}
					}
					if(notExist){
						rootElement.appendChild(artist);
						artist.setAttribute("name", tag.getArtist());
					}
				}
				//cas des artistes non tagés
				else{
				artist = (Element) rootElement.getFirstChild();	
				}
				
				Element album = doc.createElement("album");
				Attr albumTitle = doc.createAttribute("title");
				notExist = true;
				if(!tag.getAlbum().equals("")){
					
					for(int i=0; i < artist.getChildNodes().getLength() && notExist; i++){
						Element currentNode = (Element) artist.getChildNodes().item(i);
						if(currentNode.getNodeName().equals("album") && currentNode.getAttribute("title").equals(tag.getAlbum()));{
							notExist = false;
							album = currentNode;
						}
					}
					if(notExist){
						album.setAttribute("title", tag.getAlbum());
						artist.appendChild(album);
						notExist = false;
					}
				}
				Element track = doc.createElement("track");
				if(!tag.getSongYear().equals("")){
					Attr date = doc.createAttribute("year");
					track.setAttribute("year", tag.getSongYear());
				}
				if(!tag.getSong().equals("")){
					Attr title = doc.createAttribute("title");
					track.setAttribute("title", tag.getSong());
				}
				if(notExist){
					
					artist.appendChild(track);
				}
				else{
					album.appendChild(track);
				}
				Element fileName = doc.createElement("fileName");
				fileName.appendChild(doc.createTextNode(audioFile.getName()));
				track.appendChild(fileName);
				Element filePath = doc.createElement("path");
				filePath.appendChild(doc.createTextNode(audioFile.getAbsolutePath()));
				track.appendChild(filePath);
			}
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(path));
			transformer.transform(source, result);
		} catch (ParserConfigurationException | TransformerException | TagException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}