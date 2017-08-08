package org.ird.unfepi.formmodule.service.impl;

import java.io.Serializable;
import java.util.List;

import org.ird.unfepi.formmodule.context.FormModuleServiceContext;
import org.ird.unfepi.formmodule.dao.DAOField;
import org.ird.unfepi.formmodule.dao.DAOForm;
import org.ird.unfepi.formmodule.model.Field;
import org.ird.unfepi.formmodule.service.FieldService;

public class FieldServiceImpl implements FieldService{

	private FormModuleServiceContext sc;
	private DAOField daoField;
	
	public FieldServiceImpl(FormModuleServiceContext sc, DAOField daoField)
	{
		this.sc = sc;
		this.daoField = daoField;
	}
	
	@Override
	public Serializable saveField(Field field) {
		// TODO Auto-generated method stub
		return daoField.save(field);
	}

	@Override
	public List<Field> getAllFields() {
		// TODO Auto-generated method stub
		return daoField.getAll(true, 0, 0, null);
	}

	@Override
	public Field getFieldByName(String name) {
		// TODO Auto-generated method stub
		return daoField.getFieldByName(name);
	}

	@Override
	public List<Field> getFieldsByFormId(Integer id) {
		// TODO Auto-generated method stub
		return daoField.getFieldsByFormId(id);
	}

	
}
