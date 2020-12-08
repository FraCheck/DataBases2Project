package it.polimi.db2.entities;

import java.io.Serializable;
import java.sql.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "questionnaire_points", schema = "db_project")
@NamedQueries(
		{ @NamedQuery(name = "QuestionnairePoints.findByDate", query = "SELECT q FROM QuestionnairePoints q WHERE q.date = ?1 ORDER BY q.points DESC ")})
public class QuestionnairePoints implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private Date date;

	private int points;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	public QuestionnairePoints() {}
	
	public QuestionnairePoints(Date date, int id, User user, int points) {
		super();
		this.id = id;
		this.date = date;
		this.user = user;
		this.points = points;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
