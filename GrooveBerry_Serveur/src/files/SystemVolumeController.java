package files;

import java.io.IOException;

//TODO Gestion des erreurs, affichage sortie console
public class SystemVolumeController {
	private int volumePercentage;
	
	public SystemVolumeController() {
		this.volumePercentage = 50;
	}

	public int getVolumePercentage() {
		return volumePercentage;
	}
	
	public void setVolumePercentage(int volumePercentage) {
		this.volumePercentage = volumePercentage;
		if (this.volumePercentage < 0) {
			this.volumePercentage = 0;
		} 
		else if (this.volumePercentage > 100) {
			this.volumePercentage = 100;
			
		}
		try {
			 Runtime.getRuntime().exec("amixer sset 'Master' " + this.volumePercentage + "%");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void increaseVolume() {
		this.volumePercentage += (5 - (this.volumePercentage % 5));
		setVolumePercentage(this.volumePercentage);
	}
	
	public void decreaseVolume() {
		this.volumePercentage -= (5 - (this.volumePercentage % 5));
		setVolumePercentage(this.volumePercentage);
	}
	
	public static void main(String[] args) throws InterruptedException {
		SystemVolumeController sysVol = new SystemVolumeController();
		sysVol.setVolumePercentage(42);
		sysVol.increaseVolume();
		Thread.sleep(1000);
		sysVol.increaseVolume();
		Thread.sleep(1000);
		sysVol.increaseVolume();
		Thread.sleep(1000);
		sysVol.increaseVolume();
		Thread.sleep(1000);
		sysVol.decreaseVolume();
		Thread.sleep(1000);
		sysVol.decreaseVolume();
	}

}
