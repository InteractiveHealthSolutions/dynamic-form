package org.ihs.form.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Hibernate;
import org.ihs.form.utils.HtmlParser;
import org.ihs.form.utils.StringUtils;
import org.ihs.form.validator.DynamicFormValidator;
import org.ird.unfepi.formmodule.context.FormModuleContext;
import org.ird.unfepi.formmodule.context.FormModuleServiceContext;
import org.ird.unfepi.formmodule.model.Field;
import org.ird.unfepi.formmodule.model.FieldListOptions;
import org.ird.unfepi.formmodule.model.FieldType;
import org.ird.unfepi.formmodule.model.Form;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/editform")
@SessionAttributes({"command","origFormName"})
public class EditFormController {
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView editForm(HttpServletRequest request){
		ModelAndView model = new ModelAndView("denf_dynamic_form");
		Form f = formBackingObject(request);
		model.addObject("command", f);
		model.addObject("origFormName",f.getFormName());
		return model;
	}

	private Form formBackingObject(HttpServletRequest request) {
		Integer formId = Integer.parseInt(request.getParameter("formid"));
		FormModuleServiceContext sc = FormModuleContext.getServices();
		Form form;
		try{
			form = sc.getFormService().getFormById(formId);
//			Hibernate.initialize(form.getFieldsList());
//			form.getFieldsList();
			for(Field field : form.getFieldsList()){
				System.out.println(field.getFieldName());
				Hibernate.initialize(field.getFieldType());
				
			}
			List<Integer> ffIds = new ArrayList<Integer>();
			for(Field f : form.getFieldsList()){
				ffIds.add(f.getId());
				if(f.getFieldListOptions() != null && f.getFieldListOptions().size() > 0){
					f.setFieldOptionsCommaDelimited(StringUtils.listToCommaSeparatedString(f.getFieldListOptions()));
				}
			}
			request.getSession().setAttribute("formFieldsIds", ffIds);
			sc.commitTransaction();
		}catch(Exception e){
			sc.rollbackTransaction();
			return null;
		}finally{
			sc.closeSession();
		}
		return form;
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView onSubmit(@ModelAttribute("command")Form dynamicForm, BindingResult results,
								 HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView,
								 HttpSession session) throws Exception {
		
		List<Integer> deletedFormFields = new ArrayList<Integer>();
	
		removeDeletedFieldsInForm(dynamicForm,deletedFormFields);
		request.getSession().setAttribute("deletedFormFields", deletedFormFields);

		dynamicForm.setProcessedHtml(null);
		Document doc = new DynamicFormValidator().validateForm(dynamicForm, results,(String)request.getSession().getAttribute("origFormName"));
		if(results.hasErrors()){
			modelAndView.setViewName("denf_dynamic_form");
			return modelAndView;	
		}
		
		else{
			for(Field f : dynamicForm.getFieldsList())
			{
				if(f.getFieldType().getId() == null)
					dynamicForm.getFieldsList().remove(f);
			}
			
			String action = request.getParameter("action");
			
			HtmlParser.parseFields(doc,dynamicForm);
			doc.getElementsByTag("script").remove();
			dynamicForm.setProcessedHtml(doc.html());
			
			if(action.equals("preview")){
				modelAndView.addObject("command",dynamicForm);
				modelAndView.setViewName("denf_dynamic_form");
				return modelAndView;	
			}
			
			else if(action.equalsIgnoreCase("submit") || action.equalsIgnoreCase("submitFormOnly")){			
				FormModuleServiceContext fmsc = null;		
				try{
					fmsc = FormModuleContext.getServices();
					checkAndUpdateDeletedFields(dynamicForm.getId(), fmsc, request);	
			//		LoggedInUser user=UserSessionUtils.getActiveUser(request);
					
					updateFormAndFields(fmsc, dynamicForm);
					return new ModelAndView("redirect:listforms.htm");
				
				} catch (Exception e) {				
					fmsc.rollbackTransaction();
					e.printStackTrace();
					request.getSession().setAttribute("exceptionTrace", e);
					return new ModelAndView("exception");
				
				} finally {
					fmsc.closeSession();
				}		
			}
		}
		
		return null;
	}

	@ModelAttribute
	public void addAttributes(Model model){
		FormModuleServiceContext fmsc = FormModuleContext.getServices();
		try{
		List<FieldType> fieldTypes = fmsc.getFieldTypeService().getAll();
		model.addAttribute("fieldTypes", fieldTypes);
		}catch(Exception e ){
			fmsc.rollbackTransaction();
			e.printStackTrace();
		}finally{
			fmsc.closeSession();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void checkAndUpdateDeletedFields(Integer formId, FormModuleServiceContext sc, HttpServletRequest request) throws Exception{
		for(Integer deletedId : (ArrayList<Integer>) request.getSession().getAttribute("deletedFormFields")){
			for(Integer id: (ArrayList<Integer>) request.getSession().getAttribute("formFieldsIds")){
				if(deletedId == id){
						String sql = "delete from form_fields where form_id = "+formId+" AND field_id = " + id;
						sc.getSession().createSQLQuery(sql).executeUpdate();
				}
			}
		}
	}
	
	private void deletePreviousFieldOptions(Field f, FormModuleServiceContext sc){
		String sql = "delete from field_list_options where field_id = " + f.getId();
		sc.getSession().createSQLQuery(sql).executeUpdate();		
	}
	
	private void removeDeletedFieldsInForm(Form dynamicForm, List<Integer> deletedFormFields){
		for (Iterator<Field> iterator = dynamicForm.getFieldsList().iterator(); iterator.hasNext(); ) {
		    Field f = iterator.next();
		    if(f.getFieldLabel().equalsIgnoreCase("deleted")){
				iterator.remove();
				deletedFormFields.add(f.getId());
		    }
		}
	}
	
	private void updateFormAndFields(FormModuleServiceContext fmsc, Form dynamicForm) throws Exception{
		// UPDATE FORM 
		fmsc.getFormService().updateForm(dynamicForm);
		Form df = (Form) fmsc.getSession().load(Form.class, dynamicForm.getId());

		for(Field f : dynamicForm.getFieldsList()){
			if(f.getFieldType()!=null){
				Integer fieldId = null;
				/*Changing it to >0 because now default value for fieldIdHidden field in html is zero*/
				if(f.getId() > 0){
					fmsc.getSession().update(f);
					fieldId = f.getId();
				}
				else{
					fieldId = (Integer)fmsc.getFieldService().saveField(f);
				}
				
				//new added - start
				if(f.getFieldType().getName().equalsIgnoreCase("select") || f.getFieldType().getName().equalsIgnoreCase("checkbox") || f.getFieldType().getName().equalsIgnoreCase("radio")){
					
					deletePreviousFieldOptions(f,fmsc);
					FieldListOptions flo = null;
					if(f.getModelOrList() != null && f.getModelOrList().equalsIgnoreCase("list")){
						for(String s : f.getFieldOptionsCommaDelimited().split(",")){
							flo = new FieldListOptions();
							flo.setField((Field)fmsc.getSession().load(Field.class, fieldId));
							flo.setFieldOption(s);
							fmsc.getSession().save(flo);
						}
					}
				}
			}
		}
		
		fmsc.commitTransaction();
	}
}
