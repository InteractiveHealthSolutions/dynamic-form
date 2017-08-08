package org.ird.unfepi.formmodule.context;

import java.util.Properties;

import javax.management.InstanceAlreadyExistsException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.ird.unfepi.formmodule.utils.HibernateUtil;

public class FormModuleContext {

	private static SessionFactory sessionFactory;

	public static void instantiate(Properties properties) throws InstanceAlreadyExistsException{
		// session factory must have been instantiated before we could use any method involving data
		sessionFactory = HibernateUtil.getSessionFactory(properties);
	}
	
	private static SessionFactory getSessionFactory(){
		if(sessionFactory == null)
			sessionFactory = HibernateUtil.getSessionFactory(null);
		
		return sessionFactory;
	}
	
	/** Before calling this method make sure that Context has been instantiated ONCE and ONLY ONCE by calling {@linkplain Context#instantiate} method
	 */
	public static Session getNewSession() {
		return getSessionFactory().openSession();
	}
	
	/** Before calling this method make sure that Context has been instantiated ONCE and ONLY ONCE by calling {@linkplain Context#instantiate} method
	 *  
	 * NOTE: For assurance of prevention from synchronization and consistency be sure to get new ServiceContext Object
	 * for each bulk or batch of transactions. i.e Using single object for whole application may produce undesired results
	 *
	 * @return the services
	 */
	public static FormModuleServiceContext getServices(){
		
		return new FormModuleServiceContext(getSessionFactory());
	}
}
