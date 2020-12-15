package it.polimi.db2.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.sql.Date;
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
@WebServlet("/AdminPanel")
public class GoToAdminPanel extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	@EJB(name = "it.polimi.db2.services/QuestionnaireService")
	private QuestionnaireService qService;
	@EJB(name = "it.polimi.db2.services/ProductService")
	private ProductService pService;
	
	public GoToAdminPanel() {
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
		path =  "/WEB-INF/index.html";	
		if (!session.isNew() || !(session.getAttribute("user") == null))  {
			path =  "/WEB-INF/Home.html";	
			User loggedUser = (User)session.getAttribute("user");
			if (loggedUser.getRole() == 1) {
				int page_val = -1;
				String page = StringEscapeUtils.escapeJava(request.getParameter("page"));		
				if (page!=null) {
					page_val =  Integer.parseInt(page);							
				}
				path = "/WEB-INF/AdminPanel.html";
				LocalDate today = LocalDate.now();
				ctx.setVariable("today", Date.valueOf(today));
				if (page_val==0) {
					ctx.setVariable("page", "0");
					List<Product> products = null;
					products = pService.findAllProducts();		
					ctx.setVariable("products", products);
					List<Questionnaire> questionnaires = null;
					questionnaires = qService.findAllFromToday();
					ctx.setVariable("availableQuestionnaires", questionnaires);
				}else if (page_val==1) {
					ctx.setVariable("page", "1");
				}else if (page_val==2) {
					ctx.setVariable("page", "2");
				}			
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
