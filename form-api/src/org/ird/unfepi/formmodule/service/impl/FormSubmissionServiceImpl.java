package org.ird.unfepi.formmodule.service.impl;

import java.util.List;
import java.util.Set;

import org.ird.unfepi.formmodule.context.FormModuleServiceContext;
import org.ird.unfepi.formmodule.dao.DAOFieldType;
import org.ird.unfepi.formmodule.dao.DAOFormSubmission;
import org.ird.unfepi.formmodule.model.FormSubmission;
import org.ird.unfepi.formmodule.service.FormSubmissionService;

public class FormSubmissionServiceImpl implements FormSubmissionService{

	private FormModuleServiceContext sc;
	private DAOFormSubmission daoFormSubmission;
	
	public FormSubmissionServiceImpl(FormModuleServiceContext sc, DAOFormSubmission daoFormSubmission)
	{
		this.sc = sc;
		this.daoFormSubmission = daoFormSubmission;
	}
	
	@Override
	public List<FormSubmission> getFormSubmissionByFormId(Integer id) {
		// TODO Auto-generated method stub
		return daoFormSubmission.getFormSubmissionByFormId(id);
	}

	@Override
	public FormSubmission getFormSubmissionById(Integer id) {
		return daoFormSubmission.getFormSubmissionById(id);
	}

	@Override
	public List<FormSubmission> getFormSubmissionByFormId(Integer id,
			int start, int max) {
		return daoFormSubmission.getFormSubmissionByFormId(id,start,max);
	}

}
