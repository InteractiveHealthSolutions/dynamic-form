/*
 * 
 */

package org.ird.unfepi.formmodule.utils;

import java.util.Properties;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.ird.unfepi.formmodule.model.Field;
import org.ird.unfepi.formmodule.model.FieldListOptions;
import org.ird.unfepi.formmodule.model.FieldType;
import org.ird.unfepi.formmodule.model.Form;
import org.ird.unfepi.formmodule.model.FormSubmission;
import org.ird.unfepi.formmodule.model.FormSubmissionField;

public class HibernateUtil {
	
 	private static SessionFactory sessionFactory;
 	
 	/**
 	 * Gets the sessionFactory with given Properties. 
 	 * @param properties Properties that would be used to configure hibernate. Null if should be default i.e. read from cfg.xml file
 	 * @param configFileName File where hibernate mapping are defined. Null if should be default i.e. hibernate.cfg.xml
 	 * @return
 	 */
 	public synchronized static SessionFactory getSessionFactory (Properties properties) {
		if (sessionFactory == null) {
			Configuration conf = new Configuration();
			conf.addAnnotatedClass(Field.class);
			conf.addAnnotatedClass(FieldListOptions.class);
			conf.addAnnotatedClass(FieldType.class);
			conf.addAnnotatedClass(Form.class);
			conf.addAnnotatedClass(FormSubmission.class);
			conf.addAnnotatedClass(FormSubmissionField.class);
			if(properties != null){
				conf.setProperties(properties);
			}
			else{
				conf.configure("fm-hibernate.cfg.xml");
			}
			
			sessionFactory = conf.buildSessionFactory();
		}
		return sessionFactory;
		
	/*	
		Configuration cfg = new Configuration();
		cfg.configure("fm-hibernate.cfg.xml");

		ServiceRegistryBuilder builder = new ServiceRegistryBuilder();
		builder.applySettings(cfg.getProperties());
		ServiceRegistry sr = builder.buildServiceRegistry();
		return cfg.buildSessionFactory(sr);*/
	}
}
