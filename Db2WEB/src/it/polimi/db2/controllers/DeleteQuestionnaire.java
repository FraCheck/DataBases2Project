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

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;


import it.polimi.db2.entities.Questionnaire;
import it.polimi.db2.services.QuestionnaireService;



@WebServlet("/DeleteQuestionnaire")
public class DeleteQuestionnaire extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	
	@EJB(name = "it.polimi.db2.services/QuestionnaireService")
	private QuestionnaireService qService;

	
	public DeleteQuestionnaire() {
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
	
		LocalDate today = LocalDate.now();

		try {
			// Get and escape parameters
			int pqId = Integer.parseInt(request.getParameter("pqId"));
			Questionnaire selectedQuestionnaire = qService.findById(pqId);
			
			LocalDate selectedDate = selectedQuestionnaire.getDate().toLocalDate();
			// Check for valid date	
			if (!selectedDate.isBefore(today)){
				form_error = true;
				err_msg = "You have to choose a precedent date";
			}
			
			ServletContext servletContext = getServletContext();
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());						
			ctx.setVariable("page", "2");
			
			path = "/AdminPanel.html";
			
			if (form_error)
				ctx.setVariable("deleteDateSelectionError", err_msg);
			else {
				qService.deleteQuestionnaire(pqId);
				List<Questionnaire> pastQuestionnaires = qService.findPast();
				ctx.setVariable("pastQuestionnaires", pastQuestionnaires);
				ctx.setVariable("successDeleteMsg", "The questionnaire and its data has been successfully deleted!");
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
