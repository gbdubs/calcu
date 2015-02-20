package calculus.recommendation;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class DifferentiatingContent {
	private static Map<String, String> getContentDefinition(int i){
		// Since we have only implemented 3 thus far, only return real results.
		i = i % 3;
		
		Map<String, String> contentDefinition = new HashMap<String, String>();
		
		String char1 = "a";
		String title1 = "";
		String body1 = "";
		String author1 = "";
		String karma1 = "";
		String char2 = "A";
		String title2 = "";
		String body2 = "";
		String author2 = "";
		String karma2 = "";
		
		if (i == 0){
			char1 ="a";
			char2 ="A";
			
			title1 = "Chain Rule Explanation";
			body1 = "<p>In calculus, the chain rule is a formula for computing the derivative of the composition of two or more functions. That is, if f and g are functions, then the chain rule expresses the derivative of their composition f ∘ g (the function which maps x to f(g(x))) in terms of the derivatives of f and g and the product of functions as follows:</p>" +
					"$$ (f\\circ g)'=(f'\\circ g)\\cdot g'. $$" +
					"The chain rule can also be written with a different notation for function composition (though still in Lagrange's notation for differentiation). The meaning is identical." + 
					
					"$$ (f(g(x)))' = f'(g(x)) g'(x) $$" +
					"The chain rule may be written, in Leibniz's notation, in the following way. We consider z to be a function of the variable y, which is itself a function of x (see dependent variable), and so, z becomes a function of x as well:" +
					
					"$$ \\frac{dz}{dx} = \\frac{dz}{dy} \\cdot \\frac{dy}{dx}. $$" +
					"In integration, the counterpart to the chain rule is the substitution rule.";
			
			title2 = "Chain Rule Example";
			body2 = "<p>Let \\(f(x)=6x + 3\\) and \\(g(x)=-2x+5\\).  Use the chain rule to calculate \\(h'(x)\\), where \\(h(x)=f(g(x))\\).</p>" +

					"<p><b>Solution</b>:  The derivatives of \\(f\\) and \\(g\\) are "+
					"\\begin{align*}"+
					"  f'(x)&=6\\\\"+
					"  g'(x)&=-2."+
					"\\end{align*}"+
					"According to the chain rule, "+
					"\\begin{align*}"+
					"  h'(x) &= f'(g(x)) g'(x)\\\\"+
					"  &= f'(-2x +5) (-2)\\\\"+
					"  &= 6 (-2)=-12."+
					"\\end{align*}"+
					"</p>"+
					
					"<p>Since the functions were linear, this example was trivial.  Even though we had to evaluate \\(f'\\) at \\(g(x)=-2x+5\\), that didn't make a difference since \\(f'=6\\) not matter what its input is.  Moveover, in this case, if we calculate \\(h(x)\\),"+
					"\\begin{align*}"+
					"  h(x) &= f(g(x))\\\\"+
					"  &= f(-2x+5)\\\\"+
					"  &= 6(-2x+5)+3\\\\"+
					"  &= -12x+30+3 = -12x + 33,"+
					"\\end{align*}"+
					"then we can quite easily calculate its derivative directly to obtain that \\(h'(x)=-12\\).</p>";
		} else if (i == 1) {
			char1 ="b";
			char2 ="B";
			
			title1 = "Special Solution to \\(\\int \\sin ^2 (x)\\)";
			body1 = "<p>Step 1"+
					"</p><p>Use the half angle formula, \\(sin^2(x) = 1/2*(1 - cos(2x))\\) and substitute into the integral so it becomes 1/2 times the integral of \\((1 - cos(2x))\\) dx."+
					"</p><p>Step 2"+
					"</p><p>Set \\(u = 2x\\) and \\(du = 2dx\\) to perform u substitution on the integral. Since \\(dx = du/2\\), the result is 1/4 times the integral of \\((1 - cos(u))\\) du."+
					"</p><p>Step 3"+
					"</p><p>Integrate the equation. Since the integral of \\(1du\\) is \\(u\\), and the integral of \\(cos(u) du\\) is \\(sin(u)\\), the result is \\(1/4*(u - sin(u)) + c\\)."+
					"</p><p>Step 4"+
					"</p><p>Substitute u back into the equation to get \\(1/4*(2x - sin(2x)) + c\\). Simplify to get \\(x/2 - (sin(x))/4 + c\\)."+
					"</p>";
			
			title2 = "General Solution to \\(\\int \\sin ^2 (x)\\)";
			body2 = "$$\\int\\sin^2(x)dx=\\int \\sin(x)\\sin(x)dx=-\\sin(x)\\cos(x)-\\int -\\cos(x)\\cos(x)dx=$$"+
					"$$=-\\sin(x)\\cos(x)\\stackrel{\\text{not 'minus'}}{+}\\int\\cos^2(x)dx=-\\sin(x)\\cos(x)+\\int 1-\\sin^2(x)dx=$$"+
					"$$=-\\sin(x)\\cos(x)+\\int 1dx -\\int\\sin^2(x)dx=-\\sin(x)\\cos(x)+x-\\int\\sin^2(x)dx$$";
		} else if (i == 2) {
			char1 ="c";
			char2 ="C";
			
			title1 = "Explanation With Equations";
			body1 = "<p>The geometric meaning of the derivative \\(f'(x) = \\frac{df(x)}{dx}\\) is the slope of the line tangent to \\(y=f(x)\\) at x.</p>"+
					"<p>The <B>secant</B> line through P and Q has slope \\(\\frac{f(x+\\Delta x)-f(x)}{(x+\\Delta x)-x}=\\frac{f(x+\\Delta x)-f(x)}{\\Delta x}.\\)</p>"+
					"<p>We can approximate the <B>tangent</B> line through P by moving Q towards P, decreasing \\(\\Delta x\\).  In the limit as \\(\\Delta x \\to 0 \\), we get the tangent line through P with slope \\(\\lim_{\\Delta x\\to 0} \\frac{f(x+\\Delta x)-f(x)}{\\Delta x}.\\) We define \\(f'(x)=\\lim_{\\Delta x\\to 0}\\frac{f(x+\\Delta x)-f(x)^{\\small\\textrm{*}}}{\\Delta x}.\\)</p>"+
					"<p>\\(^*\\) If the limit as \\(\\Delta x \\to 0\\) at a particular point does not exist, \\(f'(x)\\) is undefined at that point.</p>"+
					"<p>We derive all the basic differentiation formulas using this definition. </p>";
			
			title2 = "Conceptual Explanation";
			body2 = "<p>The derivative measures the steepness of the graph of a function at some particular point on the graph. Thus, the derivative is a slope. (That means that it is a ratio of change in the value of the function to change in the independent variable.)"+
					"</p><p>If the independent variable happens to be 'time', we often think of this ratio as a rate of change (an example is velocity)"+
					"</p><p>If we zoom in on the graph of the function at some point so that the function looks almost like a straight line, the derivative at that point is the slope of the line. This is the same as saying that the derivative is the slope of the tangent line to the graph of the function at the given point."+
					"</p><p>The slope of a secant line (line connecting two points on a graph) approaches the derivative when the interval between the points shrinks down to zero."+
					"</p><p>The derivative is also, itself, a function: it varies from place to place. For example, the velocity of a car may change from moment to moment as the driver speeds up or slows down.</p>";

		} else if (i == 3) {
			
		} else if (i == 4) {
			
		} else if (i == 5) {
			
		} else if (i == 6) {
			
		} else if (i == 7) {
			
		} else if (i == 8) {
			
		} else if (i == 9) {
			
		} else {
			throw new RuntimeException("Higher Differentiating Content Beyond 10 Not supported.");
		}
		
		// To right-left randomize
		int rightLeftRandomization = (int) (Math.random() * 2);
		
		if (rightLeftRandomization == 0){
			contentDefinition.put("comparisonChar1", char1);
			contentDefinition.put("comparisonChar2", char2);
			contentDefinition.put("comparisonBody1", body1);
			contentDefinition.put("comparisonBody2", body2);
			contentDefinition.put("comparisonTitle1", title1);
			contentDefinition.put("comparisonTitle2", title2);
			contentDefinition.put("comparisonAuthor1", author1);
			contentDefinition.put("comparisonAuthor2", author2);
			contentDefinition.put("comparisonKarma1", karma1);
			contentDefinition.put("comparisonKarma2", karma2);
		} else {
			contentDefinition.put("comparisonChar1", char2);
			contentDefinition.put("comparisonChar2", char1);
			contentDefinition.put("comparisonBody1", body2);
			contentDefinition.put("comparisonBody2", body1);
			contentDefinition.put("comparisonTitle1", title2);
			contentDefinition.put("comparisonTitle2", title1);
			contentDefinition.put("comparisonAuthor1", author2);
			contentDefinition.put("comparisonAuthor2", author1);
			contentDefinition.put("comparisonKarma1", karma2);
			contentDefinition.put("comparisonKarma2", karma1);
		}
		
		return contentDefinition;
	}
	
	public static void placeContentDefinitionIntoRequest(HttpServletRequest req, int i){
		Map<String, String> map = getContentDefinition(i);
		for(String s : map.keySet()){
			req.setAttribute(s, map.get(s));
		}
	}
}
