
package it.polimi.db2.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.db2.entities.MarketingQuestions;
import it.polimi.db2.entities.Product;
import it.polimi.db2.entities.Questionnaire;
import it.polimi.db2.services.MarketingQuestionsService;
import it.polimi.db2.services.ProductService;
import it.polimi.db2.services.QuestionnaireService;


@WebServlet("/AddQuestionToQuestionnaire")
public class AddQuestionToQuestionnaire extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	
	@EJB(name = "it.polimi.db2.services/QuestionnaireService")
	private QuestionnaireService qService;
	
	@EJB(name = "it.polimi.db2.services/MarketingQuestionsService")
	private MarketingQuestionsService mService;
	
	@EJB(name = "it.polimi.db2.services/ProductService")
	private ProductService pService;
	
	public AddQuestionToQuestionnaire() {
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
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException { 
		boolean form_error = false; // Check variable for errors in form data
		String err_msg = null; // Error reporting message
		String path;
	
		int qId;
		String marketingQuestion;
		
		LocalDate today = LocalDate.now();
		Questionnaire questionnaire = null;
		List<Product> products = null;
		products = pService.findAllProducts();
		List<Questionnaire> availableQuestionnaires = null;
		availableQuestionnaires = qService.findAllFromToday();
		try {
			// Get and escape parameters
			qId = Integer.parseInt(request.getParameter("qId"));
			marketingQuestion = StringEscapeUtils.escapeJava(request.getParameter("marketingQuestion"));
			// Check for valid questionnaire and marketin question
			boolean validQuestionnaire = false;
			for(int i=0; i<availableQuestionnaires.size();i++)
				if (availableQuestionnaires.get(i).getId() == qId)
					validQuestionnaire = true;
					
			if (marketingQuestion == null || marketingQuestion.equals("")){
				form_error = true;
				err_msg = "You have to write a question!";
			}else if(!validQuestionnaire) {
				form_error = true;
				err_msg = "You have to choose the current or a posterior date";
			}
			
			
			ServletContext servletContext = getServletContext();
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());						
			ctx.setVariable("today", java.sql.Date.valueOf(today));
			ctx.setVariable("page", "0");
			ctx.setVariable("availableQuestionnaires", availableQuestionnaires);
			ctx.setVariable("products", products);
			
			path = "/AdminPanel.html";
			if (form_error)
				ctx.setVariable("${addQuestionErrorMsg}", err_msg);
			else {
				String successAddMsg = null;
				questionnaire = qService.findById(qId);
				mService.createQuestion(questionnaire, marketingQuestion);
				successAddMsg = "The question has been added to the questionnaire!";
				ctx.setVariable("successAddQuestionMsg", successAddMsg);
			}
			templateEngine.process(path, ctx, response.getWriter());
		} catch (Exception e) {
			// for debugging only e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());			
			return;
		}
	}	
	
	public void destroy() {
	}
	
}

