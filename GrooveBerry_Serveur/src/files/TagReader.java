package files;

import java.io.File;
import java.io.IOException;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;




public class TagReader {
	private Tag tag;
	
	public TagReader(files.AudioFile audioFile) throws CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException {
		AudioFile tagAudioFile =  AudioFileIO.read(new File(audioFile.getPath()));
		this.tag = tagAudioFile.getTag();
	}
	
	public String getArtist(){
		return this.tag.getFirst(FieldKey.ARTIST);
	}
 
	public String getAlbum(){
		return this.tag.getFirst(FieldKey.ALBUM);
	}
 
	public String getSong(){
		return this.tag.getFirst(FieldKey.TITLE);
	}
 
	public String getTrackNumberOnAlbum(){
		return this.tag.getFirst(FieldKey.TRACK);
	}
 
	public String getSongYear(){
		return tag.getFirst(FieldKey.YEAR);
	}
}
