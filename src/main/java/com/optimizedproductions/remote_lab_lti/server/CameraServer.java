package com.optimizedproductions.remote_lab_lti.server;


import org.apache.commons.codec.binary.Base64;
import org.json.simple.JSONObject;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Heriberto Reyes Esparza
 */
public class CameraServer {

	static {
		System.out.println( System.getProperty("user.dir") );
		String direct_path = System.getProperty("user.dir") + File.separator + "libs" + File.separator + Core.NATIVE_LIBRARY_NAME + ".dll";
		System.out.println( Core.NATIVE_LIBRARY_NAME );
		System.out.println( direct_path );
		//System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		System.load( direct_path );
	}

	public static void print_version(){
		System.out.println("Welcome to OpenCV " + Core.VERSION);
	}

	public static void start_video_service_thread(){
		new Thread(CameraServer::start_video_service).start();
	}

	public static void start_video_service(){

		Mat webcamMatImage = new Mat();
		String img_encoded;
		VideoCapture capture = new VideoCapture(    0   );

		//int w = 240, h = 180;
		//int w = 320, h = 240;
		int w = 400, h = 320;

		MatOfInt jpeg_params = new MatOfInt(Imgcodecs.IMWRITE_JPEG_QUALITY, 90);

		capture.set(Videoio.CAP_PROP_FRAME_WIDTH    ,w);
		capture.set(Videoio.CAP_PROP_FRAME_HEIGHT   ,h);

		JSONObject data = new JSONObject();
		data.put("data", "");
		data.put("type",MyWebSocketServer.DATA_TYPE_IMG_UPDATE);
		if( capture.isOpened()){
			while (true){
				capture.read(webcamMatImage);
				if( !webcamMatImage.empty() ){
					Size sz = new Size(w,h);

					Imgproc.resize(webcamMatImage, webcamMatImage, sz);

					MatOfByte buffer = new MatOfByte();
					Imgcodecs.imencode(".jpg", webcamMatImage, buffer, jpeg_params);
					byte[] return_buff = buffer.toArray();

					StringBuilder sb = new StringBuilder();
					sb.append("data:image/jpeg;base64,");
					sb.append( Base64.encodeBase64String(return_buff)  );
					img_encoded = sb.toString();
					//System.out.print( "." );
					data.put("data", img_encoded);
					if(
							MyWebSocketServer.getInstance().sendToAll( data )
							){
						//
					}
					try {
						//Thread.sleep(34);
						//Thread.sleep(12);
					} catch (Exception ex) {
						Logger.getLogger(CameraServer.class.getName()).log(Level.SEVERE, null, ex);
					}
				}
				else{
					System.out.println(" -- Frame not captured -- Break!");
					break;
				}
			}
		}
		else{
			System.out.println("Couldn't open capture.");
		}
	}

}
