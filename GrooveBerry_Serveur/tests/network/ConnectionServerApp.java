package network;

import static org.junit.Assert.assertEquals;

import java.net.Socket;
import java.util.ArrayList;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ConnectionServerApp {

	static Server server;
	static Client client_app;
	static Client client_server;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		server = Server.getInstance();
		server.initReadingQueue();
		server.waitConnectionForTest();
		client_app = new Client(new Socket("localhost", Server.SERVER_PORT_SIMPLE));
		Thread.sleep(100);
		client_server = server.getCurrentClient();
		System.out.println("Initialisation effectuée...");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		client_app = null;
		client_server = null;
		server = null;
	}

	@Test
	public void test_connection_ok()
	{
		assertEquals(true, client_app.getSocketSimple().isConnected());
		assertEquals(true, client_app.getSocketSimple().isBound());
		assertEquals(true, client_server.getSocketSimple().isConnected());
		assertEquals(true, client_server.getSocketSimple().isBound());
	}
	
	@Test
	public void test_send_string_to_server() 
	{
		assertEquals(client_app.sendString("test"), true);
		assertEquals(client_server.readString(), "test");
	}
	
	@Test
	public void test_send_several_strings_to_server() 
	{
		assertEquals(client_app.sendString("test"), true);
		assertEquals(client_server.readString(), "test");
		assertEquals(client_app.sendString("test2"), true);
		assertEquals(client_server.readString(), "test2");
		assertEquals(client_app.sendString("test3"), true);
		assertEquals(client_server.readString(), "test3");
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void test_send_serializable_to_server() 
	{
		ArrayList<String> send = new ArrayList<String>();
		send.add("Mr Leblanc rocks");
		send.add("And Patrice Petit too");
		assertEquals(client_app.sendSerializable(send), true);
		ArrayList<String> received = (ArrayList<String>) client_server.readSerializable();
		assertEquals(received, send); //Vérification de la similarité des tableaux
		assertEquals(received.size(), send.size()); //Vérification de la taille
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void test_send_several_serializable_to_server() 
	{
		ArrayList<String> send = new ArrayList<String>();
		send.add("Mr Leblanc rocks");
		send.add("And Patrice Petit too");
		assertEquals(client_app.sendSerializable(send), true); //Envoi effectuÃ© sans erreur
		ArrayList<String> received = (ArrayList<String>) client_server.readSerializable();
		assertEquals(received, send); //Vérification de la similarité des tableaux
		assertEquals(received.size(), send.size()); //Vérification de la taille
		
		send.add("1");
		send.add("2");
		send.add("3");
		assertEquals(client_app.sendSerializable(send), true);
		received = (ArrayList<String>) client_server.readSerializable();
		assertEquals(received, send); //Vérification de la similaritÃ© des tableaux
		assertEquals(received.size(), send.size()); //Vérification de la taille
	}

}
