package org.ird.unfepi.formmodule.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.ird.unfepi.formmodule.model.Field;
import org.ird.unfepi.formmodule.model.Form;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

public class HtmlParser {

	private Parser parser;
	private List<ParseError> errors;

	public HtmlParser(){
		this.errors = new ArrayList<ParseError>();
	}
	
	public Document parseHtml(String html){
		parser = Parser.htmlParser();
		parser.setTrackErrors(50);
		Document doc = parser.parseInput(html, "utf-8");
		setErrors(parser.getErrors());
		return doc;
	}

	public Parser getParser() {
		return parser;
	}

	public List<ParseError> getErrors(){
		return this.errors;
	}
	
	public void setErrors(List<ParseError> errors) {
		this.errors = errors;
	}
	
	public static void parseFields(Document doc, Form dynamicForm) {
		Elements elements = doc.getElementsByTag("field");

		for(Element element : elements){
			String fieldname = element.attr("name");
			Field fieldToReplace = null;
			for(Field field: dynamicForm.getFieldsList())
			{
				if(field.getFieldType() != null){
					if(field.getFieldName().equalsIgnoreCase(fieldname)){
						fieldToReplace = field;
						break;
					}
				}
				else dynamicForm.getFieldsList().remove(field);
			}
			if(fieldToReplace.getFieldType().getName().equals("select")){
				if(!element.hasAttr("nolabel")){
					element.before("<span>"+ fieldToReplace.getFieldLabel()+":</span>");
				}
				StringBuilder s = new StringBuilder();
				s.append("<select name='"+fieldToReplace.getFieldName()+"'>");
				if(fieldToReplace.getModelOrList().equalsIgnoreCase("list")){
					List<String> items = Arrays.asList(fieldToReplace.getFieldOptionsCommaDelimited().split("\\s*,\\s*"));
					for(String val: items){
						s.append("<option value='"+val+"'>"+val+"</option>");
					}
					s.append("</select>");
					element.before(s.toString());
					element.remove();
				}
			}
			else{
				if(!element.hasAttr("nolabel")){
					element.before("<span>"+ fieldToReplace.getFieldLabel()+":</span>");
				}
				element.before("<input name='"+fieldToReplace.getFieldName()+"' type='"+fieldToReplace.getFieldType().getName()+"'></input>");
				element.remove();
			}
			
		}
		doc.getElementsByTag("body").get(0).prepend("<h1 class='light' align='center'>"+dynamicForm.getFormName()+"</h1>");
		
		
	}
}
