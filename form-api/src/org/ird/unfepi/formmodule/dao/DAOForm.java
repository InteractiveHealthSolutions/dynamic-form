package org.ird.unfepi.formmodule.dao;


import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.GenericJDBCException;
import org.ird.unfepi.formmodule.model.Form;
import org.ird.unfepi.formmodule.utils.HibernateUtil;

public interface DAOForm extends DAO{

	List<Form> getAll(boolean isreadonly, int firstResult, int fetchsize, String[] mappingsToJoin);
	
	void updateForm(Form form);
	
	List<Form> getAllFormsIdAndName() throws GenericJDBCException;
	
	Form getFormByName(String name);
	
	Form getFormById(Integer id);

	String getFormNameById(Integer id);
}
