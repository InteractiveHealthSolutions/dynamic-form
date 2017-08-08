package org.ird.unfepi.formmodule.service.impl;

import java.io.Serializable;
import java.util.List;

import org.ird.unfepi.formmodule.context.FormModuleServiceContext;
import org.ird.unfepi.formmodule.dao.DAOField;
import org.ird.unfepi.formmodule.dao.DAOFieldType;
import org.ird.unfepi.formmodule.model.FieldType;
import org.ird.unfepi.formmodule.service.FieldTypeService;

public class FieldTypeServiceImpl implements FieldTypeService {

	private FormModuleServiceContext sc;
	private DAOFieldType daoFieldType;
	
	public FieldTypeServiceImpl(FormModuleServiceContext sc, DAOFieldType daoFieldType)
	{
		this.sc = sc;
		this.daoFieldType = daoFieldType;
	}
	
	@Override
	public Serializable saveFieldType(FieldType fieldType) {
		// TODO Auto-generated method stub
		return daoFieldType.save(fieldType);
	}

	@Override
	public List<FieldType> getAll() {
		// TODO Auto-generated method stub
		return daoFieldType.getAll(true, 0, 0, null);
	}

	@Override
	public FieldType getFieldTypeById(Integer id) {
		return daoFieldType.getFieldTypeById(id);
	}

}
