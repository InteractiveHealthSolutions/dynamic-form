package org.ird.unfepi.formmodule.dao;

import java.util.List;

import org.ird.unfepi.formmodule.model.FormSubmission;

public interface DAOFormSubmission extends DAO {

	List<FormSubmission> getFormSubmissionByFormId(Integer id);
	List<FormSubmission> getFormSubmissionByFormId(Integer id, int start, int max);
	FormSubmission getFormSubmissionById(Integer id);
}
