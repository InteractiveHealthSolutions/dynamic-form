package org.ird.unfepi.formmodule.service;

import java.util.List;

import org.ird.unfepi.formmodule.model.FormSubmission;

public interface FormSubmissionService {

	List<FormSubmission> getFormSubmissionByFormId(Integer id);
}
