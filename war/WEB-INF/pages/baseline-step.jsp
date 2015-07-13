<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | Baseline
	</jsp:attribute>

	<jsp:attribute name="content">
		<form class="centered margin-top-100" action="/baseline" method="post">
			<input type="hidden" name="problemUuid" value="${content.uuid}"/>
			<input type="hidden" name="stepNumber" value="${stepNumber}"/>
			<h4>How confident are you that you could answer this problem?</h4>
			<h2>${content.body}</h2>
			<div class="rating-buttons">
				<button class="btn bg-red-gradient" name="diff1"><h4>Very Challenging</h4></button>
				<button class="btn bg-orange" name="diff2"><h4>Challenging</h4></button>
				<button class="btn bg-yellow-gradient" name="diff3"><h4>Average difficulty</h4></button>
				<button class="btn bg-green-gradient" name="diff4"><h4>Fairly Easy</h4></button>
				<button class="btn bg-light-blue-gradient" name="diff5"><h4>Too Easy</h4></button>
				
				<button class="btn bg-red-gradient" name="diff0"><h4>Never Seen This Material</h4></button>
				<button class="btn bg-red-gradient" name="diff6"><h4>Already Know This Material</h4></button>
			</div>
		</div>
	</jsp:attribute>
</t:genericpage>