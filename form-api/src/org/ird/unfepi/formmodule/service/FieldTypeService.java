package org.ird.unfepi.formmodule.service;

import java.io.Serializable;
import java.util.List;

import org.ird.unfepi.formmodule.model.FieldType;


public interface FieldTypeService {

	Serializable saveFieldType(FieldType form);
	
	List<FieldType> getAll();
	
	FieldType getFieldTypeById(Integer id);
}
