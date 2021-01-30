package it.polimi.db2.services;

import java.sql.Timestamp;
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
	
	public QuestionnaireUserAnswers createAnswer(int age, char sex, int expertise, Questionnaire questionnaire, User user, int optional_answers, int mandatory_answers) {
		
		    QuestionnaireUserAnswers answer = new QuestionnaireUserAnswers(age, sex, expertise, questionnaire, user, optional_answers, mandatory_answers);
		   
			em.persist(answer);
			
			return answer;
	}
	
	
	public QuestionnaireUserAnswers deleteAnswer(User user, Questionnaire questionnaire, Timestamp date) {
		
	    QuestionnaireUserAnswers answer = new QuestionnaireUserAnswers(questionnaire, user, date);
	
		
		em.persist(answer);
		
		return answer;
    }
	
	public boolean userAlreadyAnswered(User user, int qId) {
		List<QuestionnaireUserAnswers> results = em.createNamedQuery("QuestionnaireUserAnswers.findByQuestionnaireUser", QuestionnaireUserAnswers.class).setParameter("1",qId).setParameter("2", user).getResultList();
		if (!results.isEmpty()) {
			return true;
		}
		return false;
	}
	
}
