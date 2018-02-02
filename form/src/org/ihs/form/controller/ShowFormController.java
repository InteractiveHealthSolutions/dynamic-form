package org.ihs.form.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.sun.xml.internal.fastinfoset.UnparsedEntity;

@Controller
@RequestMapping("/showform")
@SessionAttributes("form_start_date")
public class ShowFormController{

	ShowFormController() {
	
	}

	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView showForm(@RequestParam("name") String name, HttpServletRequest request, HttpServletResponse response)
	{		
		FormModuleServiceContext sc = FormModuleContext.getServices();
		try
		{
			ModelAndView model = new ModelAndView("dvf_show_form");
			Form form = sc.getFormService().getFormByName(name);
			model.addObject("form",form);
			model.addObject("form_start_date",new Date());
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
	
	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView SubmitForm(HttpServletRequest request, ModelAndView model){
		FormModuleServiceContext fmsc = FormModuleContext.getServices();
		Form form = fmsc.getFormService().getFormById(Integer.parseInt(request.getParameter("id")));
		try {		
			FormSubmission formSubmission = new FormSubmission();
			formSubmission.setForm(form);
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			setFormMetaData(request,formSubmission);
			int formSubId = (Integer)fmsc.getSession().save(formSubmission);
			
			for(Field f : form.getFieldsList()){
				if(f.getFieldType()!=null){
					f.getDynamicForm().add(form);
					Integer fieldId = f.getId();
				
					FormSubmissionField formSubField = new FormSubmissionField();
					formSubField.setField((Field)fmsc.getSession().load(Field.class, fieldId));
					formSubField.setFormSubmission(formSubmission);
					String[] values = request.getParameterValues(f.getFieldName());
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
					fmsc.getSession().save(formSubField);
				}
			}
			fmsc.commitTransaction();
			return new ModelAndView("redirect:listforms.htm");
		
		}catch(ParseException e){
			fmsc.rollbackTransaction();
			e.printStackTrace();
			request.getSession().setAttribute("exceptionTrace", e);
			return new ModelAndView("exception");
		} 
		catch (Exception e) {
			fmsc.rollbackTransaction();
			e.printStackTrace();
			request.getSession().setAttribute("exceptionTrace", e);
			return new ModelAndView("exception");
		
		} finally {
			fmsc.closeSession();
		}	
		
	}

	private void setFormMetaData(HttpServletRequest request, FormSubmission formSubmission) throws ParseException {
		// TODO Auto-generated method stub
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		formSubmission.setCreatedDate(dateFormat.parse(dateFormat.format(new Date())));
		formSubmission.setStartDate((Date)request.getSession().getAttribute("form_start_date"));
		formSubmission.setEndDate(new Date());
		try{
			formSubmission.setDateEntryDateTime(df.parse(request.getParameter("encounterDatetime")));
		}catch(ParseException pe){
			formSubmission.setDateEntryDateTime(null);
		}
		try{
			formSubmission.setLocationId(Integer.parseInt(request.getParameter("locationId")));
		}catch(Exception e){
			e.printStackTrace();
			formSubmission.setLocationId(null);
		}
	}
}
