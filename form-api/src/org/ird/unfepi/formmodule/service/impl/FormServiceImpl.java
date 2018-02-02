package org.ird.unfepi.formmodule.service.impl;

import java.io.Serializable;
import java.util.List;

import org.hibernate.exception.GenericJDBCException;
import org.ird.unfepi.formmodule.context.FormModuleServiceContext;
import org.ird.unfepi.formmodule.dao.DAOForm;
import org.ird.unfepi.formmodule.model.Form;
import org.ird.unfepi.formmodule.service.FormService;

public class FormServiceImpl implements FormService{

	private FormModuleServiceContext sc;
	private DAOForm daoForm;
	
	public FormServiceImpl(FormModuleServiceContext sc, DAOForm daoForm)
	{
		this.sc = sc;
		this.daoForm = daoForm;
	}
	@Override
	public Serializable saveForm(Form form) {
		// TODO Auto-generated method stub
		return daoForm.save(form);
	}

	@Override
	public List<Form> getAllForms() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<Form> getAllFormIdAndName() throws GenericJDBCException{
		// TODO Auto-generated method stub
		return daoForm.getAllFormsIdAndName();
	}
	@Override
	public Form getFormByName(String name) {
		// TODO Auto-generated method stub
		return daoForm.getFormByName(name);
	}
	@Override
	public Form getFormById(Integer id) {
		// TODO Auto-generated method stub
		return daoForm.getFormById(id);
	}
	@Override
	public void updateForm(Form form) {
		// TODO Auto-generated method stub
		daoForm.update(form);
	}
	@Override
	public String getFormNameById(Integer id) {
		// TODO Auto-generated method stub
		return daoForm.getFormNameById(id);
	}

}
