package files;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import org.junit.Test;


public class TagReaderTest {
	private TagReader fileTags;

	@Test
	public void testMP3Tags() throws CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException {
		this.fileTags = new TagReader(new AudioFile("audio/01 Clandestino.mp3"));
		assertEquals("Clandestino", fileTags.getAlbum());
		assertEquals("Manu Chao", fileTags.getArtist());
		assertEquals("Clandestino", fileTags.getSong());
		assertEquals("1998", fileTags.getSongYear());
		assertEquals("1", fileTags.getTrackNumberOnAlbum());
	}
	
	@Test
	public void testWAVTags() throws CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException {
		this.fileTags = new TagReader(new AudioFile("audio/free.wav"));
		assertEquals("", fileTags.getAlbum());
		assertEquals("", fileTags.getArtist());
		assertEquals("", fileTags.getSong());
		assertEquals("", fileTags.getSongYear());
		assertEquals("", fileTags.getTrackNumberOnAlbum());
	}

}
