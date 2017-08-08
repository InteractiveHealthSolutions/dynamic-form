package org.ird.unfepi.formmodule.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="form_submission_field")
public class FormSubmissionField {

	private Integer id;
	private FormSubmission formSubmission;
	private Field field;
	private String value;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="form_submission_id")
	public FormSubmission getFormSubmission() {
		return formSubmission;
	}
	public void setFormSubmission(FormSubmission formSubmission) {
		this.formSubmission = formSubmission;
	}
	
	@ManyToOne
	@JoinColumn(name="field_id")
	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	@Column(name="value")
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
}
