package org.ird.unfepi.formmodule.model;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name="form_submission")
public class FormSubmission {

	private Integer id;
	
	private Form form;
	
	private String createdByUser;

	private Set<FormSubmissionField> listFields = new LinkedHashSet<FormSubmissionField>();
	
	private Date createdDate;
	
	private Date startDate;
	
	private Date endDate;
	
	private Integer locationId;
	
	private Date dateEntryDateTime;
	
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

	@OneToMany(fetch=FetchType.LAZY, mappedBy="formSubmission")
	@Cascade(CascadeType.DELETE)
	public Set<FormSubmissionField> getListFields() {
		return listFields;
	}

	public void setListFields(Set<FormSubmissionField> listFields) {
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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="start_date")
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date start_date) {
		this.startDate = start_date;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="end_date")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date end_date) {
		this.endDate = end_date;
	}

	@Column(name="location_id")
	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	@Column(name="data_entry_date_time")
	public Date getDateEntryDateTime() {
		return dateEntryDateTime;
	}

	public void setDateEntryDateTime(Date dateEntryDateTime) {
		this.dateEntryDateTime = dateEntryDateTime;
	}
	
	
}
