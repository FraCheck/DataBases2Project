package it.polimi.db2.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.db2.entities.*;
import it.polimi.db2.services.*;

@WebServlet("/Questionnaire")
public class GoToQuestionnairePage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	
	@EJB(name = "it.polimi.db2.services/MarketingQuestionsService")
	private MarketingQuestionsService questionService;
	
	@EJB(name = "it.polimi.db2.services/QuestionnaireService")
	private QuestionnaireService questionnaireService;
	
	public GoToQuestionnairePage() {
		super();
	}

	public void init() throws ServletException {
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String path;
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		// If the user is not logged in (not present in session) redirect to the login		
		HttpSession session = request.getSession();
		if (session.isNew() || session.getAttribute("user") == null) {
			path = "/WEB-INF/index.html";
		}else {
			
			path = "/WEB-INF/Home.html";
			
			int questions_done = (int) request.getSession().getAttribute("questions_done");
			boolean completed = (boolean) request.getSession().getAttribute("completequestionnaire");
			
			
			if(!completed) {
				path = "/WEB-INF/Questionnaire.html";
				
				String[] list = null;
				try {
					list = (String[]) request.getSession().getAttribute("questionsList");
				}catch(Exception e) {}
				
				if(questions_done == 0) {
					//Questionnaire started
					LocalDate date = LocalDate.now();
					
					
				    Questionnaire quest = questionnaireService.findByDate(date);
				    
				    List<MarketingQuestions> qList = questionService.findByQuestionnaire(quest);
				    
				    
				    try {
					
					String[] questionsList = new String[qList.size()];
					String[] answersList = new String[qList.size()];
					
					for(int i = 0; i < qList.size(); i ++) {
						questionsList[i] = qList.get(i).getQuestion();
					}
					
					request.getSession().setAttribute("storedAnswers", answersList);
					request.getSession().setAttribute("questionsList", questionsList);
					
					String current_question = questionsList[questions_done];
					ctx.setVariable("question", current_question );
				    }catch(NullPointerException e) {
				    	path = "/WEB-INF/Home.html";
				    	System.out.println("Empty Questionnaire");
				    }
					
		}else if(questions_done == list.length ) {
			//Questionnaire ended, redirect to optional part, reset counter;
			path = "/WEB-INF/QuestionnaireOptional.html";
			request.getSession().setAttribute("questions_done", 0);
			
		}else {
			list = (String[]) request.getSession().getAttribute("questionsList");
			String current_question = list[questions_done];
			ctx.setVariable("question", current_question );
		}
				
	}

		}

		templateEngine.process(path, ctx, response.getWriter());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try{doGet(request, response);
		
		int done =(int) request.getSession().getAttribute("questions_done");
		String[] storedAnswers = (String[]) request.getSession().getAttribute("storedAnswers");
		
		String answer = StringEscapeUtils.escapeJava(request.getParameter("answer"));
		
		storedAnswers[done] = answer;
		
		done++;
		request.getSession().setAttribute("questions_done", done);
		}catch(NullPointerException e) {
	    	System.out.println("Empty Questionnaire");
		}
	}

	public void destroy() {
	}

}
