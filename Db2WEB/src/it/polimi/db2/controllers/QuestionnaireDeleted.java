package it.polimi.db2.controllers;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;

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

import it.polimi.db2.entities.Questionnaire;
import it.polimi.db2.entities.User;
import it.polimi.db2.services.QuestionnaireService;
import it.polimi.db2.services.QuestionnaireUserAnswersService;

@WebServlet("/QuestionnaireDeleted")
public class QuestionnaireDeleted extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	
	@EJB(name = "it.polimi.db2.services/QuestionnaireUserAnswersService")
	private QuestionnaireUserAnswersService service = new QuestionnaireUserAnswersService();
	
	@EJB(name = "it.polimi.db2.services/Questionnaire")
	private QuestionnaireService qService = new QuestionnaireService();
	
	
	private  User user;
	
	public QuestionnaireDeleted() {
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
			path = "/WEB-INF/AfterCancellation.html";
			user = (User)session.getAttribute("user");
		}

		

		templateEngine.process(path, ctx, response.getWriter());
	}
	
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	
		doGet(request, response);
		
		LocalDate date = LocalDate.now();
		Questionnaire questionnaire = qService.findByDate(date);
		
		Timestamp timestamp = Timestamp.valueOf(date.atStartOfDay());
		 
		
		service.deleteAnswer(user, questionnaire, timestamp);
	}
	
	public void destroy() {
	}
	
}
