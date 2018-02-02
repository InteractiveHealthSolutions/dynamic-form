package org.ihs.form.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/editfield")
public class EditFieldController {

	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView editField(){
		ModelAndView model = new ModelAndView("edit_field");
		return model;
	}
}
