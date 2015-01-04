<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | Writing Math Equations
	</jsp:attribute>
	<jsp:attribute name="content">
		<div class="box box-solid practice-problem">
			<div class="box-header">
				<i class="fa fa-info-circle"></i>
				<h3 class="box-title">Using Mathematics Expressions in CalcU</h3>
			</div>
			<div class="box-body">
				<h5>Using LaTex!</h5>
				<p>
					Creating mathematics expressions in CalcU is simple, whether you are making them in Comments, Answers, Questions, or Practice Problems,
					the format is the same for each, so it is easy to remember. Expressions are all based on a markup language called LaTex, which is a
					way that people around the world make all kinds of printed content look gorgeous. Listed below are the most common expressions that you
					might need, and the syntax for them.
				</p>
				<h5>Wrap your expressions</h5>
				<p>
					Whenever you are writing something that you want to be interpreted as math, and rendered in the LaTex format, you need to wrap it with special
					characters.  HERE IS THE THING: http://en.wikibooks.org/wiki/LaTeX/
					http://www.personal.ceu.hu/tex/cookbook.html
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
	</jsp:attribute>	
</t:genericpage>
	