package files;

public class Playlist extends TrackStorage {

	private String name;
	
	public Playlist(String filePath) {
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
