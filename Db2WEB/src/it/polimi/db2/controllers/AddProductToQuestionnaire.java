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

import it.polimi.db2.entities.Product;
import it.polimi.db2.entities.Questionnaire;
import it.polimi.db2.services.ProductService;
import it.polimi.db2.services.QuestionnaireService;


@WebServlet("/AddProductToQuestionnaire")
public class AddProductToQuestionnaire extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	
	@EJB(name = "it.polimi.db2.services/QuestionnaireService")
	private QuestionnaireService qService;
	@EJB(name = "it.polimi.db2.services/ProductService")
	private ProductService pService;
	
	public AddProductToQuestionnaire() {
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
		
		int pId;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate selectedDate;
		
		LocalDate today = LocalDate.now();
		List<Product> products = null;
		products = pService.findAllProducts();
		Questionnaire questionnaire = null;
		List<Questionnaire> availableQuestionnaires = null;
		availableQuestionnaires = qService.findAllFromToday();
		
		try {
			// Get and escape parameters
			pId = Integer.parseInt(request.getParameter("productId"));
			selectedDate = LocalDate.parse(StringEscapeUtils.escapeJava(request.getParameter("questionnaireDate")),formatter);
			
			// Check for valid date and product id			
			if (today.isAfter(selectedDate)){
				form_error = true;
				err_msg = "You have to choose the current or a posterior date";
			}else if(pService.findById(pId)==null) {
				form_error = true;
				err_msg = "The selected product does not exits.";
				pId=1;
			}
			
			ServletContext servletContext = getServletContext();
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());						
			
			// Re-set necessary variables for a correct and consistent page processing
			ctx.setVariable("today", java.sql.Date.valueOf(today));
			ctx.setVariable("page", "0");
			ctx.setVariable("products", products);
			ctx.setVariable("availableQuestionnaires", availableQuestionnaires);
			path = "/AdminPanel.html";
			if (form_error)
				ctx.setVariable("addProdErrorMsg", err_msg);
			else {
				String successAddMsg = null;
				// Find questionnaire based on input date by the user
				questionnaire = qService.findByDate(selectedDate);
				if (questionnaire == null) { // It's a new questionnaire -> create a new questionnaire
					questionnaire = qService.createQuestionnaire(selectedDate, pService.findById(pId));
					
					// Update the available questionnaires variable in order to have an updated list
					// (considering the one we just added)
					availableQuestionnaires = qService.findAllFromToday();
					successAddMsg = "Questionnarie for the current date created!";
					ctx.setVariable("availableQuestionnaires", availableQuestionnaires);
				}else { // Edit existing questionnaire's product
					qService.updateQuestionnaire(questionnaire.getId(), pId);
					successAddMsg = "The questionnaire's product has been updated!";
				}
				ctx.setVariable("successAddMsg", successAddMsg);
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
