<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | Writing Math Equations
	</jsp:attribute>
	<jsp:attribute name="content">
		<div class="centered">
			<div class="max-width-at-1000 align-left">
				<div class="box box-solid practice-problem">
					<div class="box-header">
						<i class="fa fa-info-circle"></i>
						<h3 class="box-title">Using Mathematics Expressions in CalcU</h3>
					</div>
					<div class="box-body">
						<h4>Using LaTex!</h4>
						<p>
							Creating mathematics expressions in CalcU is simple, whether you are making them in Comments, Answers, Questions, or Practice Problems,
							the format is the same for each, so it is easy to remember. Expressions are all based on a markup language called LaTex, which is a
							way that people around the world make all kinds of printed content look gorgeous. Listed below are the most common expressions that you
							might need, and the syntax for them.
						</p>
						<h4>Wrap your expressions</h4>
						<p>
							Whenever you are writing something that you want to be interpreted as math, and rendered in the LaTex format, you need to wrap it with special
							characters. There are two different "modes" of input, inline expressions and displayed equations (which are centered, on their own line). Inline expressions
							are used when writing mathematical expressions in the middle of text. Displayed equations are used when you wish the mathematical equation to stand alone.
							<ul>
								<li>To create an <b>inline expression</b>, begin the expression with <code>\</code><code>(</code>, write your expression in LaTex, and use <code>\</code><code>)</code> as a closing statement.</li>
								<li>To create a <b>displayed equation</b>, begin the expression with <code>\</code><code>[</code>, write your expression in LaTex, and use <code>\</code><code>]</code> to close the statement.</li>
								<li>Note that the inline Tex shorthand <code>$ ... $</code> has been intentionally disabled, in order to prevent accidental misuse (as it likely would cause). 
							</ul>
						<p>
						<h4>Stellar external resources</h4>
						<p> 
							Luckily the LaTex community is incredibly well supported. The equations listed below represent just a small portion of the vast number
							of possibilities with LaTex. The <a href="http://en.wikibooks.org/wiki/LaTeX/Mathematics">LaTex Wiki Mathematics page</a> has everything you might ever want to know, and if you want
							to see a bunch of examples, there is a stellar cookbook of LaTex recipes stored <a href="http://www.personal.ceu.hu/tex/cookbook.html">here</a>.
							Finally, another useful LaTex tool is the <a href="https://www.codecogs.com/latex/eqneditor.php">LaTex Calculator</a>, however this particular style of LaTex will not always render
							on this site. If you wish practice rendering LaTex on this site, go to our Create Practice Problem page and in the Problem Editor click this icon
							<a href="/contribute/practice-problem/new" class="btn-primary btn"> <i class="fa fa-magic"></i> </a> to get started.
						</p>	
					</div>
					<div class="box-body no-padding">
						<table class="table centered">
							<tbody>
								<tr>
									<td>Pattern</td>
									<td>Result</td>
								</tr>
								<c:forEach items="${latexExamples}" var="example">
									<tr>
										<td>${example}</td>
										<td>$$ ${example} $$</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</jsp:attribute>	
</t:genericpage>
	