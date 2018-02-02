package org.ird.unfepi.formmodule.service;

import java.util.List;
import java.util.Set;

import org.ird.unfepi.formmodule.model.FormSubmission;

public interface FormSubmissionService {

	List<FormSubmission> getFormSubmissionByFormId(Integer id);
	List<FormSubmission> getFormSubmissionByFormId(Integer id, int start, int max);
	FormSubmission getFormSubmissionById(Integer id);
}
