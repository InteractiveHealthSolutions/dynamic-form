package org.ihs.form.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.ihs.form.utils.StringUtils;
import org.ird.unfepi.formmodule.context.FormModuleContext;
import org.ird.unfepi.formmodule.context.FormModuleServiceContext;
import org.ird.unfepi.formmodule.model.Field;
import org.ird.unfepi.formmodule.model.Form;
import org.ird.unfepi.formmodule.model.FormSubmission;
import org.ird.unfepi.formmodule.model.FormSubmissionField;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

@Controller
@RequestMapping("/fm/showformdata")
public class ShowFormDataController {

	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView showForm(@RequestParam("id") Integer id, HttpServletRequest request, HttpServletResponse response)
	{		
		FormModuleServiceContext sc = FormModuleContext.getServices();
		try
		{
			ModelAndView model = new ModelAndView("dvf_show_form_data");
			List<FormSubmission> formSubmission = sc.getFormSubmissionService().getFormSubmissionByFormId(id);
//			model.addObject("form",formSubmission);
			List<Field> fields = sc.getFieldService().getFieldsByFormId(id);
//			model.addObject("fields", fields);
			Map<String,Object> obj = new HashMap<String,Object>();			
			
			List<Object> listOfColumnsMap = new ArrayList<Object>();
			Map<String,String> tm = null;
			for(Field f : fields){
				tm = new HashMap<String,String>();
				tm.put("title", f.getFieldLabel()); 
				tm.put("data", f.getFieldName());
				listOfColumnsMap.add(tm);
			}
			tm = new HashMap<String,String>();
			tm.put("title", "Date Created"); 
			tm.put("data", "created_date");
			listOfColumnsMap.add(tm);
			obj.put("columns", listOfColumnsMap);
			
			List<Object> listOfValuesMap = new ArrayList<Object>();
			for(FormSubmission fs : formSubmission){
				tm = new HashMap<String,String>();
				for(FormSubmissionField fsf : fs.getListFields()){
					if(!StringUtils.isEmpty(fsf.getValue()))
						tm.put(fsf.getField().getFieldName(), fsf.getValue()); 
				}
				tm.put("created_date", fs.getCreatedDate() == null ? "" : fs.getCreatedDate().toString());
				if(tm.size() == listOfColumnsMap.size())
					listOfValuesMap.add(tm);
				
			}
			obj.put("columns", listOfColumnsMap);
			obj.put("data", listOfValuesMap);
			model.addObject("obj", new Gson().toJson(obj));
			model.addObject("form_name",sc.getFormService().getFormNameById(id));
			return model;
		}
		catch(Exception e ){
			e.printStackTrace();
			sc.rollbackTransaction();
			return null;
		}
		finally
		{
			sc.closeSession();
		}
		
	}
}
