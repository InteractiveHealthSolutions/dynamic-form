package org.ihs.form.controller;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.ihs.form.utils.FieldSetter;
import org.ird.unfepi.formmodule.context.FormModuleContext;
import org.ird.unfepi.formmodule.context.FormModuleServiceContext;
import org.ird.unfepi.formmodule.model.Field;
import org.ird.unfepi.formmodule.model.Form;
import org.ird.unfepi.formmodule.model.FormSubmission;
import org.ird.unfepi.formmodule.model.FormSubmissionField;
import org.ird.unfepi.formmodule.utils.HtmlParser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class EditFormDataController {

	@RequestMapping(value="/editFormData", method=RequestMethod.GET)
	public ModelAndView editFormData(@RequestParam("fsid") Integer id){
		System.out.print(id);
		FormModuleServiceContext sc = FormModuleContext.getServices();
		try{
			FormSubmission ls =  sc.getFormSubmissionService().getFormSubmissionById(id);
			Form form = sc.getFormService().getFormById(ls.getForm().getId());

			HtmlParser parser = new HtmlParser();
			Document doc = parser.parseHtml(form.getProcessedHtml());
			for(FormSubmissionField fsf: ls.getListFields()){
				FieldSetter.valueOf(fsf.getField().getFieldType().getName()).read(doc,ls,fsf);			
			}
			ModelAndView model = new ModelAndView("dvf_show_form");
			form.setProcessedHtml(doc.toString());
			model.addObject("form",form);
			model.addObject("fs_id",ls.getId());
			return model;
			}
		catch(Exception e){
			e.printStackTrace();
		}finally{
			sc.closeSession();
		}
		return null;	
	}
	
	@RequestMapping(value="/editFormData", method=RequestMethod.POST)
	public ModelAndView method(HttpServletRequest req){
		Integer fsId = Integer.parseInt(req.getParameter("fsid"));
		FormModuleServiceContext sc = null;
		try{
			sc = FormModuleContext.getServices();
			FormSubmission fs = sc.getFormSubmissionService().getFormSubmissionById(fsId);
			
			
			Map paramMap = req.getParameterMap();
			Set<FormSubmissionField> fsfList = fs.getListFields();
			
			/**
			 * Delete all previous FormSubmissionFields
			 * and add new 
			 */
			for(FormSubmissionField fsf : fsfList){
				sc.getSession().delete(fsf);
			}			
			for(Field f : fs.getForm().getFieldsList()){
				if(f.getFieldType()!=null){
					f.getDynamicForm().add(fs.getForm());
					Integer fieldId = f.getId();
				
					FormSubmissionField formSubField = new FormSubmissionField();
					formSubField.setField((Field)sc.getSession().load(Field.class, fieldId));
					formSubField.setFormSubmission(fs);
					String[] values = req.getParameterValues(f.getFieldName());
					try{
						if(values.length > 1){
							StringBuilder value= new StringBuilder();
							for(String s : values){
								if(!StringUtils.isEmpty(s))
									value.append(s).append(",");
							}
							try{
								value.deleteCharAt(value.length()-1);
							}catch(StringIndexOutOfBoundsException e){}
							
							if(formSubField.getField().getFieldType().getName().equalsIgnoreCase("textarea")){
								formSubField.setValueTextarea(value.toString());
							}
							else
								formSubField.setValue(value.toString());
						}
						else{
							if(formSubField.getField().getFieldType().getName().equalsIgnoreCase("textarea")){
								formSubField.setValueTextarea(values[0]);
							}
							else
								formSubField.setValue(values[0]);
						} 
					}catch(NullPointerException e){
						formSubField.setValue(null);
					}
					sc.getSession().save(formSubField);
				}
			}
		    sc.commitTransaction();
		    
		    ModelAndView model = new ModelAndView("redirect:showformdata?id="+req.getParameter("id"));
			return model;
		}catch(Exception e){
			sc.rollbackTransaction();
			e.printStackTrace();
			req.getSession().setAttribute("exceptionTrace", e);
			return new ModelAndView("exception");
		}finally{
			sc.closeSession();
		}
	}
}