package calculus.contribute;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.UserContextAPI;

@SuppressWarnings("serial")
public class LaTexIntroductionServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		
		// This is the list of expressions that will be shown on the LaTex introduction page.
		// A simple static list is the easiest way to go about this, and doesn't really have any
		// adverse impacts.
		
		String[] expressions = {
			"x^2 - 3x - 4 = (x - 4)(x + 1)",
			"{base} ^ {exponent1 + exponent2}",
			"\\frac {numerator} {denomenator}",
			"\\| x \\|",
			"\\int x \\, dx",
			"\\int_ 0 ^ \\infty",
			"\\alpha, A, \\beta, B, \\gamma, \\Gamma, \\pi, \\Pi, \\phi, \\varphi, \\Phi",
			"\\forall x \\in X, \\quad \\exists y \\leq \\epsilon",
			"\\cos (2\\theta) = \\cos^2 \\theta - \\sin^2 \\theta"
		};
		req.setAttribute("latexExamples", expressions);
		
		UserContextAPI.addUserContextToRequest(req, "/contribute/latex-introduction");
		resp.setContentType("text/html");
		RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/contribute/latex-introduction.jsp");
		jsp.forward(req, resp);
	}
}
