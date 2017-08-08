package org.ird.unfepi.formmodule.dao;

import java.util.List;

import org.ird.unfepi.formmodule.model.Field;
import org.ird.unfepi.formmodule.model.Form;

public interface DAOField extends DAO{

	List<Field> getAll(boolean isreadonly, int firstResult, int fetchsize, String[] mappingsToJoin);
	
	Field getFieldByName(String name);
	
	List<Field> getFieldsByFormId(Integer id);
}
