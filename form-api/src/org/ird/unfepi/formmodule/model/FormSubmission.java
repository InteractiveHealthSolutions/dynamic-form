package org.ird.unfepi.formmodule.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name="form_submission")
public class FormSubmission {

	private Integer id;
	
	private Form form;
	
	private String createdByUser;

	private List<FormSubmissionField> listFields;
	
	private Date createdDate;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(name="form_id")
	public Form getForm() {
		return form;
	}

	public void setForm(Form dynamicForm) {
		this.form = dynamicForm;
	}

	@Transient
	public String getCreatedByUser() {
		return createdByUser;
	}

	public void setCreatedByUser(String createdByUser) {
		this.createdByUser = createdByUser;
	}

	@OneToMany(fetch=FetchType.EAGER, mappedBy="formSubmission")
	public List<FormSubmissionField> getListFields() {
		return listFields;
	}

	public void setListFields(List<FormSubmissionField> listFields) {
		this.listFields = listFields;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_date")
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	
}
