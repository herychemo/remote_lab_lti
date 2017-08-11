package com.optimizedproductions.remote_lab_lti.helpers;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GlobalHelper {

	public static String url_encode(String txt) {
		if( txt.isEmpty() )
			return txt;
		try {
			return URLEncoder.encode(txt,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String render_file(final String path, final JSONObject args){
		ClassLoader cl = GlobalHelper.class.getClassLoader();
		InputStream in = cl.getResourceAsStream(path);
		if (in == null)
			in = cl.getResourceAsStream("static/404.html");
		try {
			String text = new String(IOUtils.toByteArray(in));
			return render_args(
					new String(
							text.getBytes("ISO-8859-1"),
							"utf-8" ),
					args
			);
		} catch (IOException e) {
			Logger.getLogger(GlobalHelper.class.getName()).log(Level.SEVERE, e.getMessage(),e);
		}
		return path;
	}

	public static String render_args(final String template, final JSONObject args){
		return template;
	}

}
