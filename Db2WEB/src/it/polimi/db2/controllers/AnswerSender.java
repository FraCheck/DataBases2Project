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

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.db2.entities.MarketingQuestions;
import it.polimi.db2.entities.Questionnaire;
import it.polimi.db2.entities.QuestionnaireUserAnswers;
import it.polimi.db2.entities.User;
import it.polimi.db2.services.MarketingAnswersService;
import it.polimi.db2.services.QuestionnaireService;
import it.polimi.db2.services.QuestionnaireUserAnswersService;


@WebServlet("/AnswerSender")
public class AnswerSender extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	
	@EJB(name = "it.polimi.db2.services/QuestionnaireUserAnswersService")
	private QuestionnaireUserAnswersService service;
	
	@EJB(name = "it.polimi.db2.services/QuestionnaireService")
	private QuestionnaireService qService;
	
	@EJB(name = "it.polimi.db2.services/MarketingAnswersService")
	private MarketingAnswersService answersService;
	
	
	public AnswerSender() {
		super();
	}
	
	private  User user;
	
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
			path = "/WEB-INF/Greetings.html";
			user = (User) session.getAttribute("user");
		}

		

		templateEngine.process(path, ctx, response.getWriter());
	}

		
		
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		    doGet(request,response);
			
			LocalDate date = LocalDate.now();
			
			Questionnaire questionnaire = qService.findByDate(date);
			
			List<MarketingQuestions> questionsList = questionnaire.getMarketingQuestions();
			
			int mandatory_answers = questionsList.size();
			
			int age = 0;
			char sex = 0;
			int expertise = 0;
			int optional_answers = 0;

			String age_input = null;
			String sex_input = null;
			String expertise_input = null;
			
			optional_answers = 0;
			
			try {
				age_input = request.getParameter("Age");
				
				System.out.println(request.getParameter("Age"));
				System.out.println(request.getParameter("Sex"));
				System.out.println(request.getParameter("MyExpertise"));
				
				sex_input = request.getParameter("Sex");
				
				expertise_input = request.getParameter("MyExpertise");
			}catch(Exception e) {
				System.out.println("Errors when retrieving optional answers!");
			}
			
			if(age_input != null) {
				try{
					age = Integer.parseInt(age_input);
					optional_answers++;
				}catch(NumberFormatException e){}
			}
			
			if(sex_input != null) {
				
				if(sex_input.equalsIgnoreCase("1")) {
					sex = 'M';
					optional_answers++;
				}else if(sex_input.equalsIgnoreCase("2")) {
					sex = 'F';
					optional_answers++;
				}else if(sex_input.equalsIgnoreCase("3")){
					sex = 'N';
					optional_answers++;
				}
			}
			
			if(expertise_input != null) {
				
				if(expertise_input.equalsIgnoreCase("1")) {
					expertise = 1;
					optional_answers++;
				}else if(expertise_input.equalsIgnoreCase("2")) {
					expertise = 2;
					optional_answers++;
				}else if(expertise_input.equalsIgnoreCase("3")) {
					expertise = 3;
					optional_answers++;
				}
			}
			
			
			String[] storedAnswers = (String[]) request.getSession().getAttribute("storedAnswers");
			
			QuestionnaireUserAnswers q = service.createAnswer(age, sex, expertise, questionnaire, user, optional_answers, mandatory_answers);
			
			for(int i = 0; i< storedAnswers.length; i++) {
				
				int id = questionsList.get(i).getId(); // CHECK FOR ERRORS(?)
				String ans = storedAnswers[i];
				
				answersService.createAnswer(q, answersService.findById(id) , ans);
			}
			
		}
	
	public void destroy() {
	}
	
}
