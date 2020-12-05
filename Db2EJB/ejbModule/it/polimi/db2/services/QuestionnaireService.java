package it.polimi.db2.services;


import java.time.LocalDate;
import java.util.List;
import java.sql.Date;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.polimi.db2.entities.Questionnaire;

@Stateless
public class QuestionnaireService {
	@PersistenceContext(unitName = "Db2EJB")
	private EntityManager em;

	public QuestionnaireService() {}
	
	public Questionnaire findById(int id) {
		return em.find(Questionnaire.class, id);
	}
	
	public Questionnaire findByDate(LocalDate date) {
		System.out.println(date.toString());
		List<Questionnaire> results = em.createNamedQuery("Questionnaire.findByDate", Questionnaire.class).setParameter("1", Date.valueOf(date))
				.getResultList();
		if (results.size() > 0)
			return results.get(0);
		return null;
	}
}
