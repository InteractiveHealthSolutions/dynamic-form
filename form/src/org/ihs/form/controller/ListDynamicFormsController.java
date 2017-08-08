package org.ihs.form.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.InstanceAlreadyExistsException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.formmodule.context.FormModuleContext;
import org.ird.unfepi.formmodule.context.FormModuleServiceContext;
import org.ird.unfepi.formmodule.model.Form;
import org.ird.unfepi.model.DynamicForm;
import org.ird.unfepi.model.Women;
import org.ird.unfepi.model.WomenVaccination;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/fm/listforms")
public class ListDynamicFormsController{

	public ListDynamicFormsController() {
//		super("dataForm", new  DataViewForm("forms_list", "Forms List", SystemPermissions.VIEW_CHILDREN_DATA, false));
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView listDynamicForms(HttpServletRequest request, HttpServletResponse response) throws InstanceAlreadyExistsException
	{
		ModelAndView model = new ModelAndView();
		FormModuleServiceContext sc = FormModuleContext.getServices();
		try
		{
			FormModuleContext.instantiate(null);
			List<Form> forms = sc.getFormService().getAllFormIdAndName();
			model.addObject("forms", forms);
			model.setViewName("dvf_forms_list");
			return model;
		}
		finally
		{
			sc.closeSession();
		}
		
	}
}
