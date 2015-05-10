package network;

import static org.junit.Assert.assertEquals;

import java.net.Socket;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ModifyReadingQueue {

	static Server server;
	static Client client_app;
	static Client client_server;
	
	/**
     * Liste de lecture du serveur : 
			01 Clandestino.mp3
			02 - Defender of Beauty (feat Marcia Griffiths).mp3
			04 Hey Joe.mp3
			05 Mentira.mp3
			09 - Born Again.mp3
			09 Little Wing.mp3
			12 Bold as Love.mp3
			15 Voodoo Child (Slight Return).mp3
			16 Freedom.mp3
			9.wav
			aol.wav
			banane2.wav
			Bob Marley - Jammin.mp3
			conneriesd1formaticiens.wav
			free.wav
			tennisc.wav
			test.wav
	 */
	@BeforeClass
	public static void setUp() throws Exception {
		server = Server.getInstance();
		server.waitConnectionForTest();
		client_app = new Client(new Socket("localhost", Server.SERVER_PORT_SIMPLE));
		Thread.sleep(100);
		
		client_server = server.getCurrentClient();
		System.out.println("Initialisation effectuée...");
		new Thread(client_server).start(); //Thread chargé de la réception et du traitement des constantes reçues de l'application cliente
		Thread.sleep(100);
	}

	@AfterClass
	public static void tearDown() throws Exception {
		client_app = null;
		client_server = null;
		server = null;
	}
	
	@Test
	public void test_reading_queue() throws InterruptedException
	{
		assertEquals(true,client_app.sendSerializable("play"));
		Thread.sleep(100);
		assertEquals("01 Clandestino.mp3", Server.getInstance().getReadingQueue().getCurrentTrack().getName());
		assertEquals(true, Server.getInstance().getReadingQueue().getCurrentTrack().isPlaying());
		
		assertEquals(true,client_app.sendSerializable("next"));
		Thread.sleep(100);
		assertEquals("02 - Defender of Beauty (feat Marcia Griffiths).mp3", Server.getInstance().getReadingQueue().getCurrentTrack().getName());
	
		assertEquals(true,client_app.sendSerializable((Integer) 4));
		Thread.sleep(100);
		assertEquals("09 - Born Again.mp3", Server.getInstance().getReadingQueue().getCurrentTrack().getName());
		
		assertEquals(true,client_app.sendSerializable("prev"));
		Thread.sleep(100);
		assertEquals("05 Mentira.mp3", Server.getInstance().getReadingQueue().getCurrentTrack().getName());
		
		assertEquals(true,client_app.sendSerializable("pause"));
		Thread.sleep(100);
		assertEquals("05 Mentira.mp3", Server.getInstance().getReadingQueue().getCurrentTrack().getName());
		assertEquals(true, Server.getInstance().getReadingQueue().getCurrentTrack().isPaused());
		
		assertEquals(true,client_app.sendSerializable("loop"));
		Thread.sleep(100);
		assertEquals("05 Mentira.mp3", Server.getInstance().getReadingQueue().getCurrentTrack().getName());
		assertEquals(true, Server.getInstance().getReadingQueue().getCurrentTrack().isLooping());
	}
}