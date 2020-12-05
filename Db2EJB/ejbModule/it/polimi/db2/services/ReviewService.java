package it.polimi.db2.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.polimi.db2.entities.Review;
@Stateless
public class ReviewService {
	@PersistenceContext(unitName = "Db2EJB")
	private EntityManager em;

	public ReviewService() {}
	
	public Review findById(int id) {
		return em.find(Review.class, id);
	}
}
