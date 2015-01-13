package files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

public class FichierAudio {
	
	public static void lireMP3(File ficMP3) 
	{
		try {
			FileInputStream in = new FileInputStream(ficMP3);
			AdvancedPlayer player = new AdvancedPlayer(in);
		    player.play(); //Lecture du fichier mp3
		} catch (FileNotFoundException e) { e.printStackTrace();
		} catch (JavaLayerException e) { e.printStackTrace(); }
	}
	
	public static void lireWAV(File ficWAV) 
	{
		try {
			AudioInputStream stream = AudioSystem.getAudioInputStream(ficWAV);
			AudioFormat format = stream.getFormat();
		    DataLine.Info info = new DataLine.Info(Clip.class, format);
		    Clip clip = (Clip) AudioSystem.getLine(info);
		    clip.open(stream);
		    clip.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main (String [] args) throws JavaLayerException {
		File mp3 = new File("audio/Bob Marley - Jammin.mp3");
		File wav = new File("audio/test.wav");
		System.out.println(new File(".").getAbsolutePath());
		if (!mp3.exists() || !wav.exists()) {
			System.out.println("Un des fichiers de test n'a pas pu être récupéré, arrêt...");
		} else {	
	        Scanner scanIn = new Scanner(System.in);
	        String input;
	        do {
	        	System.out.println("Choisir quel type de format jouer ['mp3' / 'wav']\n'stop' pour quitter : ");
	        	input = scanIn.nextLine();
	        	if (input.equals("mp3"))
	        		lireMP3(mp3);
	        	else if (input.equals("wav"))
	        		lireWAV(wav);
	        } while (!input.equals("stop"));
	        scanIn.close();
		}
	}
	

}
