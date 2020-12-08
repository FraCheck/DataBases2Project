package it.polimi.db2.services;


import java.time.LocalDate;
import java.util.List;
import java.sql.Date;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.polimi.db2.entities.QuestionnairePoints;

@Stateless
public class PointsService {
	@PersistenceContext(unitName = "Db2EJB")
	private EntityManager em;

	public PointsService() {}
	
	public QuestionnairePoints findById(int id) {
		return em.find(QuestionnairePoints.class, id);
	}
	
	public List<QuestionnairePoints> findByDate(LocalDate date) {
		System.out.println(date.toString());
		List<QuestionnairePoints> results = em.createNamedQuery("QuestionnairePoints.findByDate", QuestionnairePoints.class).setParameter("1", Date.valueOf(date))
				.getResultList();
		if (results.size() > 0)
			return results;
		return null;
	}	
	
}
