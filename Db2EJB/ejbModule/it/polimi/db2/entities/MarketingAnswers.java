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
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "marketing_answers", schema = "db_project") 
@NamedQueries({@NamedQuery(name = "MarketingAnswers.findByQustionnaireAndUser", query = "SELECT m FROM MarketingAnswers m WHERE m.questionnaireUserAnswer.questionnaire.id = ?1 AND m.questionnaireUserAnswer.user.username = ?2"),
	@NamedQuery(name = "MarketingAnswers.findByQuestionnaireUserAnswer", query = "SELECT m FROM MarketingAnswers m WHERE m.questionnaireUserAnswer.id = ?1")})
public class MarketingAnswers implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@OneToOne
	@JoinColumn(name="question")
	private MarketingQuestions question;

	private String answer;
	
	@ManyToOne
	@JoinColumn(name = "questionnaire_user_answers")
	private QuestionnaireUserAnswers questionnaireUserAnswer;
	
	public MarketingAnswers() {}

	public MarketingAnswers(QuestionnaireUserAnswers questionnaireUserAnswer, MarketingQuestions question, String answer) {
		this.question = question;
		this.answer = answer;
		this.questionnaireUserAnswer = questionnaireUserAnswer;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public MarketingQuestions getQuestion() {
		return question;
	}

	public void setQuestion(MarketingQuestions question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public QuestionnaireUserAnswers getQuestionnaireUserAnswer() {
		return questionnaireUserAnswer;
	}

	public void setQuestionnaireUserAnswer(QuestionnaireUserAnswers questionnaireUserAnswer) {
		this.questionnaireUserAnswer = questionnaireUserAnswer;
	}

}
