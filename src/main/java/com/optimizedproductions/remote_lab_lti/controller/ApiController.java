package com.optimizedproductions.remote_lab_lti.controller;

import com.optimizedproductions.remote_lab_lti.helpers.GlobalHelper;
import org.imsglobal.lti.launch.LtiOauthVerifier;
import org.imsglobal.lti.launch.LtiVerificationException;
import org.imsglobal.lti.launch.LtiVerificationResult;
import org.imsglobal.lti.launch.LtiVerifier;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Heriberto Reyes Esparza
 *
 *
 *  Do not forget to generate a XML Config file
 *
 *  https://www.edu-apps.org/build_xml.html
 * eg. see in static resources

 */
@RestController
public class ApiController {

	private static final String NONE_VALUE = "None";
	private static final String PRIVATE_FILE_TEMPLATE = "private/%1$s.html";

	private static final String KEY = "843ad69b5ed55c307bd5e8013e495f64";
	private static final String SECRET = "91f3e802d1b8fab4d41e519657680626";


	@RequestMapping(value = "/entryPoint", method = RequestMethod.POST)
	public String entryPoint(HttpServletRequest request){

		//  Verify Request
		LtiVerifier ltiVerifier = new LtiOauthVerifier();
		LtiVerificationResult ltiResult;
		try {
			ltiResult = ltiVerifier.verify(request, SECRET);
		} catch (LtiVerificationException ex) {
			Logger.getLogger(ApiController.class.getName()).log(Level.SEVERE, null, ex);
			System.err.println("A problem occurred verifying a request.");
			System.err.println( ex.getMessage() );
			return "Access Denied";
		}
		if( ! ltiResult.getSuccess() ){
			System.err.println("Bad Oauth Request");
			System.err.println(ltiResult.getMessage());
			System.err.println(ltiResult.getError().toString());
			return ltiResult.getMessage();
		}

		//  Get Incoming Parameters
		Enumeration<String> keys = request.getParameterNames();
		JSONObject json = new JSONObject();
		while( keys.hasMoreElements() ){
			String next = keys.nextElement();
			String val = request.getParameter( next );
			json.put(next, val);
		}

		//  Check If Request Has The Basic Lti Info And It Is Correct.
		if(
				(json.getOrDefault("lti_message_type", NONE_VALUE)).equals( "basic-lti-launch-request" )
						&&
						json.getOrDefault("lti_version", NONE_VALUE).equals( "LTI-1p0" )
				){
			//  Filling Params
			System.out.println(  json.toJSONString()  );
			JSONObject args = new JSONObject();

			args.put("student_full_name", json.get("lis_person_name_full"));
			args.put("activity_name",     ((String)json.get("custom_activity")).replace("_"," ")  );
			args.put("go_back_url", json.get("launch_presentation_return_url"));

			return GlobalHelper.render_file(
					String.format(PRIVATE_FILE_TEMPLATE,
							json.getOrDefault("custom_activity", "123")
					),  args );
		}
		return "There was a problem with your LTI Basic Data";
	}

}
