package network;

import static org.junit.Assert.assertEquals;

import java.net.Socket;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ConnectionServerApp {

	static Server server;
	static Socket socket;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		server = new Server();
		server.waitConnectionForTest();
		socket = new Socket("localhost", Server.SERVER_PORT);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		socket = null;
		server = null;
	}

	@Test
	public void test_connection_ok() 
	{
		assertEquals(true, socket.isConnected());
		assertEquals(true, socket.isBound());
		if (server.getCurrentClient() == null)
			System.out.println("current client null");
		assertEquals(true, server.getCurrentClient().getSocket().isConnected());
		assertEquals(true, server.getCurrentClient().getSocket().isBound());
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
