package com.optimizedproductions.remote_lab_lti.helper;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	public static JSONObject get_incoming_params(HttpServletRequest request){
		//  Get Incoming Parameters
		Enumeration<String> keys = request.getParameterNames();
		JSONObject json = new JSONObject();
		while( keys.hasMoreElements() ){
			String next = keys.nextElement();
			String val = request.getParameter( next );
			json.put(next, val);
		}
		return json;
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

	public static String render_args(String template, final JSONObject args){
		String my_regex = "\\$\\{\\{(.*?)\\}\\}";
		Pattern p = Pattern.compile( my_regex );
		while (true){
			Matcher m = p.matcher(template);
			if( m.find() ){
				String key = m.group(1).trim();
				template = m.replaceFirst((String) args.getOrDefault(key, key));
			}else
				break;
		}
		return template;
	}

}
