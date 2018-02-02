package org.ird.unfepi.formmodule.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="field_type")
public class FieldType implements java.io.Serializable {
	static int counter = 0;
	private Integer id;
	private String name;
	private List<Field> fields;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="field_type_id")
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name="name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="fieldType")
	public List<Field> getFields() {
		return fields;
	}
	public void setFields(List<Field> fields) {
		this.fields = fields;
	}
	
	@Override
	public int hashCode()
	{
		return super.hashCode()+ ++counter;
	}
}
