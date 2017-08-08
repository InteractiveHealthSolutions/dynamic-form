package org.ird.unfepi.formmodule.dao;

import java.util.List;

import org.ird.unfepi.formmodule.model.FieldType;

public interface DAOFieldType extends DAO {

	List<FieldType> getAll(boolean isreadonly, int firstResult, int fetchsize, String[] mappingsToJoin);
	FieldType getFieldTypeById(Integer id);
}
