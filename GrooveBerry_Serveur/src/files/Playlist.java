package files;

import java.io.IOException;

public class Playlist extends TrackStorage {

	private String name;
	
	public Playlist(String filePath) throws IOException {
		super(filePath);
		this.name ="default";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
