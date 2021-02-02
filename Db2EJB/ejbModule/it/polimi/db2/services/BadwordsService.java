package it.polimi.db2.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.polimi.db2.entities.Badwords;

@Stateless
public class BadwordsService {
	@PersistenceContext(unitName = "Db2EJB")
	private EntityManager em;

	public BadwordsService() {}
	
	public Badwords findById(int id) {
		return em.find(Badwords.class, id);
	}
	
	public List<Badwords> findAllBadwords() {
		List <Badwords> results = em.createNamedQuery("Badwords.findAll", Badwords.class).getResultList();
		if (results.isEmpty())
			return null;
		return results;
	}	
}
