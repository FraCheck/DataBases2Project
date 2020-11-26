package it.polimi.db2.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.NonUniqueResultException;

import it.polimi.db2.entities.User;
import it.polimi.db2.exceptions.*;


import java.util.List;

@Stateless
public class UserService {
	@PersistenceContext(unitName = "Db2EJB")
	private EntityManager em;

	public UserService() {
	}

	public User checkCredentials(String usrn, String pwd) throws CredentialsException, NonUniqueResultException {
		List<User> uList = null;
		try {
			uList = em.createNamedQuery("User.checkCredentials", User.class).setParameter(1, usrn).setParameter(2, pwd)
					.getResultList();
		} catch (PersistenceException e) {
			throw new CredentialsException("Could not verify credentals");
		}
		if (uList.isEmpty())
			return null;
		else if (uList.size() == 1)
			return uList.get(0);
		throw new NonUniqueResultException("More than one user registered with same credentials");

	}
	public User findUserByUsername(String username) throws CredentialsException, UserExistsAlreadyException {
		List<User> uList = null;
		try {
			uList = em.createNamedQuery("User.findByUsername", User.class).setParameter(1, username)
					.getResultList();
		} catch (PersistenceException e) {
			throw new CredentialsException("Could not verify credentals");
		}
		if (uList.isEmpty())
			return null;
		else if (uList.size() == 1)
			return uList.get(0);
		throw new UserExistsAlreadyException("An account with your username is already registered."
				+ "Please choose a new username");
	}
	
	public User findUserByEmail(String email) throws CredentialsException, UserExistsAlreadyException {
		List<User> uList = null;
		try {
			uList = em.createNamedQuery("User.findByEmail", User.class).setParameter(1, email)
					.getResultList();
		} catch (PersistenceException e) {
			throw new CredentialsException("Could not verify credentals");
		}
		if (uList.isEmpty())
			return null;
		else if (uList.size() == 1)
			return uList.get(0);
		throw new UserExistsAlreadyException("An account with your e-Mail is already registered"
				+ "Please choose a new e-Mail");
	}
	
	
	//Create a new user
	public User createUser(String username, String pwd, String email) throws UserExistsAlreadyException, CredentialsException {
		if(findUserByUsername(username) == null && findUserByEmail(email) == null) {
			System.out.println("Debug test");
			User user = new User(username, pwd, email);
			//for debugging: let's check if user is managed
			System.out.println("Method createUser");
			System.out.println("Is user object managed?  " + em.contains(user));
			//em.persist(user);
			System.out.println(user.getPassword());
			return user;
		}else
			return null;
	}
	
	
	public void updateProfile(User u) throws UpdateProfileException {
		try {
			em.merge(u);
		} catch (PersistenceException e) {
			throw new UpdateProfileException("Could not change profile");
		}
	}
}
