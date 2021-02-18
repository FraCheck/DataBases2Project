package it.polimi.db2.controllers;

	import java.io.IOException;

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
	import it.polimi.db2.services.*;

	@WebServlet("/Back")
	public class GoBack extends HttpServlet {
		private static final long serialVersionUID = 1L;
		private TemplateEngine templateEngine;
		
		@EJB(name = "it.polimi.db2.services/MarketingQuestionsService")
		private MarketingQuestionsService questionService;
		
		@EJB(name = "it.polimi.db2.services/QuestionnaireService")
		private QuestionnaireService questionnaireService;
		
		@EJB(name = "it.polimi.db2.services/QuestionnaireSUserAnswersService")
		private QuestionnaireUserAnswersService service;
		
		public GoBack() {
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
				
				System.out.println(questions_done);
				
					path = "/WEB-INF/Questionnaire.html";
					
					String[] list = null;
					try {
						list = (String[]) request.getSession().getAttribute("questionsList");
						String current_question = list[questions_done];
						ctx.setVariable("question", current_question );
					}catch(Exception e) {
						System.out.println("Could't get questions");
						System.out.println(e);
					}
						
			}
					

			templateEngine.process(path, ctx, response.getWriter());
		}

		protected void doPost(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
				
				int done = (int) request.getSession().getAttribute("questions_done");
				
                done = done -1;
				
				request.getSession().setAttribute("questions_done", done);
				
				System.out.println(request.getSession().getAttribute("questions_done"));
				
				doGet(request, response);
				
		}

		public void destroy() {
		}

	}
	
