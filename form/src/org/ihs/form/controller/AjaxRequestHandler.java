package org.ihs.form.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.ihs.form.utils.FormUtils;
import org.ihs.form.utils.StringUtils;
import org.ird.unfepi.formmodule.context.FormModuleContext;
import org.ird.unfepi.formmodule.context.FormModuleServiceContext;
import org.ird.unfepi.formmodule.model.Field;
import org.ird.unfepi.formmodule.model.FieldListOptions;
import org.ird.unfepi.formmodule.model.FieldType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

@Controller
@RequestMapping("/ajax")
public class AjaxRequestHandler {

	@RequestMapping(value = "/checkFieldExists", method = RequestMethod.POST)
	@ResponseBody
	public String getSample(@RequestBody String name) {
		FormModuleServiceContext sc = FormModuleContext.getServices();
		try{
			Gson gson = new Gson();
			List<Field> fields = sc.getSession().createQuery("from Field where fieldName = '"+name+"'").list();
			sc.commitTransaction();
			if(fields.size()>0){
				return "true";
			}
			else return "false";
		}catch(Exception e){
			sc.rollbackTransaction();
			e.printStackTrace();
			return null;
		}finally{
			sc.closeSession();
		}
//	    return gson.toJson(simulateSearchResult(term));
	}
	
	@RequestMapping(value = "/getAllFields", method = RequestMethod.GET)
	@ResponseBody
	public String getTags(@RequestParam String term) {
		Gson gson = new Gson();
	    return gson.toJson(simulateSearchResult(term));
	}

	private List<Object> simulateSearchResult(String tagName) {
		FormModuleServiceContext sc = FormModuleContext.getServices();
		try{
			List<Field> result= new ArrayList<Field>();
			List<Field> temp = new ArrayList<Field>();
			temp = sc.getFieldService().getAllFields();
			List<Object> listobj = new ArrayList<Object>();
			// iterate a list and filter by tagName
			for (Field formField : temp) {
				if(formField.getFieldName() != null){
					if (formField.getFieldName().toLowerCase().contains(tagName.toLowerCase().trim())) {
						HashMap<String, Object> t = new HashMap<String, Object>();
						t.put("fieldId",formField.getId());
						t.put("fieldTypeId",formField.getFieldType().getId());
						t.put("fieldName", formField.getFieldName());
						t.put("fieldType",formField.getFieldType().getName());
						t.put("modelOrList", formField.getModelOrList());
						t.put("modelName", formField.getModelName());
						t.put("fieldLabel", formField.getFieldLabel());
						t.put("fieldOptions", StringUtils.listToCommaSeparatedString(formField.getFieldListOptions()));
						listobj.add(t);
					}
				}
			}

			sc.commitTransaction();
			return listobj;
		}catch(Exception e){
			sc.rollbackTransaction();
			return null;
		}finally{
			sc.closeSession();
		}
	}


	@RequestMapping(value="/createField", method = RequestMethod.POST)
	public @ResponseBody String addField(@RequestBody HashMap<String, Object> obj) {
		FormModuleServiceContext sc = FormModuleContext.getServices();
	    System.out.println("Field = " +obj);
		Field field = new Field();
	    try{
		    field.setId(Integer.parseInt((String)obj.get("fieldIdHidden")));
		    field.setFieldLabel((String) obj.get("fieldLabel"));
		    field.setFieldName((String) obj.get("fieldName"));
		    field.setModelOrList((String)obj.get("radioModelOrList"));
		    FieldType fType = sc.getFieldTypeService().getFieldTypeById(Integer.parseInt((String)obj.get("fieldTypeId")));
		    field.setFieldType(fType);
		    int fieldId = 0;
		    if(field.getId() > 0){
				sc.getSession().update(field);
				fieldId = field.getId();
			}
			else{
				fieldId = (Integer)sc.getFieldService().saveField(field);
			}
		    
		    if(fType.getName().equalsIgnoreCase("select")){
		    	FormUtils.deletePreviousFieldOptions(field, sc);
		    	FieldListOptions flo = null;
				if(obj.get("radioModelOrList") != null && ((String)obj.get("radioModelOrList")).equalsIgnoreCase("list")){
					for(String s : ((String)obj.get("txtValues")).split(",")){
						flo = new FieldListOptions();
						flo.setField((Field)sc.getSession().load(Field.class, fieldId));
						flo.setFieldOption(s.trim());
						sc.getSession().save(flo);
					}
				}
		    }
		    
		    sc.commitTransaction();
		    Gson gson = new Gson();
		    return gson.toJson(field.getId());
		    /* REMAINING WORK - FETCHING DATA FROM REST URL */
	    }catch(Exception e){
	    	e.printStackTrace();
	    	sc.rollbackTransaction();
	    	return null;
	    }finally{
	    	sc.closeSession();
	    }
	}
}
