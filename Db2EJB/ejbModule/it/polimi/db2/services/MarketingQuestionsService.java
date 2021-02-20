package it.polimi.db2.services;

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
	
	
	// Create a new question for a questionnaire
	public void createQuestion(Questionnaire questionnaire, String question) {
		MarketingQuestions mquestion = new MarketingQuestions(questionnaire, question);
		em.persist(mquestion);
	}
}
