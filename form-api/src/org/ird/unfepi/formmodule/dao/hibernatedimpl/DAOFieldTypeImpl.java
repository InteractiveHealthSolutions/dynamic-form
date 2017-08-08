package org.ird.unfepi.formmodule.dao.hibernatedimpl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.ird.unfepi.formmodule.dao.DAOFieldType;
import org.ird.unfepi.formmodule.model.Field;
import org.ird.unfepi.formmodule.model.FieldType;
import org.ird.unfepi.formmodule.model.Form;

public class DAOFieldTypeImpl extends DAOHibernateImpl implements DAOFieldType {

	private Session session;
	
	public DAOFieldTypeImpl(Session session) {
		super(session);
		// TODO Auto-generated constructor stub
		this.session = session;
	}

	@Override
	public List<FieldType> getAll(boolean isreadonly, int firstResult,
			int fetchsize, String[] mappingsToJoin) {
		// TODO Auto-generated method stub
		Criteria cri = session.createCriteria(FieldType.class).setReadOnly(isreadonly);

		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
//		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		List<FieldType> list = cri.list();
		return list;
	}

	@Override
	public FieldType getFieldTypeById(Integer id) {
		Criteria cri = session.createCriteria(FieldType.class);
		cri.add(Restrictions.eq("id", id));
		FieldType fType = (FieldType) cri.uniqueResult();
		return fType;
	}

}
