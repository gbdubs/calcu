package calculus.recommendation;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class DifferentiatingContent {
	private static Map<String, String> getContentDefinition(int i){
		// Since we have only implemented 13 thus far, only return real results.
		i = i % 13;
		
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
			char1 = "d";
			char2 = "D";
			
			title1 = "Drawing Conclusions";
			body1 = "<p>The three statements given below are each true. What conclusion can be found to be true?</p>"+
					"<p>1. If Rachael joins the choir then Rachael likes to sing.</p>"+
					"<p>2. Rachael will join the choir or Rachael will play basketball.</p>"+
					"<p>3. Rachael does not like to sing.</p>"+
					"<p><B>Solution</B>: A conditional and its contrapositve are logically equivalent. Therefore, If 'Rachael does not like to sing, then Rachael will not join the choir' is true.</p>"+
					"<p>By the Law of Detachment, when the hypothesis of a true contitional is true, the conclusion must be true. Therefore, since 'Rachael does not like to sing' is true, 'Rachael will not join the choir' must be true.</p>"+
					"<p>Since 'Rachael will not join the choir' is true, then its negation, 'Rachael will join the choir,' must be false.</p>"+
					"<p>Since 'Rachael will join the choir or Rachael will play basketball' is true and 'Rachael will join the choir' is false, then by the Law of Disjunctive Inference, 'Rachael will play basketball' must be true.</p>";
			
			title2 = "Alternate Method of Drawing Conlusions";
			body2 = "<p>Let c represent 'Rachael joins the choir,'</p>"+
					"<p>s represent 'Rachael likes to sing,'</p>"+
					"<p>and b represent 'Rachael will play basketball.'</p>"+
					"<p>Write statements 1, 2, and 3 in symbols:</p>"+
					"<p>1. \\(c \\rightarrow s\\)  2. \\(c \\vee b\\)  3. \\(\\sim s\\). </p>"+
					"<p>Using Statement 1: \\(c \\rightarrow s\\) is true, so \\(\\sim s \\rightarrow \\sim c\\) is true.</p>"+
					"<p>Using Statement 3: \\(\\sim s\\) is true and \\(\\sim s \\rightarrow \\sim c\\) is true, so \\(\\sim c\\) is true. Also, c is false.</p>"+
					"<p>Using Statement 2: \\(c \\vee b\\) is true and c is false, so b must be true.</p>"+
					"<p>Answer: Rachael does not join the choir. Rachael will play basketball.</p>";
			
		} else if (i == 4) {
			char1 = "e";
			char2 = "E";
			
			title1 = "Biconditional Statement";
			body1 = "<p>Every definition can be written in reverse order. Both of the following statements are true:</p>"+
					"<p>\\(\\bullet\\) Congruent segments are segments that have the same measure.</p>"+
					"<p>\\(\\bullet\\) Line segments that have the same measure are congruent.</p>"+
					"<p>We can restate the definition as two true conditionals:</p>"+
					"<p>\\(\\bullet\\) If two line segments are congruent, then they have the same measure.</p>"+
					"<p>\\(\\bullet\\) If two line segments have the same measure, then they are congruent.</p>"+
					"<p>Therefore, this definition can be restated as a true biconditional:</p>"+
					"<p>\\(\\bullet\\) Two line segments are congruent if and only if they have the same measure.</p>";
			
			title2 = "Biconditional Example";
			body2 = "<p>We know that when we add the same number to both sides of an equation or when we multiply both sides of an equation or when we multiply both sides of an equation by the same number, the derived equation"
					+ "has the same solution set as the given equation. That is, any number that makes the first equation true will make the derived equation true.</p>"+
					"<p>For example:</p>"+
					"$$p: 3x + 7 = 19$$"+
					"$$q: 3x = 12$$"+
					"$$p \\rightarrow q: If \\,3x + 7 = 19, then \\,3x = 12.$$"+
					"$$q \\rightarrow p: If \\,3x = 12, then \\,3x + 7 = 19.$$"+
					"<p> When \\(x = 4\\), both p and q are true and both \\(p \\rightarrow q\\) and \\(q \\rightarrow p\\) are true.</p>"+
					"<p> When \\(x = 1\\) or when x equals any number other than 3, both p and q are false and both \\(p \\rightarrow q\\) and \\(p \\rightarrow q\\) are true.</p>"+
					"<p> Therefore the biconditional \\(3x + 7 = 19\\) if and only if \\(3x = 12\\) is true.</p>"+
					"<p> The solution of an equation is a series of biconditionals:</p>"+
					"$$3x + 7 = 19$$"+
					"$$3x = 12$$"+
					"$$x = 4$$";
			
		} else if (i == 5) {
			char1 = "f";
			char2 = "F";
			
			title1 = "Verbal Expressions";
			body1 = "<p>\\(\\bullet\\) add 3 to a number</p>"+
					"<p>\\(\\bullet\\) a number plus 3</p>"+
					"<p>\\(\\bullet\\) the sum of a number and 3</p>"+
					"<p>\\(\\bullet\\) 3 more than a number</p>"+
					"<p>\\(\\bullet\\) a number increased by 3</p>";
			
			title2 = "Variable Expression";
			body2 = "<p>$$n + 3$$</p>";
			
		} else if (i == 6) {
			char1 = "g";
			char2 = "G";
			
			title1 = "Percentage Visual";
			body1 = "<img src=\"/_static/img/differentiating-content/method1.png\">"+
					"<p>\\(100\\% \\div 5 = 20\\%\\) per box</p>"+
					"<p>So \\(20\\% \\times 3 = 60\\%\\)</p>";
			
			title2 = "Percentage Example";
			body2 = "<p>$$\\frac{3}{5} = \\frac{x}{100%}$$</p>"+
					"<p>$$3 \\cdot 100 = 5 \\cdot x$$</p>"+
					"<p>$$300 = 5 \\cdot x$$</p>"+
					"<p>$$x = 60%$$</p>";
			
			
		} else if (i == 7) {
			char1 = "h";
			char2 = "H";
			
			title1 = "Whole Deduction";
			body1 = "<p>Since \\(6\\) apples equal \\(30\\%\\), each apple is \\(5\\%\\). \\(5 \\times 20 = 100\\), so there are \\(20\\) apples.</p>";
			
			title2 = "Whole Example";
			body2 = "<p>$$\\frac{6}{x} = \\frac{30%}{100%}$$</p>"+
					"<p>$$6 \\cdot 100 = 30 \\cdot x$$</p>"+
					"<p>$$600 = 30 \\cdot x$$</p>"+
					"<p>$$x = 20$$</p>";
			
		} else if (i == 8) {
			char1 = "i";
			char2 = "I";
			
			title1 = "Algebra Visual";
			body1 = "<img src=\"/_static/img/differentiating-content/method2.png\">";
			
			title2 = "Algebraic Equation";
			body2 = "<p>$$m + 9 = 4m + 3$$</p>"+
					"<p>$$-3$$</p>"+
					"<p>$$m + 6 = 4m$$</p>"+
					"<p>$$-m$$</p>"+
					"<p>$$   6  = 3m$$</p>"+
					"<p>$$ \\frac{6}{3} = \\frac{3m}{3}$$</p>"+
					"<p>$$   2  =  m$$</p>";
			
		} else if (i == 9) {
			char1 = "j";
			char2 = "J";
			
			title1 = "Proportions Vertically";
			body1 = "<p>Are these two ratios equivalent?</p>"+
					"<p>$$\\frac{3}{6} = \\frac{4}{8}$$</p>"+
					"<p>Since the numerator and denominator are related (by multiplying or dividing by 2), we know these two ratios are equivalent</p>";
			
			title2 = "Proportions Using Cross-Products";
			body2 = "<p>Are these two ratios equivalent?</p>"+
					"<p>$$\\frac{6}{9} = \\frac{8}{12}$$</p>"+
					"<p>$$6 \\times 12 = 9 \\times 8$$</p>"+
					"<p>$$72 = 72$$</p>"+
					"<p>Since the cross-products are equal to each other, the two ratios are equivalent.</p>";
		} else if (i == 10){
			char1 = "k";
			char2 = "K";
			
			title1 = "Shorter Solution";
			body1 = "<p>Task: AB is a dimater on circle with center O. D and E are points on circle O so that DO||EB. C is the intersection point of AD and BE. "
					+ "Prove that CB = AB</p>"+
					"<img src=\"/_static/img/differentiating-content/method3.png\">"+
					"<p>Solution: \\(DO = \\frac{1}{2}AB\\) (Equal radiuses in a circle) \\(\\Rightarrow DO\\) is a midline in triangle ABC (parallel to BC and bisecting AB) \\(\\Rightarrow DO = \\frac{1}{2}AB = \\frac{1}{2}BC \\Rightarrow AB = BC\\)</p>";
					
			title2 = "Long Solution";
			body2 = "<p>Task: AB is a dimater on circle with center O. D and E are points on circle O so that DO||EB. C is the intersection point of AD and BE. Prove that CB = AB</p>"+
					"<img src=\"/_static/img/differentiating-content/method3.png\">"+
					"<p>Solution: Auxiliary construction: continue DO till point F so that DF is a diameter. Draw the line FB."
					+ "DO = AO (Equal radiuses in a circle) \\(\\Rightarrow \\angle ABC = \\angle AOD\\) (Based angles in an isosceles triangle)"
					+ "\\(\\angle F = \\angle A\\) (Inscribed angles that subtend the same are) \\(\\Rightarrow\\)"
					+ "\\(\\angle F = \\angle ADO \\Rightarrow CD||BF\\) (equal alternate angles)"
					+ "DFBC is a parallelgram (2 pairs of parallel sides)\\(\\Rightarrow\\)"
					+ "DF = CB (opposite sides of a parallelogram), DF = AB (diameters) \\(\\Rightarrow\\) AB = BC</p>";
		} else if (i == 11) {
			char1 = "l";
			char2 = "L";
			
			title1 = "Numerical Associative Property";
			body1 = "<p>$$8 \\times (4 \\times 2) = (8 \\times 4) \\times 2$$</p>"+
					"<p>$$8 \\times (8) = 32 \\times 2$$</p>"+
					"<p>$$64 = 64$$</p>";
			
			title2 = "Variable Associative Property";
			body2 = "<p>$$a(bc) = (ab)c$$</p>";
		
		} else if (i == 12) {
			char1 = "m";
			char2 = "M";
			
			title1 = "Solving Quadratic Equations Steps";
			body1 = "<p>A quadratic equation is an equation that could be written as \\(Ax^{2} + Bx + C = 0\\). To solve a quadratic equation,</p>"+
					"<p>1. Put all terms on one side of the equal sign, leaving zero on the other side.</p>"+
					"<p>2. Factor.</p>"+
					"<p>3. Set each factor equal to zero.</p>"+
					"<p>4. Solve each of these equations.</p>"+
					"<p>5. Check by inserting your answer in the original equation.</p>";
			
			title2 = "Solving Quadratic Equations Example";
			body2 = "<p>Solve for x: </p>"+
					"<p>$$x^{2} - 6x = 16$$</p>"+
					"<p>$$x^{2} - 6x = 16 \\;becomes \\;x^{2} - 6x - 16 = 0$$</p>"+
					"<p>$$Factoring, (x - 8)(x + 2) = 0$$</p>"+
					"<p>$$x - 8 = 0 \\;or \\;x + 2 = 0$$</p>"+
					"<p>$$x = 8  x = -2$$</p>"+
					"<p>$$To \\,check, \\,(8)^{2} - 6(8) = 16 \\;or \\;(-2)^{2} - 6(-2) = 16$$</p>"+
					"<p>$$64 - 48 = 16$$</p>"+
					"<p>$$or$$</p>"+ 
					"<p>$$4 + 12 = 16$$</p>"+
					"<p>$$16 = 16$$</p>";
		
		} else {
			throw new RuntimeException("Higher Differentiating Content Beyond 13 Not supported.");
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
