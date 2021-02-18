package it.polimi.db2.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the user database table.
 * 
 */
@Entity
@Table(name = "user", schema = "db_project")
@NamedQueries({ @NamedQuery(name = "User.checkCredentials", query = "SELECT r FROM User r  WHERE r.username = ?1 and r.password = ?2"),
	@NamedQuery(name = "User.findByUsername", query = "SELECT r FROM User r WHERE r.username = ?1"),
	@NamedQuery(name = "User.findByEmail", query = "SELECT r FROM User r WHERE r.email = ?1")})
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String password;

	private String username;
	
	private String email;
	
	private Boolean banned;
	
	private int role;

	public User(){}
	
		
	public User(String user, String psw, String email) {
		this.username = user;
		this.password = psw;
		this.email = email;
		this.banned = false;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getRole() {
		return this.role;
	}

	public void setRole(int role) {
		this.role = role;
	}
	
	public Boolean getBanned() {
		return this.banned;
	}

	public void setBanned(Boolean banned) {
		this.banned = banned;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}


}