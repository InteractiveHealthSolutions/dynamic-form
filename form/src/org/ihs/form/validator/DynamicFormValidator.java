package org.ihs.form.validator;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringEscapeUtils;
import org.ird.unfepi.formmodule.context.FormModuleContext;
import org.ird.unfepi.formmodule.context.FormModuleServiceContext;
import org.ird.unfepi.formmodule.model.Field;
import org.ird.unfepi.formmodule.model.Form;
import org.ird.unfepi.formmodule.utils.FieldElementsValidator;
import org.ird.unfepi.formmodule.utils.HtmlParser;
import org.ihs.form.constants.DynamicFormConstants;
import org.ihs.form.controller.EditFormController;
import org.ihs.form.utils.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.nodes.Element;
import org.jsoup.parser.HtmlTreeBuilder;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class DynamicFormValidator implements Validator{

	Document doc;
	private String rawHtml;
	
	public Document getDoc() {
		return doc;
	}

	public void setDoc(Document doc) {
		this.doc = doc;
	}

	@Override
	public boolean supports(Class clazz) {
		// TODO Auto-generated method stub
		return Form.class.equals(clazz);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		// TODO Auto-generated method stub
/*		DynamicForm df = (DynamicForm) obj; 
		validateRawHtml(df.getRawHtml(), errors);*/
	}

	public Document validateForm(Form form, Errors errors, String origFormName) throws Exception{
		rawHtml = form.getRawHtml().replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&nbsp;", " ").replaceAll("<p>", "").replaceAll("</p>", "");
		if(StringUtils.isEmpty(form.getFormName()))
			errors.rejectValue("formName", "nocode","Form name is required");
		else{
//			if(!EditFormController.formName.equalsIgnoreCase(form.getFormName())){
			if(!StringUtils.isEmpty(origFormName) && !origFormName.equalsIgnoreCase(form.getFormName())){
				FormModuleServiceContext sc = null;
				try{
					sc = FormModuleContext.getServices();
					Form temp = null; 
					temp = sc.getFormService().getFormByName(form.getFormName());
					if(temp != null)
						errors.rejectValue("formName", "nocode", "This name already exists");
				}catch(Exception e){
					sc.rollbackTransaction();
					e.printStackTrace();
					errors.rejectValue("formName", "nocode", "Some error occurred and form could not be validated");
					/**
					 * TODO Show error on client side if error occurs. 
					 */
					throw e;
				}finally{
					sc.closeSession();
				}
				}
		}
		if(StringUtils.isEmpty(rawHtml))
			errors.rejectValue("rawHtml","nocode","Please provide some html");
		
		return validateHtml(form, errors);
	}
	public Document validateHtml(Form df, Errors errors){
		HtmlParser parser = new HtmlParser();
		Document doc = parser.parseHtml(rawHtml);
		
		if(parser.getErrors().size()>0){
			for(ParseError error : parser.getErrors()){
				if(error.isCustom()){
					errors.rejectValue("rawHtml", "nocode", error.getErrorMessage());
					return null;
				}
			}
		}
		/*******
		 * 
		 * 
		 * Check for duplicate fields added
		 * 
		 * 
		 *******/
		
		Elements elements = doc.getElementsByTag("field");
		Set<String> elementSet = new HashSet<String>();
		for(Element el : elements){
			if(!elementSet.add(el.attr("name").trim()))
				errors.rejectValue("rawHtml", "nocode", "Field with the name ' "+el.attr("name")+" ' appears multiple times in HTML (not allowed)");
		}
		
		
		{
			String errorMsg = FieldElementsValidator.validateFieldElements(doc, df);
			if(errorMsg != null)
				errors.rejectValue("rawHtml", "nocode", errorMsg);
			/*Elements elements = doc.getElementsByTag("field");
			if(elements.size() == 0){
				errors.rejectValue("rawHtml", "nocode", "No field tags defined");
				return null;
			}
			else{
				boolean found = false;
				for(Element el : elements){
					if(!el.hasAttr("name")){
						errors.rejectValue("rawHtml", "nocode", "One of the field tags does not contain 'name' attribute");
						return null;
					}
					else{
						for(Field f : df.getFieldsList()){
							if(f.getFieldName().equalsIgnoreCase(el.attr("name"))){
								found = true;
								break;
							}
							else found = false;
						}
						if(!found){
							errors.rejectValue("rawHtml", "nocode", "No field added with the name '"+el.attr("name") + "'");
							return null;
						}
				
					}
				}*/
			}
			return doc;
		}
	
	private void checkForValidName(Form df, Element element, Errors errors){
		String name = element.attr(DynamicFormConstants.CUSTOM_ATTRIBUTE_NAME);
		for(Field field: df.getFieldsList())
		{
			if(field.getFieldType() != null){
				if(field.getFieldName().equals(name)){
					return;
				}
			}
		}
		errors.rejectValue("rawHtml", "nocode", "No field defined with the name '" + name + "'");
	}
}
