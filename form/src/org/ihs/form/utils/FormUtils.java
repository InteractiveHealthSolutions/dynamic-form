package org.ihs.form.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.ird.unfepi.formmodule.context.FormModuleServiceContext;
import org.ird.unfepi.formmodule.model.Field;
import org.ird.unfepi.formmodule.model.FieldListOptions;

public class FormUtils {

	public static Date getCurrentDateTime() throws ParseException{
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		return dateFormat.parse(dateFormat.format(date));
	}
	
	public static void deletePreviousFieldOptions(Field f, FormModuleServiceContext sc){
		List<FieldListOptions> list = new ArrayList<FieldListOptions>();
		String sql = "delete from field_list_options where field_id = " + f.getId();
		sc.getSession().createSQLQuery(sql).executeUpdate();		
	}
}
