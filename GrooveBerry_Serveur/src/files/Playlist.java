package files;

import java.io.IOException;

public class Playlist extends TrackStorage {

	private String name;
	
	public Playlist(String filePath) throws IOException {
		this(filePath, "default");
	}
	
	public Playlist(String filePath, String name) throws IOException {
		super(filePath);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
