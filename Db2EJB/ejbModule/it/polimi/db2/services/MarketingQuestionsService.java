package it.polimi.db2.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.polimi.db2.entities.MarketingQuestions;
import it.polimi.db2.entities.Questionnaire;
@Stateless
public class MarketingQuestionsService {
	@PersistenceContext(unitName = "Db2EJB")
	private EntityManager em;

	public MarketingQuestionsService() {}
	
	public MarketingQuestions findById(int id) {
		return em.find(MarketingQuestions.class, id);
	}
}
