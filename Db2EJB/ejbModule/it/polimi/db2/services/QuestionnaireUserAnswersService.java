package it.polimi.db2.services;


import java.sql.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.polimi.db2.entities.*;

@Stateless
public class QuestionnaireUserAnswersService {
	@PersistenceContext(unitName = "Db2EJB")
	private EntityManager em;

	public QuestionnaireUserAnswersService() {}
	
	public QuestionnaireUserAnswers findById(int id) {
		return em.find(QuestionnaireUserAnswers.class, id);
	}
	
	public List<QuestionnaireUserAnswers> findByQuestionnaire(int qId){
		List<QuestionnaireUserAnswers> results = em.createNamedQuery("QuestionnaireUserAnswers.findByQuestionnaire", QuestionnaireUserAnswers.class).setParameter("1",qId)
				.getResultList();
		if (results.isEmpty())
			return null;
		return results;
	}
	
	public List<QuestionnaireUserAnswers> findByQuestionnaireCancelled(int qId){
		List<QuestionnaireUserAnswers> results = em.createNamedQuery("QuestionnaireUserAnswers.findByQuestionnaireCancelled", QuestionnaireUserAnswers.class).setParameter("1",qId)
				.getResultList();
		if (results.isEmpty())
			return null;
		return results;
	}
	
	// Create a new QuestionnaireUserAnswers
	
}
