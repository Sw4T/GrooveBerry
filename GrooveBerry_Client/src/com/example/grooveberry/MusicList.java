package com.example.grooveberry;

public class MusicList {

	private static String [] listOfMusic = new String [15];
	private static int musicNb;
	
	public MusicList () {
		MusicList.listOfMusic[0] = "audio/Bob Marley - Jammin.mp3";
		MusicList.listOfMusic[1] = "audio/Lalal_tamère_Bastien.mp3";
		MusicList.musicNb = 0;
	}
	
	public static String getFilePlaying() {
		return listOfMusic[musicNb];
	}
	
	public String[] getListOfMusic () {
		return MusicList.listOfMusic;
	}
	
	public static int getMusicNb () {
		return MusicList.musicNb;
	}
	
	public static void nextMusic () {
		MusicList.musicNb++;
	}
	
	public static void previousMusic () {
		MusicList.musicNb--;
	}
	
	public static int countMusicInList () {
		return MusicList.listOfMusic.length;
	}
}
