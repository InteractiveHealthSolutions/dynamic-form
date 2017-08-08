package org.ird.unfepi.formmodule.dao.hibernatedimpl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
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
		Criteria cri = session.createCriteria(FormSubmission.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		cri.add(Restrictions.eq("form.id", id));
		List<FormSubmission> frmSub = cri.list();
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
}
