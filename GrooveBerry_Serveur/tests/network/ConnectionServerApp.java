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
		server = new Server();
		server.waitConnectionForTest();
		client_app = new Client(new Socket("localhost", Server.SERVER_PORT));
		client_server = server.getCurrentClient();
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
		assertEquals(true, client_app.getSocket().isConnected());
		assertEquals(true, client_app.getSocket().isBound());
		assertEquals(true, client_server.getSocket().isConnected());
		assertEquals(true, client_server.getSocket().isBound());
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
		ArrayList<String> test = new ArrayList<String>();
		test.add("jm pas");
		test.add("les tests.");
		assertEquals(client_app.sendSerializable(test), true);
		ArrayList<String> received = (ArrayList<String>) client_server.readSerializable();
		assertEquals(received, test); //Vérification de la similarité des tableaux
		assertEquals(received.size(), test.size()); //Vérification de la taille
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void test_send_several_serializable_to_server() 
	{
		ArrayList<String> test = new ArrayList<String>();
		test.add("jm pas");
		test.add("les tests.");
		assertEquals(client_app.sendSerializable(test), true); //Envoi effectué sans erreur
		ArrayList<String> received = (ArrayList<String>) client_server.readSerializable();
		assertEquals(received, test); //Vérification de la similarité des tableaux
		assertEquals(received.size(), test.size()); //Vérification de la taille
		
		test.add("pour de");
		test.add("vrai");
		assertEquals(client_app.sendSerializable(test), true);
		received = (ArrayList<String>) client_server.readSerializable();
		assertEquals(received, test); //Vérification de la similarité des tableaux
		assertEquals(received.size(), test.size()); //Vérification de la taille
	}

}
