package org.ihs.form.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.InstanceAlreadyExistsException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.exception.GenericJDBCException;
import org.ird.unfepi.formmodule.context.FormModuleContext;
import org.ird.unfepi.formmodule.context.FormModuleServiceContext;
import org.ird.unfepi.formmodule.model.Form;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/listforms")
public class ListDynamicFormsController{
	
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView listDynamicForms(HttpServletRequest request, HttpServletResponse response) throws InstanceAlreadyExistsException
	{
		ModelAndView model = new ModelAndView();
//		FormModuleServiceContext sc = FormModuleContext.getServices();
		FormModuleServiceContext sc = null;
		try
		{
			sc = FormModuleContext.getServices();
			List<Form> forms = sc.getFormService().getAllFormIdAndName();
			model.addObject("forms", forms);
			model.setViewName("dvf_forms_list");
			
			return model;
		}catch(GenericJDBCException e){
			e.printStackTrace();
			request.getSession().setAttribute("exceptionTrace", e);
			return new ModelAndView("exception");
		}
		finally
		{
			if(sc != null)
				sc.closeSession();
		}
		
	}
}
