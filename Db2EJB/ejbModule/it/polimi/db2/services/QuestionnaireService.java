package it.polimi.db2.services;


import java.time.LocalDate;
import java.util.List;
import java.sql.Date;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.polimi.db2.entities.*;

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
	public List<Questionnaire> findAllFromToday(){
		List<Questionnaire> results = em.createNamedQuery("Questionnaire.findAllFromToday", Questionnaire.class)
				.getResultList();
		return results;
	}
	
	// Create a new questionnaire
	public Questionnaire createQuestionnaire(LocalDate date, Product product) {
		if (findByDate(date) == null) {
			Questionnaire questionnaire = new Questionnaire(java.sql.Date.valueOf(date), product);
			em.persist(questionnaire);
			return questionnaire;
		}else {
			System.out.println("CRITICAL ERROR: trying to create a questionnaire with a date for which already exists.");
			return null;
		}
	}
	
	// Edit questionnaire product
	public void updateQuestionnaire(Questionnaire questionnaire, int pId) {
		Product managedProduct = em.find(Product.class, pId);
		Questionnaire questionnaireManaged = em.find(Questionnaire.class, questionnaire.getId());
		questionnaireManaged.setProduct(managedProduct);
	}
}
