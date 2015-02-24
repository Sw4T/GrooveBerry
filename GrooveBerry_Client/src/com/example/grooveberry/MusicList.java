package com.example.grooveberry;

import java.util.ArrayList;

public class MusicList {


	private ArrayList<String> listOfMusic;
	private int musicNb;
	private String nextMusicName = "";
	
	public MusicList (ArrayList<String> list) {
		listOfMusic = list;
		musicNb = 0;
	}
	
	public String getFilePlaying() {
		return listOfMusic.get(musicNb);
	}
	
	public ArrayList<String> getListOfMusic () {
		return listOfMusic;
	}
	
	public int getMusicNb () {
		return musicNb;
	}
	
	public void nextMusic () {
		musicNb++;
	}
	
	public void previousMusic () {
		musicNb--;
	}
	
	public int countMusicInList () {
		int i = 0;
		
		for (String s : listOfMusic) {
			if (s == null) {
				return i;
			}
			i++;
		}
		return i-1;
	}
	
	public String[] convertToStringTab() {
		String[] list = this.listOfMusic.toArray(new String[this.listOfMusic.size()]);
		
		return list;
	}
	
	public void setNextMusicName (String name) {
		this.nextMusicName = name;
	}
}
