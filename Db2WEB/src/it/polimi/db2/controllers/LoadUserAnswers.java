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

import it.polimi.db2.entities.MarketingAnswers;
import it.polimi.db2.entities.Questionnaire;
import it.polimi.db2.entities.QuestionnaireUserAnswers;
import it.polimi.db2.services.MarketingAnswersService;
import it.polimi.db2.services.QuestionnaireService;
import it.polimi.db2.services.QuestionnaireUserAnswersService;


@WebServlet("/LoadUserAnswers")
public class LoadUserAnswers extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	
	@EJB(name = "it.polimi.db2.services/QuestionnaireService")
	private QuestionnaireService qService;
	@EJB(name = "it.polimi.db2.services/QuestionnaireUserAnswersService")
	private QuestionnaireUserAnswersService quaService;
	
	public LoadUserAnswers() {
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
		String path;
		
		int pqUAId;
		List<Questionnaire> pastQuestionnaires = null;
		pastQuestionnaires = qService.findPast();
		QuestionnaireUserAnswers selectedQuestionnaireUserAnswer;
		
		try {
			// Get and escape parameters
			
			// Specific QuestionnaireUserAnswer of the user of which we want to read the answers
			pqUAId = Integer.parseInt(request.getParameter("pqUAId"));
			
			
			// Get the selected questionnaire
			selectedQuestionnaireUserAnswer = quaService.findById(pqUAId);
			
			Questionnaire selectedQuestionnaire = selectedQuestionnaireUserAnswer.getQuestionnaire();
			ServletContext servletContext = getServletContext();
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());		
			
			// Set all the necessary variables to have consistency with the page processing
			ctx.setVariable("page", "1");
			ctx.setVariable("pastQuestionnaires", pastQuestionnaires);
			ctx.setVariable("pastQuestionnaireSelectedId",selectedQuestionnaire.getId());
			ctx.setVariable("selectedQuestionnaireUserAnswer", selectedQuestionnaireUserAnswer);
			ctx.setVariable("selectedQuestionnaireAnswerId", selectedQuestionnaireUserAnswer.getId());
			List<QuestionnaireUserAnswers> qUA = quaService.findByQuestionnaire(selectedQuestionnaire.getId());
			ctx.setVariable("usersWithQuestionnaire", qUA);
			List<QuestionnaireUserAnswers> qUACancelled = quaService.findByQuestionnaireCancelled(selectedQuestionnaire.getId());
			ctx.setVariable("usersWithCancelledQuestionnaire",qUACancelled);
			
			path = "/AdminPanel.html";
			
			// Load user Marketing Answers
			//List<MarketingAnswers> mA = maService.findByQuestionnaireUserAnswer(pqUAId);
			List<MarketingAnswers> mA = selectedQuestionnaireUserAnswer.getMarketingAnswers();
			ctx.setVariable("marketingAnswers", mA);
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
