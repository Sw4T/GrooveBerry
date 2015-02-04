package files;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Library extends TrackStorage {
	public static final String DEFAULT_PATHNAME = "res/library.txt";
	public static final String DELIMITER = "#";

	public Library() {
		super(DEFAULT_PATHNAME);
	}
	
	public Library(ArrayList<AudioFile> audioFileList) throws FileNotFoundException {
		this();
		this.audioFileList = new ArrayList<>(audioFileList);
		this.updateLibraryFile();
	}

	//TODO overwrite add() to kill doublons
}
