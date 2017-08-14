package com.optimizedproductions.remote_lab_lti.server;


import com.github.sarxos.webcam.Webcam;
import org.json.simple.JSONObject;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author Heriberto Reyes Esparza
 */
public class CameraServer {

	public static void start_video_service_thread(){
		new Thread(() -> {
			CameraServer.start_video_service();
		}).start();
	}

	public static void start_video_service(){
		Webcam webcam = Webcam.getDefault();
		try {
			List<Webcam> my_cams = Webcam.getWebcams(1000);
			for ( Webcam cam : my_cams ){
				System.out.println("MyCams Starts");
				System.out.println(">> " + cam.getName());
				System.out.println("MyCams Ends");
				if( cam.getName().contains("C170") )
					webcam = cam;
			}
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
		String img_encoded;

		try {
			webcam.open();

			JSONObject data = new JSONObject();
			data.put("data", "");
			data.put("type",MyWebSocketServer.DATA_TYPE_IMG_UPDATE);

			while(webcam.isOpen() ){
				BufferedImage im = webcam.getImage();

				//

				ByteArrayOutputStream out = new ByteArrayOutputStream();
				ImageIO.write(im, "PNG", out);
				String img_base64 = DatatypeConverter.printBase64Binary(out.toByteArray());


				StringBuilder sb = new StringBuilder();
				sb.append("data:image/jpeg;base64,");
				sb.append( img_base64 );
				img_encoded = sb.toString();
				//System.out.print( "." );
				data.put("data", img_encoded);
				if(
						MyWebSocketServer.getInstance().sendToAll( data )
						){
					//
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}


	}

}
