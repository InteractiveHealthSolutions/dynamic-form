package org.ird.unfepi.formmodule.dao.hibernatedimpl;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.ird.unfepi.formmodule.dao.DAOFormSubmission;
import org.ird.unfepi.formmodule.model.FormSubmission;

public class DAOFormSubmissionImpl extends DAOHibernateImpl implements
		DAOFormSubmission {

	private Session session;
	
	public DAOFormSubmissionImpl(Session session) {
		super(session);
		this.session = session;
	}

	@Override
	public List<FormSubmission> getFormSubmissionByFormId(Integer id) {
		Criteria cri = session.createCriteria(FormSubmission.class)/*.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)*/;
		cri.createAlias("form", "f").add(Restrictions.eq("f.id", id));
		List<FormSubmission> frmSub =  cri.list();
		return frmSub;
	}

	/*@Override
	public List<FormSubmission> getFormSubmissionByFormId(Integer id) {
		Criteria fs = session.createCriteria(FormSubmission.class,"fs").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		fs.setFetchMode("FormSubmissionField", FetchMode.JOIN).setFetchMode("Field", FetchMode.JOIN);
		fs.createAlias("fs.listFields", "fields");
		fs.add(Restrictions.eq("form.id", id));
		
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("fields.field"));
		projList.add(Projections.property("fields.value"));
		fs.setProjection(Projections.distinct(projList));
		
		List fslist = fs.list();
		List<FormSubmission> frmSub = fs.list();
		return frmSub;
	}*/
	
	@Override
	public FormSubmission getFormSubmissionById(Integer id) {
		Criteria cri = session.createCriteria(FormSubmission.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		cri.add(Restrictions.eq("id", id));
		FormSubmission frmSub = (FormSubmission) cri.uniqueResult();
		return frmSub;
	}

	@Override
	public List<FormSubmission> getFormSubmissionByFormId(Integer id,
			int start, int max) {		
		/*Criteria cri = session.createCriteria(FormSubmission.class);
		cri.createAlias("form", "f").add(Restrictions.eq("f.id", id));
		cri.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).setFirstResult(start).setMaxResults(max);
		
		cri.setFirstResult(start);
		cri.setMaxResults(max);
		List<FormSubmission> frmSub = cri.list();*/
		
		Query query = session.createQuery("From FormSubmission where form.id = "+id);
		query.setFirstResult(start);
		query.setMaxResults(max);
		List<FormSubmission> fooList = query.list();
		return fooList;
	}
}
