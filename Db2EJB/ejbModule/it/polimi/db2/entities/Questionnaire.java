package it.polimi.db2.entities;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "questionnaire", schema = "db_project")
@NamedQueries({ @NamedQuery(name = "Questionnaire.findByDate", query = "SELECT q FROM Questionnaire q WHERE q.date = ?1"),
	@NamedQuery(name = "Questionnaire.findAllFromToday", query = "SELECT q FROM Questionnaire q WHERE q.date >= CURRENT_DATE"),
	@NamedQuery(name = "Questionnaire.findPast", query = "SELECT q FROM Questionnaire q WHERE q.date < CURRENT_DATE")})
public class Questionnaire implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private Date date;
	
	@ManyToOne
	@JoinColumn(name = "product")
	private Product product;

	@OneToMany(mappedBy="questionnaire", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true )
	private List<MarketingQuestions> marketingQuestions;
	
	public Questionnaire() {}
	
	public Questionnaire(Date date, Product product) {
		this.date = date;
		this.product = product;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	public List<MarketingQuestions> getMarketingQuestions() {
		return marketingQuestions;
	}

	public void setMarketingQuestions(List<MarketingQuestions> marketingQuestions) {
		this.marketingQuestions = marketingQuestions;
	}
	
}
