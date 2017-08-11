package com.optimizedproductions.remote_lab_lti.helpers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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
}
