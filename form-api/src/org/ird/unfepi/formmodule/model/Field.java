package org.ird.unfepi.formmodule.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.hibernate.annotations.ForeignKey;

@Entity
@Table(name="field")
public class Field implements java.io.Serializable {
	
	private Integer id;
	private List<Form> dynamicForm = new ArrayList<Form>();
	private String fieldName;
	private FieldType fieldType;
	private List<FormSubmissionField> formSubmissionField;
	
	private String modelOrList;
	private String modelName;
	private List<FieldListOptions> fieldListOptions;
	private String fieldOptionsCommaDelimited;
	private String fieldLabel;
	
	public Field()
	{
		this.dynamicForm = new ArrayList<Form>();
	}
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@ManyToMany(fetch=FetchType.LAZY, mappedBy="fieldsList")
	public List<Form> getDynamicForm() {
		return dynamicForm;
	}
	public void setDynamicForm(List<Form> dynamicForm) {
		this.dynamicForm = dynamicForm;
	}

	
	@Column(name="field_name")
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="field_type_id")
	public FieldType getFieldType() {
		return fieldType;
	}


	public void setFieldType(FieldType fieldType) {
		this.fieldType = fieldType;
	}

	@OneToMany(fetch=FetchType.LAZY, mappedBy="field")
	public List<FormSubmissionField> getFormSubmissionField() {
		return formSubmissionField;
	}


	public void setFormSubmissionField(List<FormSubmissionField> formSubmissionField) {
		this.formSubmissionField = formSubmissionField;
	}


	@Column(name="model_or_list")
	public String getModelOrList() {
		return modelOrList;
	}


	public void setModelOrList(String modelOrList) {
		this.modelOrList = modelOrList;
	}

	@Column(name="model_name")
	public String getModelName() {
		return modelName;
	}


	public void setModelName(String modelName) {
		this.modelName = modelName;
	}


	@OneToMany(fetch=FetchType.EAGER, mappedBy="field")
	public List<FieldListOptions> getFieldListOptions() {
		return fieldListOptions;
	}


	public void setFieldListOptions(List<FieldListOptions> fieldListOptions) {
		this.fieldListOptions = fieldListOptions;
	}

	@Transient
	public String getFieldOptionsCommaDelimited() {
		return fieldOptionsCommaDelimited;
	}


	public void setFieldOptionsCommaDelimited(String fieldOptionsCommaDelimited) {
		this.fieldOptionsCommaDelimited = fieldOptionsCommaDelimited;
	}

	@Column(name="field_label")
	public String getFieldLabel() {
		return fieldLabel;
	}


	public void setFieldLabel(String fieldLabel) {
		this.fieldLabel = fieldLabel;
	}
	
}
