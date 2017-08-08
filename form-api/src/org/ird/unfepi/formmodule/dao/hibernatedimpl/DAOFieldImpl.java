package org.ird.unfepi.formmodule.dao.hibernatedimpl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.ird.unfepi.formmodule.dao.DAOField;
import org.ird.unfepi.formmodule.model.Field;
import org.ird.unfepi.formmodule.model.Form;

public class DAOFieldImpl extends DAOHibernateImpl implements DAOField {

	/** The session. */
	private Session session;
	
	public DAOFieldImpl(Session session) {
		super(session);
		// TODO Auto-generated constructor stub
		this.session = session;
	}

	@Override
	public List<Field> getAll(boolean isreadonly, int firstResult,
			int fetchsize, String[] mappingsToJoin) {
		// TODO Auto-generated method stub
		Criteria cri = session.createCriteria(Field.class).setReadOnly(isreadonly);

		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
//		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		List<Field> list = cri.list();
		return list;
	}

	@Override
	public Field getFieldByName(String name) {
		// TODO Auto-generated method stub
		Criteria cri = session.createCriteria(Field.class);
		cri.add(Restrictions.eq("fieldName", name));
		cri.setMaxResults(1);
		Field field = (Field) cri.uniqueResult();
		return field;
	}

	@Override
	public List<Field> getFieldsByFormId(Integer id) {
		// TODO Auto-generated method stub
		List<Integer> field_ids = session.createSQLQuery("select field_id from form_fields where form_id = " + id).list();
		try{	
			Criteria cri = session.createCriteria(Field.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			cri.add(Restrictions.in("id", field_ids));
			List<Field> fields = cri.list();
			return fields;
		}catch(Exception e)
		{
			return null;
		}
	}

}
