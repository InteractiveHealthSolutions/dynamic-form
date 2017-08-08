package org.ird.unfepi.formmodule.service;

import java.io.Serializable;
import java.util.List;

import org.ird.unfepi.formmodule.model.Field;


public interface FieldService {

	Serializable saveField(Field form);
	
	List<Field> getAllFields();
	
	Field getFieldByName(String name);
	
	List<Field> getFieldsByFormId(Integer id);
}
