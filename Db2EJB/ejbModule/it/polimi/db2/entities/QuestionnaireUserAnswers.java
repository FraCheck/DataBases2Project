package it.polimi.db2.entities;


import java.io.Serializable;
import java.sql.Timestamp;
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

import it.polimi.db2.services.MarketingQuestionsService;
import util.MandatoryAnswersStorage;

@Entity
@Table(name = "questionnaire_user_answers", schema = "db_project")
@NamedQueries({@NamedQuery(name = "QuestionnaireUserAnswers.findByQuestionnaire", query = "SELECT q FROM QuestionnaireUserAnswers q WHERE q.questionnaire.id = ?1 AND q.cancelled=false"),
	@NamedQuery(name = "QuestionnaireUserAnswers.findByQuestionnaireUser", query = "SELECT q FROM QuestionnaireUserAnswers q WHERE q.questionnaire.id = ?1 AND q.cancelled=false AND q.user = ?2"),
	@NamedQuery(name = "QuestionnaireUserAnswers.findByQuestionnaireCancelled", query = "SELECT q FROM QuestionnaireUserAnswers q WHERE q.questionnaire.id = ?1 AND q.cancelled=true")})
public class QuestionnaireUserAnswers implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private int age;
	
	private char sex;
	
	private int expertise_level;
	
	private boolean cancelled;
	
	private int mandatory_answers;
	
	private int optional_answers;
	
	private Timestamp date_cancellation;
	
	
	@ManyToOne
	@JoinColumn(name = "questionnaire")
	private Questionnaire questionnaire;
	
	@ManyToOne
	@JoinColumn(name = "user")
	private User user;
	
	@OneToMany(mappedBy="questionnaireUserAnswer", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true )
	private List <MarketingAnswers> marketingAnswers;
	
	public List<MarketingAnswers> getMarketingAnswers() {
		return marketingAnswers;
	}


	public void setMarketingAnswers(List<MarketingAnswers> marketingAnswers) {
		this.marketingAnswers = marketingAnswers;
	}


	public QuestionnaireUserAnswers() {}
	
	
	//Constructor for answers submitted
	public QuestionnaireUserAnswers(int age, char sex, int expertise_level, Questionnaire questionnaire, User user, int optional_answers, int mandatory_answers) {
		
		this.age = age;
		this.sex = sex;
		this.expertise_level = expertise_level;
		this.cancelled = false;
		this.questionnaire = questionnaire;
		this.user = user;
		this.optional_answers = optional_answers;
		this.mandatory_answers = mandatory_answers;
	}
	
	//Constructor for answers cancelled
	public QuestionnaireUserAnswers(Questionnaire questionnaire, User user, Timestamp date) {
		this.cancelled = true;
		this.date_cancellation = date;
		this.user = user;
		this.questionnaire = questionnaire;
		
	}

	
	public void addMarketingAnswers(Questionnaire questionnaire, MandatoryAnswersStorage list) {
		MarketingQuestionsService service = new MarketingQuestionsService();
		int length = list.getSize(); 
		for(int i = 0; i< length; i ++) {
		
			int id = list.removeAnswer_1();
			String answer = list.removeAnswer_2();
		   marketingAnswers.add(new MarketingAnswers(this, service.findById(id), answer));
		
		}
		
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public char getSex() {
		return sex;
	}

	public void setSex(char sex) {
		this.sex = sex;
	}

	public int getExpertise_level() {
		return expertise_level;
	}

	public void setExpertise_level(int expertise_level) {
		this.expertise_level = expertise_level;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public Questionnaire getQuestionnaire() {
		return questionnaire;
	}

	public void setQuestionnaire(Questionnaire questionnaire) {
		this.questionnaire = questionnaire;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getOptional_answers() {
		return optional_answers;
	}

	public void setOptional_answers(int optional_answers) {
		this.optional_answers = optional_answers;
	}

	public int getMandatory_answers() {
		return mandatory_answers;
	}

	public void setMandatory_answers(int mandatory_answers) {
		this.mandatory_answers = mandatory_answers;
	}

	public Timestamp getDate_cancellation() {
		return date_cancellation;
	}

	public void setDate_cancellation(Timestamp date_cancellation) {
		this.date_cancellation = date_cancellation;
	}



	
}
