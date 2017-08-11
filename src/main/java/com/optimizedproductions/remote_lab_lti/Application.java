package com.optimizedproductions.remote_lab_lti;

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
		SerialServer.getInstance();
	}

	public static class Config{
		public static String PORT_NAME_SERIAL = "COM3";
	}
}
