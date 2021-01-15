package it.polimi.db2.entities;

import java.io.Serializable;
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
@Table(name = "marketing_questions", schema = "db_project")
@NamedQueries({ @NamedQuery(name = "MarketingQuestions.findByQuestionnaire", query = "SELECT m FROM MarketingQuestions m  WHERE m.questionnaire = ?1")})
public class MarketingQuestions implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String question;

	@ManyToOne
	@JoinColumn(name = "questionnaire")
	private Questionnaire questionnaire;
	
	public MarketingQuestions() {}
	
	public MarketingQuestions(Questionnaire questionnaire, String question) {
		this.questionnaire = questionnaire;
		this.question = question;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public Questionnaire getQuestionnaire() {
		return questionnaire;
	}

	public void setQuestionnaire(Questionnaire questionnaire) {
		this.questionnaire = questionnaire;
	}


}
