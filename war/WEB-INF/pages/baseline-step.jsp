<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | Baseline
	</jsp:attribute>

	<jsp:attribute name="content">
		<div class="centered">
			<form class="centered margin-top-100 max-width-at-1000" action="/baseline" method="post">
				<h2 class="welcome-banner margin-top-100 margin-bottom-50">Baseline Step #${stepNumber}</h2>
				<h3 class="welcome-banner-subtitle margin-bottom-50">How confident are you that you could answer this problem? </h3>
				<input type="hidden" name="problemUuid" value="${content.uuid}"/>
				<input type="hidden" name="stepNumber" value="${stepNumber}"/>
				<h2>${content.body}</h2>
				<div class="rating-buttons margin-top-100 max-width-at-575">
					<button class="btn btn-block btn-danger" name="diff0"><h4>Never Seen This Material</h4></button>
					<button class="btn btn-block btn-danger" name="diff1"><h4>Very Challenging</h4></button>
					<button class="btn btn-block btn-warning" name="diff2"><h4>Challenging</h4></button>
					<button class="btn btn-block btn-warning" name="diff3"><h4>Average difficulty</h4></button>
					<button class="btn btn-block btn-warning" name="diff4"><h4>Fairly Easy</h4></button>
					<button class="btn btn-block btn-success" name="diff5"><h4>Too Easy</h4></button>
					<button class="btn btn-block btn-success" name="diff6"><h4>Already Know This Material</h4></button>
				</div>
			</div>
		</div>
	</jsp:attribute>
</t:genericpage>