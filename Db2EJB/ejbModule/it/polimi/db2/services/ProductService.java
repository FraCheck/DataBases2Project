package it.polimi.db2.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.polimi.db2.entities.Product;

@Stateless
public class ProductService {
	@PersistenceContext(unitName = "Db2EJB")
	private EntityManager em;

	public ProductService() {}
	
	public Product findById(int id) {
		return em.find(Product.class, id);
	}
}
