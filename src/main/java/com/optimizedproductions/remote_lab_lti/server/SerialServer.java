package com.optimizedproductions.remote_lab_lti.server;

import serialHelper.SerialAdmin;
import serialHelper.events.LineAvailableListener;

import static com.optimizedproductions.remote_lab_lti.Application.Config.PORT_NAME_SERIAL;

public class SerialServer {

	private static SerialServer serial_server;
	private static LineAvailableListener listener;

	public static void set_line_listener(LineAvailableListener l){
		listener = l;
	}

	private SerialServer(){}
	public static SerialServer getInstance(){
		if( serial_server == null)
			init_server();
		return serial_server;
	}

	private static boolean init_server() {
		System.out.print("STARTING");
		serial_server = new SerialServer();
		serial_server.serial = new SerialAdmin();
		if( !serial_server.serial.begin( PORT_NAME_SERIAL, 9600, aux_listener) ){
			System.err.println("CANNOT OPEN " + PORT_NAME_SERIAL);
			return false;
		}
		System.out.println("OK, Serial PORT Open.!");
		return true;
	}

	private static final LineAvailableListener aux_listener = new LineAvailableListener() {
		@Override
		public void onLineAvailable(String s) {
			if (s.trim().isEmpty())
				return;
			System.out.println("Internal Received: " + s);
			if (listener != null)
				listener.onLineAvailable(s);
		}
	};

	public SerialAdmin serial;
}
