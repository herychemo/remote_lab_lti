package com.optimizedproductions.remote_lab_lti.server;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.simple.JSONObject;

import java.net.InetSocketAddress;
import java.util.Collection;

/**
 *
 * @author Heriberto Reyes Esparza
 */
public class MyWebSocketServer extends WebSocketServer {

	public static final String WS_SERVER_INTERFACE = "0.0.0.0";
	public static final int WS_SERVER_PORT = 9992;

	public static final String DATA_TYPE_IMG_UPDATE = "img_update";
	public static final String DATA_TYPE_MSG = "msg";
	public static final String DATA_TYPE_ECHO = "echo";

	private static MyWebSocketServer MyWebSocketServer;

	private MyWebSocketServer(InetSocketAddress address ) {
		super( address );
	}

	public static MyWebSocketServer getInstance(){
		if( MyWebSocketServer == null ){
			MyWebSocketServer = new MyWebSocketServer( new InetSocketAddress( WS_SERVER_INTERFACE , WS_SERVER_PORT ) );
			MyWebSocketServer.start();
		}
		return MyWebSocketServer;
	}


	@Override
	public void onOpen(WebSocket ws, ClientHandshake ch) {
		System.out.println("OPEN");
		System.out.println(ws.getLocalSocketAddress());
		//ws.send("welcome");
	}

	@Override
	public void onClose(WebSocket ws, int i, String string, boolean bln) {
		System.out.println("CLOSE");
		System.out.println(ws.getLocalSocketAddress());
	}

	@Override
	public void onMessage(WebSocket ws, String string) {
		System.out.println("From: " + ws.getLocalSocketAddress());
		System.out.println("Received: " + string);
		System.err.println("Sending to All.");
		sendToAll(string, DATA_TYPE_ECHO);
		// Do Something
	}

	@Override
	public void onError(WebSocket ws, Exception excptn) {
		System.err.println("WebSocket ERROR, LOOK AT THIS !!");
		excptn.printStackTrace();
		System.err.println("WebSocket ERROR, END ");
	}

	/**
	 * Sends <var>text</var> to all currently connected WebSocket clients.
	 *
	 * @param text
	 *            The String to send across the network.
	 * @throws InterruptedException
	 *             When socket related I/O errors occur.
	 */
	public boolean sendToAll( String text, String type ) {   // It returns true, if you sent your message to at least one client.
		boolean success = false;
		if ( text.isEmpty() )
			return success;

		JSONObject data = new JSONObject();
		data.put("type", type);
		data.put("data", text );
		return sendToAll(data);
	}
	public boolean sendToAll(String text){
		return sendToAll(text, DATA_TYPE_MSG );
	}
	public boolean sendToAll(JSONObject data){
		boolean success = false;
		String text = data.toString();
		Collection<WebSocket> con = connections(); // Super Util
		synchronized ( con ) {
			for( WebSocket c : con ) {
				c.send( text );
				success = true;
			}
		}
		return success;
	}

}
