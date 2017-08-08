package org.ird.unfepi.formmodule.utils;

import org.ird.unfepi.formmodule.model.Field;
import org.ird.unfepi.formmodule.model.Form;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class FieldElementsValidator {

	public static String validateFieldElements(Document doc, Form form){
		Elements elements = doc.getElementsByTag("field");
		if(elements.size() == 0){
			return "No field tags defined";
		}
		else{
			boolean found = false;
			for(Element el : elements){
				if(!el.hasAttr("name")){
					return "One of the field tags does not contain 'name' attribute";
				}
				else{
					for(Field f : form.getFieldsList()){
						if(f.getFieldName().trim().equalsIgnoreCase(el.attr("name").trim())){
							found = true;
							break;
						}
						else found = false;
					}
					if(!found){
						return "No field added with the name '"+el.attr("name") + "'";
					}			
				}
			}
		}
		return null;
	}
}
