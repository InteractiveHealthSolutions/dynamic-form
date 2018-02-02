package org.ihs.form.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Query;
import org.ihs.form.utils.FormUtils;
import org.ihs.form.utils.StringUtils;
import org.ird.unfepi.formmodule.context.FormModuleContext;
import org.ird.unfepi.formmodule.context.FormModuleServiceContext;
import org.ird.unfepi.formmodule.model.Field;
import org.ird.unfepi.formmodule.model.FieldListOptions;
import org.ird.unfepi.formmodule.model.FieldType;
import org.ird.unfepi.formmodule.model.Form;
import org.ird.unfepi.formmodule.model.FormSubmission;
import org.ird.unfepi.formmodule.model.FormSubmissionField;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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
						t.put("regex", formField.getRegex());
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
		    field.setRegex((String)obj.get("regexPat"));
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
		    
		    if(fType.getName().equalsIgnoreCase("select") || fType.getName().equalsIgnoreCase("checkbox") || fType.getName().equalsIgnoreCase("radio")){
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
		    /** 
		     * 
		     * TODO: FETCHING DATA FROM REST URL 
		     * 
		     * */
	    }catch(Exception e){
	    	e.printStackTrace();
	    	sc.rollbackTransaction();
	    	return null;
	    }finally{
	    	sc.closeSession();
	    }
	}
	
	@RequestMapping(value="/dtsource", method=RequestMethod.POST)
	public @ResponseBody String postmethod(HttpServletRequest req){
		/*System.out.println(req.getParameter("order[0][column]"));
		System.out.println(req.getParameter("order[1][column]"));
		System.out.println(req.getParameter("order[2][column]"));*/
		Integer colNum = Integer.parseInt(req.getParameter("order[0][column]"));
		/*System.out.println(req.getParameter("columns["+colNum.toString()+"][data]"));
		System.out.println(req.getParameter("form_id"));*/
		
		Integer start = Integer.parseInt(req.getParameter("start"));
		Integer max = Integer.parseInt(req.getParameter("length"));
		
		FormModuleServiceContext sc = FormModuleContext.getServices();
		try
		{
			Integer intId = null;
			try{
				intId  = Integer.parseInt(req.getParameter("form_id"));
			}
			catch(Exception e){
				intId = sc.getFormService().getFormByName(req.getParameter("form_id")).getId();
			}
			
			ModelAndView model = new ModelAndView("dvf_show_form_data");
			List<FormSubmission> formSubmission = sc.getFormSubmissionService().getFormSubmissionByFormId(intId,start,max);
			List<Field> fields = sc.getFieldService().getFieldsByFormId(intId);
			Map<String, Object> tm = new HashMap<String,Object>();
			
		
			List<Object> listOfValuesMap = new ArrayList<Object>();
			for(FormSubmission fs : formSubmission){
				fs.getListFields();
				tm = new HashMap<String,Object>();
				for(FormSubmissionField fsf : fs.getListFields()){
					if(fsf.getField().getFieldType().getName().equalsIgnoreCase("textarea")){
						if(!StringUtils.isEmpty(fsf.getValueTextarea()))
							tm.put(fsf.getField().getFieldName(), fsf.getValueTextarea());
					}
					else if(!StringUtils.isEmpty(fsf.getValue()))
						tm.put(fsf.getField().getFieldName(), fsf.getValue());
					
				}
				/**
				 * ADD MISSING FIELDS IN FORM SUBMISSION 
				 */
				
				for(Field field : fields){
					if(!tm.containsKey(field.getFieldName()))
						tm.put(field.getFieldName(), "");
				}
				/**
				 * ADD MISSING FIELDS IN FORM SUBMISSION 
				 */
				tm.put("fs_id", fs.getId());
				tm.put("created_date", fs.getCreatedDate() == null ? "" : fs.getCreatedDate().toString().substring(0, 10));
				listOfValuesMap.add(tm);
			}

			String countQ = "Select count (fs.id) from FormSubmission fs where fs.form.id = "+intId;
		    Query countQuery = sc.getSession().createQuery(countQ);
		    Long countResults = (Long) countQuery.uniqueResult();
		    
			HashMap<String,Object> newMap = new HashMap<String, Object>();
			newMap.put("recordsTotal", countResults);
			newMap.put("recordsFiltered", countResults);
			newMap.put("data", listOfValuesMap);
			newMap.put("draw", req.getParameter("draw"));
			
			model.addObject("form_name",sc.getFormService().getFormNameById(intId));
			return (new JSONObject(newMap)).toString();
		}
		catch(Exception e ){
			e.printStackTrace();
			sc.rollbackTransaction();
			return null;
		}
		finally
		{
			sc.closeSession();
		}
	}
	
	@RequestMapping(value="/getColumns", method=RequestMethod.POST)
	public @ResponseBody String getColumns(@RequestBody String id,HttpServletRequest req){
		System.out.print(req.getParameter("id"));
		System.out.print(id);
		FormModuleServiceContext sc = null;
		List<Field> fields = null;
		try{
			sc = FormModuleContext.getServices();
			fields = sc.getFieldService().getFieldsByFormId(Integer.parseInt(id));
		}catch(Exception e){
			return null;
			/**
			 * TODO Show error alert on client side 
			 */
		}finally{
			sc.closeSession();
		}
		HashMap<String, String> map = new HashMap<String, String>();
		List list = new ArrayList<Object>();
		
		map.put("data", "fs_id");	
		map.put("title", "");
		map.put("defaultContent", "");
		list.add(new JSONObject(map));
		
		map.put("data", "created_date");	
		map.put("title", "Created Date");
		map.put("defaultContent", "");
		list.add(new JSONObject(map));
		for(Field field : fields){
			map = new HashMap<String, String>();
			map.put("data", field.getFieldName());
			map.put("title", field.getFieldLabel());
			map.put("defaultContent", "");
			list.add(new JSONObject(map));
		}
		return list.toString();
		
	}
	
	@RequestMapping(value="/deleteForm", method=RequestMethod.GET)
	public @ResponseBody Integer deleteForm(@RequestParam Integer id){
		System.out.print(id);
		FormModuleServiceContext sc = FormModuleContext.getServices();
		try{
			Form frm = (Form) sc.getSession().load(Form.class, id);
			sc.getSession().delete(frm);
			sc.commitTransaction();
		}catch(Exception e){
			e.printStackTrace();
			sc.rollbackTransaction();
			return -1;
		}finally{
			if(sc!=null && sc.getSession().isOpen())
				sc.closeSession();
		}
		return id;
	}
}
