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
@Table(name="field_list_options")
public class FieldListOptions {

	private Integer id;
	private String fieldOption;
	private Field field;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name="field_option")
	public String getFieldOption() {
		return fieldOption;
	}
	public void setFieldOption(String option) {
		this.fieldOption = option;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="field_id")
	public Field getField() {
		return field;
	}
	public void setField(Field field) {
		this.field = field;
	}
}
