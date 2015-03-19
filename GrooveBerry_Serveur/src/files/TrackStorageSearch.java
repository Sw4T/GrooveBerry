package files;

import java.util.ArrayList;

public class TrackStorageSearch {
	private TrackStorage trackStorage;
	
	public TrackStorageSearch(TrackStorage trackStorage) {
		this.trackStorage = trackStorage;
	}

	public ArrayList<AudioFile> getTrackByName(String searchSequence) {
		ArrayList<AudioFile> result = new ArrayList<>();
		for (AudioFile audioFile : this.trackStorage.getAudioFileList()) {
			if (audioFile.getName().contains(searchSequence)) {
				result.add(audioFile);
			}
		}
		
		return result;
	}	

}
