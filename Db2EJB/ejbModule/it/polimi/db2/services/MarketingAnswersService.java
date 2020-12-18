package it.polimi.db2.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.polimi.db2.entities.MarketingAnswers;
import it.polimi.db2.entities.MarketingQuestions;

@Stateless
public class MarketingAnswersService {
	@PersistenceContext(unitName = "Db2EJB")
	private EntityManager em;

	public MarketingAnswersService() {}
	
	public MarketingQuestions findById(int id) {
		return em.find(MarketingQuestions.class, id);
	}
	
	public List<MarketingAnswers> findByQuestionnaireUserAnswer(int quaId){
		List<MarketingAnswers> results = em.createNamedQuery("MarketingAnswers.findByQuestionnaireUserAnswer", MarketingAnswers.class).setParameter(1, quaId)
				.getResultList();
		if (results.isEmpty())
			return null;
		return results;
	}
	
	public List<MarketingAnswers> findByQustionnaireAndUser(int quaId, String username){
		List<MarketingAnswers> results = em.createNamedQuery("Questionnaire.findByQuestionnaireUserAnswer", MarketingAnswers.class).setParameter(1, quaId).setParameter(2, username)
				.getResultList();
		if (results.isEmpty())
			return null;
		return results;
	}
	
	// Create a new answer for a questionnaire
}

