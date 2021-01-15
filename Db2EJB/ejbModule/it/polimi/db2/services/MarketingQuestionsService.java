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
	
	public List<MarketingQuestions> findByQuestionnaire(Questionnaire q){
		List<MarketingQuestions> results = em.createNamedQuery("MarketingQuestions.findByQuestionnaire", MarketingQuestions.class).setParameter("1", q)
				.getResultList();
		if (results.isEmpty())
			return null;
		return results;
	}
	
	// Create a new question for a questionnaire
	public void createQuestion(Questionnaire questionnaire, String question) {
		MarketingQuestions mquestion = new MarketingQuestions(questionnaire, question);
		em.persist(mquestion);
	}
}
