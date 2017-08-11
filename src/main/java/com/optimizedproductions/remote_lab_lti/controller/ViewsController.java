package com.optimizedproductions.remote_lab_lti.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Heriberto Reyes Esparza
 */
@Controller
public class ViewsController {

	//  Constants
	private static final String FILE_404 = "404.html";

	//  Variables
	private Integer c;

	@RequestMapping(value="/heartbeat", method= RequestMethod.GET)
	public String heartbeat2(){
		if( c == null )
			c = 0;
		return "counter: " + ( c++ );
	}

	@RequestMapping("/greeting")
	public String greeting(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
		model.addAttribute("name", name);
		return "greeting";
	}

}
