package it.polimi.db2.controllers;

import java.io.IOException;

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

import it.polimi.db2.entities.User;
import it.polimi.db2.exceptions.UserExistsAlreadyException;
import it.polimi.db2.services.UserService;
import it.polimi.db2.utils.*;

@WebServlet("/SignIn")
public class SignIn extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	
	@EJB(name = "it.polimi.db2.services/UserService")
	private UserService usrService;
	
	public SignIn() {
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
		String err_msg = ""; 
		boolean form_error = false; // Check variable for errors in form data
		// Get and escape parameters
		String username = null;
		String password = null;
		String password_confirmation = null;
		String email = null;
		try {
			username = StringEscapeUtils.escapeJava(request.getParameter("username"));
			password = StringEscapeUtils.escapeJava(request.getParameter("pwd"));
			password_confirmation = StringEscapeUtils.escapeJava(request.getParameter("confirm_pwd"));
			email = StringEscapeUtils.escapeJava(request.getParameter("mail"));
			
			if (username == null || password == null || password_confirmation == null || email == null || username.isEmpty() ||
					password.isEmpty() || password_confirmation.isEmpty() || email.isEmpty()) 
			{
				form_error = true;
				err_msg = "Missing or empty required values. Please try again.";				
			}
			else if (!passwordCheck(password,password_confirmation)) {
				form_error = true;
				err_msg = "The passwords inserted don't match. Please try again.";				
			}			
			else if (!mailCheck(email)) {
				form_error = true;
				err_msg = "E-Mail is invalid. Please provide a valid one.";	
			}
			if (form_error) {
				String path;				
				ServletContext servletContext = getServletContext();
				final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
				ctx.setVariable("errorMsg", err_msg);
				ctx.setVariable("username_textbox", username);
				ctx.setVariable("pwd_textbox", password);
				ctx.setVariable("confirm_pwd_textbox", password_confirmation);
				ctx.setVariable("mail_textbox", email);
				path = "/SignIn.html";
				templateEngine.process(path, ctx, response.getWriter());
			}
			
		} catch (Exception e) {
			// for debugging only e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());			
			return;
		}
		
		if(!form_error) { // If no errors in form are detected, proceed to try creating the user
			User user = null;		
	
			try {
				user = usrService.createUser(username, password, email);
			}catch (UserExistsAlreadyException e) {
				err_msg = e.getMessage();
			}catch (Exception e) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Couldn't register the account with inserted data");
				return;
			}
			
			String path;
			if (user == null) {		
				ServletContext servletContext = getServletContext();
				final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
				ctx.setVariable("errorMsg", err_msg);
				path = "/SignIn.html";
				templateEngine.process(path, ctx, response.getWriter());
				} else {
				request.getSession().setAttribute("user", user);
				path = getServletContext().getContextPath() + "/Home";
				response.sendRedirect(path);
				}
			}			
		}	
	
	// Form data checks
	private boolean passwordCheck(String passoword, String password_confirmation) {
		return(passoword.equals(password_confirmation));
	}
	private boolean mailCheck(String mail) {
		return(RegistrationUtils.isValidEmail(mail));
	}
	
	public void destroy() {
	}
	
}
