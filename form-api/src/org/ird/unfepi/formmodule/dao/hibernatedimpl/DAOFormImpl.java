package org.ird.unfepi.formmodule.dao.hibernatedimpl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.ird.unfepi.formmodule.dao.DAOForm;
import org.ird.unfepi.formmodule.model.Form;

public class DAOFormImpl extends DAOHibernateImpl implements DAOForm{

	/** The session. */
	private Session session;
	
	public DAOFormImpl(Session session) {
		super(session);
		// TODO Auto-generated constructor stub
		this.session = session;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Form> getAll(boolean isreadonly, int firstResult,
			int fetchsize, String[] mappingsToJoin) {
		// TODO Auto-generated method stub
		Criteria cri = session.createCriteria(Form.class).setReadOnly(isreadonly);

		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
//		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<Form> list = cri.list();
		return list;
	}

	@Override
	public List<Form> getAllFormsIdAndName() {
		// TODO Auto-generated method stub
		Criteria criteria = session.createCriteria(Form.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);;
		criteria.setProjection(Projections.projectionList().add(Projections.property("formName")).add(Projections.property("id")));
		
		return criteria.list();
	}

	@Override
	public Form getFormByName(String name) {
		// TODO Auto-generated method stub
		Criteria criteria = session.createCriteria(Form.class);
		criteria.add(Restrictions.eq("formName", name));
		List<Form> formList = criteria.list();
		try{
			return formList.get(0);
		}catch(IndexOutOfBoundsException e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Form getFormById(Integer id) {
		Criteria cri = session.createCriteria(Form.class);
		cri.add(Restrictions.eq("id", id));
		Form form = (Form) cri.uniqueResult();
		return form;
	}

	@Override
	public void updateForm(Form form) {
		// TODO Auto-generated method stub
		session.update(form);
		
	}

	@Override
	public String getFormNameById(Integer id) {
		Criteria cri = session.createCriteria(Form.class);
		cri.add(Restrictions.eq("id", id));
		cri.setProjection(Projections.property("formName"));
		cri.setMaxResults(1);
		String name = (String) cri.list().get(0);
		return name;
	}

}
