package org.ird.unfepi.formmodule.context;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.ird.unfepi.formmodule.dao.DAOField;
import org.ird.unfepi.formmodule.dao.DAOFieldType;
import org.ird.unfepi.formmodule.dao.DAOForm;
import org.ird.unfepi.formmodule.dao.DAOFormSubmission;
import org.ird.unfepi.formmodule.dao.hibernatedimpl.DAOFieldImpl;
import org.ird.unfepi.formmodule.dao.hibernatedimpl.DAOFieldTypeImpl;
import org.ird.unfepi.formmodule.dao.hibernatedimpl.DAOFormImpl;
import org.ird.unfepi.formmodule.dao.hibernatedimpl.DAOFormSubmissionImpl;
import org.ird.unfepi.formmodule.service.FieldService;
import org.ird.unfepi.formmodule.service.FieldTypeService;
import org.ird.unfepi.formmodule.service.FormService;
import org.ird.unfepi.formmodule.service.FormSubmissionService;
import org.ird.unfepi.formmodule.service.impl.FieldServiceImpl;
import org.ird.unfepi.formmodule.service.impl.FieldTypeServiceImpl;
import org.ird.unfepi.formmodule.service.impl.FormServiceImpl;
import org.ird.unfepi.formmodule.service.impl.FormSubmissionServiceImpl;

public class FormModuleServiceContext {

	private FormService formService;
	
	private FieldService fieldService;
	
	private FieldTypeService fieldTypeService;
	
	private FormSubmissionService formSubmissionService;

	private Session session;

	private Transaction transaction;
	
	public FormModuleServiceContext(SessionFactory sessionObj){
		this.session = sessionObj.openSession();
		this.transaction = session.beginTransaction();
		DAOForm daoForm = new DAOFormImpl(session);
		this.setFormService(new FormServiceImpl(this,daoForm));
		
		DAOField daoField = new DAOFieldImpl(session);
		this.setFieldService(new FieldServiceImpl(this, daoField));
		
		DAOFieldType daoFieldType = new DAOFieldTypeImpl(session);
		this.setFieldTypeService(new FieldTypeServiceImpl(this, daoFieldType));
		
		DAOFormSubmission daoFS = new DAOFormSubmissionImpl(session);
		this.setFormSubmissionService(new FormSubmissionServiceImpl(this, daoFS));
	}

	public FieldService getFieldService() {
		return fieldService;
	}

	public void setFieldService(FieldService fieldService) {
		this.fieldService = fieldService;
	}

	public FormService getFormService() {
		return formService;
	}

	public void setFormService(FormService formService) {
		this.formService = formService;
	}
	
	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public void closeSession()
	{
		try
		{
			session.close();
		}
		catch (Exception e)
		{
		}
	}

	public void beginTransaction()
	{
		if (transaction == null)
		{
			transaction = session.beginTransaction();
		}
	}
	
	public void commitTransaction()
	{
		transaction.commit();
	}
	
	public void rollbackTransaction()
	{
		if (transaction != null)
		{
			transaction.rollback();
		}
	}

	public FieldTypeService getFieldTypeService() {
		return fieldTypeService;
	}

	public void setFieldTypeService(FieldTypeService fieldTypeService) {
		this.fieldTypeService = fieldTypeService;
	}
	
	public FormSubmissionService getFormSubmissionService() {
		return formSubmissionService;
	}

	public void setFormSubmissionService(FormSubmissionService formSubmissionService) {
		this.formSubmissionService = formSubmissionService;
	}
}
