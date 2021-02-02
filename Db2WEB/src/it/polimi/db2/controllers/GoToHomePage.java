package it.polimi.db2.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.db2.entities.*;
import it.polimi.db2.services.*;

@WebServlet("/Home")
public class GoToHomePage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	
	
	@EJB(name = "it.polimi.db2.services/QuestionnaireService")
	private QuestionnaireService questionnaireService;
	
	@EJB(name = "it.polimi.db2.services/BadwordsService")
	private BadwordsService badwordsService;
	
	@EJB(name = "it.polimi.db2.services/MarketingAnswerService")
	private MarketingAnswersService answersService;
	
	public GoToHomePage() {
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
			User user = (User)session.getAttribute("user");
			
			List <Badwords> badwordsObj = badwordsService.findAllBadwords();
			
			List<String> badwords = new ArrayList<String>();
			
			String username = user.getUsername();
			
			
			List<MarketingAnswers> results = answersService.findByUser(username);
			
			List<String> answersTexts = new ArrayList<String>();
			
			for(int i = 0 ; i < badwordsObj.size() ; i++) {
				
				badwords.add(badwordsObj.get(i).getText());
		
			}
			
			try {
			
				for(int i = 0 ; i < results.size() ; i++) {
				
					answersTexts.add(results.get(i).getAnswer());
			
				}
			}catch(NullPointerException e) {}
			
			
			boolean isBanned = false;
			
			for(int i = 0 ; i < answersTexts.size() ; i++) {
				
				
				if(badwords.contains(answersTexts.get(i))) {
					isBanned = true;
				}
			}
			
			
			if(isBanned) {
				user.setBanned(true);
			}
			
			request.getSession().setAttribute("questions_done", 0);
			
			
			if (user.getBanned()) {
				ctx.setVariable("banned",true);
			}else {
			LocalDate today = LocalDate.now();
			Questionnaire availableQuestionnaire = questionnaireService.findByDate(today);		
			ctx.setVariable("todayQuestionnaire", availableQuestionnaire);
			ctx.setVariable("banned",false);
			}		
		}

		

		templateEngine.process(path, ctx, response.getWriter());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
	}

}
