//UNUSED, CREATED FOR EARLY DESIGN, CAN BE DELETED

package it.polimi.db2.controllers;

import java.time.LocalDate;
import java.util.List;

import javax.ejb.EJB;


import it.polimi.db2.entities.MarketingQuestions;
import it.polimi.db2.entities.Questionnaire;
import it.polimi.db2.services.MarketingQuestionsService;
import it.polimi.db2.services.QuestionnaireService;


public class MandatoryQuestionReader{
	
	@EJB(name = "it.polimi.db2.services/MarketingQuestionsService")
	private MarketingQuestionsService questionService;
	
	@EJB(name = "it.polimi.db2.services/QuestionnaireService")
	private QuestionnaireService questionnaireService;
	
	public MandatoryQuestionReader() {
		super();
	}
	
	
	//Get the list (as array of Strings) of today's questions
	public String[] getTodayQuestions(){
		List<MarketingQuestions> list;
		LocalDate date = LocalDate.now();
		
		
	    Questionnaire quest = questionnaireService.findByDate(date);
		list = questionService.findByQuestionnaire(quest);
		
		String[] questionsList = {};
		
		for(int i = 0; i < list.size(); i ++) {
			questionsList[i] = list.get(i).getQuestion();
		}
		
		return questionsList;
			
	}
	
	public int getNumberOfQuestions() {
		LocalDate date = LocalDate.now();
		Questionnaire quest = questionnaireService.findByDate(date);
		List<MarketingQuestions> list = questionService.findByQuestionnaire(quest);
		return list.size();
	}	
	
}
