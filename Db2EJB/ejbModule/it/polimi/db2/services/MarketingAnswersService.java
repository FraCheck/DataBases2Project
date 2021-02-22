package it.polimi.db2.services;

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
		
	public void createAnswer(QuestionnaireUserAnswers questionnaireUserAnswer, MarketingQuestions question, String answer) {
		MarketingAnswers ans = new MarketingAnswers(questionnaireUserAnswer, question, answer);		
		em.persist(ans);		
	}
}

