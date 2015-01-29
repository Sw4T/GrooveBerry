package com.example.grooveberry;

import java.util.ArrayList;

public class MusicList {

	private static ArrayList<String> listOfMusic;
	private static int musicNb;
	
	public MusicList () {
		//Récupérer la liste des fichiers présents dans audio/ 
		//sur le serveur
//		MusicList.listOfMusic[0] = "audio/Bob Marley - Jammin.mp3";
//		MusicList.listOfMusic[1] = "audio/Lalal_tamère_Bastien.mp3";
//		MusicList.listOfMusic[2] = null;
		MusicList.listOfMusic = new ArrayList<String> ();
		MusicList.listOfMusic.add("audio/Bob Marley - Jammin.mp3");
		MusicList.listOfMusic.add("audio/Lalal_tamère_Bastien.mp3");
		MusicList.musicNb = 0;
	}
	
	public static String getFilePlaying() {
		return MusicList.listOfMusic.get(musicNb);
	}
	
	public ArrayList<String> getListOfMusic () {
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
		int i = 0;
		
		for (String s : MusicList.listOfMusic) {
			if (s == null) {
				return i;
			}
			i++;
		}
		return i-1;
	}
}
