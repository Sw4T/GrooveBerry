package network;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class ConnectionServerApp {

	static Server server;
	static ClientTest client;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		server = new Server();
		server.waitConnectionForTest();
		client = new ClientTest();
	}

	@After
	public void tearDownAfterClass() throws Exception {
		client = null;
	}

	@Test
	public void test_connection_ok() 
	{
		client.connectionServer();
		assertEquals(true, client.getSocket().isConnected());
		assertEquals(true, client.getSocket().isBound());
		assertEquals(true, server.getSocket().isConnected());
		assertEquals(true, server.getSocket().isBound());
	}

}
