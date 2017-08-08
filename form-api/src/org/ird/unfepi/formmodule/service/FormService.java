package org.ird.unfepi.formmodule.service;

import java.io.Serializable;
import java.util.List;

import org.ird.unfepi.formmodule.model.Form;


public interface FormService {

	Serializable saveForm(Form form);
	
	void updateForm(Form form);
	
	List<Form> getAllForms();
	
	List<Form> getAllFormIdAndName();
	
	Form getFormByName(String name);
	
	Form getFormById(Integer id);
	
	String getFormNameById(Integer id);
}
