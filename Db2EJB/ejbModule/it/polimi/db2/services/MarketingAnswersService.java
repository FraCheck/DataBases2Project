package it.polimi.db2.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.polimi.db2.entities.MarketingAnswers;
import it.polimi.db2.entities.MarketingQuestions;
import it.polimi.db2.entities.QuestionnaireUserAnswers;

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
	
	public List<MarketingAnswers> findByQuestionnaireAndUser(int quaId, String username){
		List<MarketingAnswers> results = em.createNamedQuery("MarketingAnswers.findByQuestionnaireUserAnswer", MarketingAnswers.class).setParameter(1, quaId).setParameter(2, username)
				.getResultList();
		if (results.isEmpty())
			return null;
		return results;
	}
	
	public List<MarketingAnswers> findByUser(String username){
		List<MarketingAnswers> results = em.createNamedQuery("MarketingAnswers.findByUser", MarketingAnswers.class).setParameter(1, username)
				.getResultList();
		if (results.isEmpty())
			return null;
		return results;
	}
	
	public void createAnswer(QuestionnaireUserAnswers questionnaireUserAnswer, MarketingQuestions question, String answer) {
		MarketingAnswers ans = new MarketingAnswers(questionnaireUserAnswer, question, answer);
		
		em.persist(ans);
		
	}
}

