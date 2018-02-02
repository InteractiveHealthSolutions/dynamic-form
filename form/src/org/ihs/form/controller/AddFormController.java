package org.ihs.form.controller;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ihs.form.utils.FormUtils;
import org.ihs.form.utils.HtmlParser;
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
@RequestMapping("/addform")
@SessionAttributes("command")
public class AddFormController{
	
	@ModelAttribute
	public void addAttributes(Model model){
		FormModuleServiceContext fmsc = null;
		try{
		fmsc = FormModuleContext.getServices();
		List<FieldType> fieldTypes = fmsc.getFieldTypeService().getAll();
		model.addAttribute("fieldTypes", fieldTypes);
		}catch(Exception e ){
			e.printStackTrace();
			fmsc.rollbackTransaction();
		}finally{
			fmsc.closeSession();
		}
	}
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView addChildFormView(){
		Form df = new Form();
		ModelAndView model = new ModelAndView("denf_dynamic_form");
		model.addObject("command", new Form());
		return model;	
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView onSubmit(@ModelAttribute("command")Form dynamicForm, BindingResult results,
								 HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView,
								 HttpSession session)  {		
		removeDeletedFieldsFromForm(dynamicForm);		
		dynamicForm.setProcessedHtml(null);
		Document doc = null;
		try{
			doc = new DynamicFormValidator().validateForm(dynamicForm, results, ".");
		}catch(Exception e){
			modelAndView.setViewName("denf_dynamic_form");
			return modelAndView;
		}
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
//			FormModuleServiceContext fmsc = FormModuleContext.getServices();
			String action = request.getParameter("action");	
			HtmlParser.parseFields(doc, dynamicForm);			
			dynamicForm.setProcessedHtml(doc.toString());
			if(action.equals("preview")){
//				modelAndView.addObject("command",dynamicForm);
				modelAndView.setViewName("denf_dynamic_form");
				return modelAndView;	
			}
			else if(action.equals("submit") || action.equals("submitFormOnly")){	
		//		LoggedInUser user=UserSessionUtils.getActiveUser(request);
				FormModuleServiceContext fmsc = null;
				try {		
					fmsc = FormModuleContext.getServices();
					this.saveFormAndFields(fmsc, dynamicForm);
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
	
	/*private void parseFields(Elements elements, Form dynamicForm) {
		// TODO Auto-generated method stub
		for(Element element : elements){
			String fieldname = element.attr(DynamicFormConstants.CUSTOM_ATTRIBUTE_NAME).trim();
			Field fieldToReplace = null;
			for(Field field: dynamicForm.getFieldsList())
			{
				if(field.getFieldType() != null){
					if(field.getFieldName().trim().equalsIgnoreCase(fieldname)){
						fieldToReplace = field;
						break;
					}
				}
				else dynamicForm.getFieldsList().remove(field);
			}
			if(fieldToReplace.getFieldType().getName().equals("select")){
				if(!element.hasAttr("nolabel")){
					element.before("<p>"+ fieldToReplace.getFieldLabel()+"</p>");
				}
				StringBuilder s = new StringBuilder();
				s.append("<select name='"+fieldToReplace.getFieldName()+"'>");
				if(fieldToReplace.getModelOrList().equalsIgnoreCase(DynamicFormConstants.RADIO_LIST_VALUE)){
					List<String> items = Arrays.asList(fieldToReplace.getFieldOptionsCommaDelimited().split("\\s*,\\s*"));
					for(String val: items){
						s.append("<option value='"+val+"'>"+val+"</option>");
					}
					s.append("</select>");
					element.before(s.toString());
					element.remove();
				}
			}
			else{
				if(!element.hasAttr("nolabel")){
					element.before("<p>"+ fieldToReplace.getFieldLabel()+"</p>");
				}
				element.before("<input name='"+fieldToReplace.getFieldName()+"' type='"+fieldToReplace.getFieldType().getName()+"'></input>");
				element.remove();
			}
			
		}
	}*/

	private void saveFormAndFields(FormModuleServiceContext fmsc, Form dynamicForm) throws Exception{
		// SAVE FORM
		int formId = (Integer) fmsc.getFormService().saveForm(dynamicForm);/* (Integer) sc.getDynamicFormService().saveForm(dynamicForm);*/
		Form df = (Form) fmsc.getSession().load(Form.class, formId);
		// SAVE FIELDS
		for(Field f : dynamicForm.getFieldsList()){
			
			if(f.getFieldType()!=null){
				f.getDynamicForm().add(df);
				Integer fieldId = null;
				if(f.getId()!=null && f.getId() > 0){
					fmsc.getSession().update(f);
					fieldId = f.getId();
				}
				else{
					fieldId = (Integer)fmsc.getFieldService().saveField(f);
				}
				
				if(f.getFieldType().getName().equalsIgnoreCase("select") || f.getFieldType().getName().equalsIgnoreCase("checkbox") || f.getFieldType().getName().equalsIgnoreCase("radio")){
					FormUtils.deletePreviousFieldOptions(f, fmsc);
					FieldListOptions flo = null;
					if(f.getModelOrList() != null && f.getModelOrList().equalsIgnoreCase("list")){
						for(String s : f.getFieldOptionsCommaDelimited().split(",")){
							flo = new FieldListOptions();
							flo.setField((Field)fmsc.getSession().load(Field.class, fieldId));
							flo.setFieldOption(s.trim());
							fmsc.getSession().save(flo);
						}
					}
				}
			}
		}
		
		fmsc.commitTransaction();
	}
	
	private void removeDeletedFieldsFromForm(Form dynamicForm){
		for (Iterator<Field> iterator = dynamicForm.getFieldsList().iterator(); iterator.hasNext(); ) {
		    Field f = iterator.next();
		    if(f.getFieldLabel().equalsIgnoreCase("deleted")){
				iterator.remove();
		    }
		}
	}
}
