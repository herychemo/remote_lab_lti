package com.optimizedproductions.remote_lab_lti;

import com.optimizedproductions.remote_lab_lti.server.CameraServer;
import com.optimizedproductions.remote_lab_lti.server.MyWebSocketServer;
import com.optimizedproductions.remote_lab_lti.server.SerialServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 *
 * @author Heriberto Reyes Esparza
 */
@SpringBootApplication
public class Application {
	public static void main(String args[]){
		SpringApplication.run(Application.class, args);

		MyWebSocketServer.getInstance();
		SerialServer.getInstance();

		CameraServer.start_video_service_thread();
	}

	public static class Config{
		public static String PORT_NAME_SERIAL = "COM4";
	}
}
