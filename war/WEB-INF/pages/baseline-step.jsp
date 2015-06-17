<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | Baseline
	</jsp:attribute>

	<jsp:attribute name="content">
		<form class="centered margin-top-100" action="/baseline" method="post">
			<input type="hidden" name="problemUuid" value="${question.uuid}"/>
			<input type="hidden" name="stepNumber" value="${stepNumber}"/>
			<h4>How confident are you that you could answer this problem?</h4>
			<h2>${question.body}</h2>
			<div class="rating-buttons">
				<button class="btn bg-red-gradient" name="diff1"><h4>I don't Understand at All</h4></button>
				<button class="btn bg-orange" name="diff2"><h4>This is a Difficult Problem</h4></button>
				<button class="btn bg-yellow-gradient" name="diff3"><h4>It was not easy for me to Solve this</h4></button>
				<button class="btn bg-green-gradient" name="diff4"><h4>I can solve problems like this</h4></button>
				<button class="btn bg-light-blue-gradient" name="diff5"><h4>This is too Easy</h4></button>
			</div>
		</div>
	</jsp:attribute>
</t:genericpage>