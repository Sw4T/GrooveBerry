package network;

import static org.junit.Assert.assertEquals;

import java.net.Socket;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ConnectionServerApp {

	static Server server;
	static Client client;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		server = new Server();
		server.waitConnectionForTest();
		client = new Client(new Socket("localhost", Server.SERVER_PORT));
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		client = null;
		server = null;
	}

	@Test
	public void test_connection_ok() 
	{
		assertEquals(true, client.getSocket().isConnected());
		assertEquals(true, client.getSocket().isBound());
		/*assertEquals(true, server.getSocket().isConnected());
		assertEquals(true, server.getSocket().isBound());*/
	}
	
	/*@Test
	public void test_send_string_to_server() 
	{
		client.sendString("test");
		assertEquals(server.getStringFromRemote(), "test");
	}
	
	@Test
	public void test_send_several_strings_to_server() 
	{
		client.sendString("test");
		assertEquals(server.getStringFromRemote(), "test");
		client.sendString("test2");
		assertEquals(server.getStringFromRemote(), "test2");
	}
*/
}
