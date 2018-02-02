package org.ihs.form.utils;

import org.ird.unfepi.formmodule.model.FormSubmission;
import org.ird.unfepi.formmodule.model.FormSubmissionField;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.CharacterReader;
import org.jsoup.select.Elements;

public enum FieldSetter {	
	textarea{
		
		@Override
		public void read(Document doc, FormSubmission ls, FormSubmissionField fsf) throws Exception{
			System.out.print("Hey");
				Elements el = doc.getElementsByAttributeValue("name",fsf.getField().getFieldName());
				try{
					el.get(0).text(fsf.getValueTextarea());
/*					String[] valuesArray = fsf.getValueTextarea().split(",");
					for(int i=0; i<valuesArray.length; i++){
						el.get(i).text(((String)valuesArray[i]).trim());
					}*/
				}catch(Exception e){}
			
		}
	},
	
	checkbox{

		@Override
		public void read(Document doc, FormSubmission ls, FormSubmissionField fsf) throws Exception {
				Elements elements = doc.getElementsByAttributeValue("name",fsf.getField().getFieldName());
				try{
					String[] valuesArray = fsf.getValue().split(",");
					for(int i=0; i<valuesArray.length; i++){
						Elements elements1 = elements.select("[value = "+valuesArray[i]+"]");
						
						for(Element e : elements1){
							if(e.attr("value").equals( ((String)valuesArray[i]).trim()) && !e.hasAttr("checked"))
								e.attr("checked",true);					
						}
					}
				}catch(Exception e){}
		}
	},
	
	radio{

		@Override
		public void read(Document doc, FormSubmission ls, FormSubmissionField fsf) throws Exception {
				Elements elements = doc.getElementsByAttributeValue("name",fsf.getField().getFieldName());
				try{
					String[] valuesArray = fsf.getValue().split(",");
					for(int i=0; i<valuesArray.length; i++){
						Elements elements1 = elements.select("[value = "+valuesArray[i]+"]");
						
						for(Element e : elements1){
							if(e.attr("value").equals( ((String)valuesArray[i]).trim()) && !e.hasAttr("checked"))
								e.attr("checked",true);					
						}
					}
				}catch(Exception e){}
		}	
	},
	
	select{

		@Override
		public void read(Document doc, FormSubmission ls, FormSubmissionField fsf) throws Exception {
				Elements el = doc.getElementsByAttributeValue("name",fsf.getField().getFieldName());
				try{
					String[] valuesArray = fsf.getValue().split(",");				
					for(int i=0; i<valuesArray.length; i++){
						Elements options = el.select("option");
						for(Element opt : options){
							if(opt.attr("value").equals(((String)valuesArray[i]).trim()))
								opt.attr("selected", true);
						}
					}
				}catch(Exception e){}
		}
		
	},
	
	number{

		@Override
		public void read(Document doc, FormSubmission ls, FormSubmissionField fsf) throws Exception {
				Elements el = doc.getElementsByAttributeValue("name",fsf.getField().getFieldName());
				try{
					el.get(0).attr("value", fsf.getValue());
/*					String[] valuesArray = fsf.getValue().split(",");
					for(int i=0; i<valuesArray.length; i++){
						el.get(i).attr("value", ((String)valuesArray[i]).trim());
					}*/
				}catch(Exception e){}
		}
	},
	
	text{

		@Override
		public void read(Document doc, FormSubmission ls, FormSubmissionField fsf) throws Exception {
				Elements el = doc.getElementsByAttributeValue("name",fsf.getField().getFieldName());
				try{
					el.get(0).attr("value", fsf.getValue());
					/*String[] valuesArray = fsf.getValue().split(",");
					for(int i=0; i<valuesArray.length; i++){
						el.get(i).attr("value", ((String)valuesArray[i]).trim());
					}*/
				}catch(Exception e){}
		}
	},
	
	date{

		@Override
		public void read(Document doc, FormSubmission ls, FormSubmissionField fsf) throws Exception {
				Elements el = doc.getElementsByAttributeValue("name",fsf.getField().getFieldName());
				try{
					el.get(0).attr("value", fsf.getValue());
/*					String[] valuesArray = fsf.getValue().split(",");
					for(int i=0; i<valuesArray.length; i++){
						el.get(i).attr("value", ((String)valuesArray[i]).trim());
					}*/
				}catch(Exception e){}
		}
	};
	
	public abstract void read(Document doc, FormSubmission ls, FormSubmissionField fsf) throws Exception;
}
