package it.polimi.db2.filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet Filter implementation class LoggedIn
 */
@WebFilter("/LoggedIn")
public class LoggedIn implements Filter {

    /**
     * Default constructor. 
     */
    public LoggedIn() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{
		System.out.print("Login checker filter executing ...\n");

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		String loginpath = req.getServletContext().getContextPath() + "/index.html";
		String homepath = req.getServletContext().getContextPath() + "/Home";
		//String req_url = req.getRequestURI();	// get the URI requested by the user
		// Debug
		//System.out.println("Requested URL: " + req_url);
	
		HttpSession session = req.getSession();
		if (session.isNew() || session.getAttribute("user") == null) {	// If User is not logged yet...
				res.sendRedirect(loginpath);	// redirect to the login page
				return;
		}else if(session.getAttribute("user") != null)	// If User is already logged in...
		{	
				res.sendRedirect(homepath);	// redirect him to the homepage
				return;			
		}		
		// pass the request along the filter chain
		chain.doFilter(request, response);	
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
