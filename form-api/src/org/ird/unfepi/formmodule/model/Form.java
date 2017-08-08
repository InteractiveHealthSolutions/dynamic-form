package org.ird.unfepi.formmodule.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.collections.FactoryUtils;
import org.apache.commons.collections.list.LazyList;
import org.hibernate.annotations.ForeignKey;

@Entity
@Table (name = "form")
public class Form implements java.io.Serializable{

/**
	 * 
	 */

	//	public 
	private Integer id;
	
	private String formName;

	private String rawHtml;
	
	private String processedHtml;
	
	private List<FormSubmission> formSubmission;
	
	@SuppressWarnings("unchecked")
	private List<Field> fieldsList = LazyList.decorate(new ArrayList<Field>(),  
		       FactoryUtils.instantiateFactory(Field.class)); ;
	
	public Form() {
		
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="form_id")
	public Integer getId() {
		return id;
	}
	public void setId(Integer formId) {
		this.id = formId;
	}
	
	@Column(name="form_name", length=45)
	public String getFormName() {
		return formName;
	}
	public void setFormName(String formName) {
		this.formName = formName;
	}
	
	@Column(name="raw_html")
	public String getRawHtml() {
		return rawHtml;
	}
	public void setRawHtml(String html) {
		this.rawHtml = html;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="form_fields", 
	joinColumns={@JoinColumn(name="form_id")}, 
	inverseJoinColumns={@JoinColumn(name="field_id")})
	public List<Field> getFieldsList() {
		return fieldsList;
	}

	public void setFieldsList(List<Field> fieldsList) {
		this.fieldsList = fieldsList;
	}

	@Column(name="processed_html")
	public String getProcessedHtml() {
		return processedHtml;
	}

	public void setProcessedHtml(String processedHtml) {
		this.processedHtml = processedHtml;
	}

	@OneToMany(fetch=FetchType.LAZY, mappedBy="form")
	public List<FormSubmission> getFormSubmission() {
		return formSubmission;
	}

	public void setFormSubmission(List<FormSubmission> formSubmission) {
		this.formSubmission = formSubmission;
	}	
}
