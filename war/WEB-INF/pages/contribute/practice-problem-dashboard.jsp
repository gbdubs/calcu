<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | Practice Problem Dashboard
	</jsp:attribute>
	<jsp:attribute name="content">
		<div class="box box-warning">
			<div class="box-header">
				<i class="fa fa-warning"></i>
				<h3 class="box-title">Unfinished/Unsubmitted Problems<small>To open a problem up for editing, or to submit it, click on its link below</small></h3>
			</div>
			<div class="box-body">
			<c:forEach items="${unsubmittedPracticeProblems}" var="practiceProblem">
				
				<a href="${practiceProblem.editUrl}"> ${practiceProblem.problemTitle} </a>
				<br>
				
			</c:forEach>
			</div>
			<div class="box-footer">
				Total: ${fn:length(unsubmittedPracticeProblems)} Unsubmitted Problems
			</div>
		</div>
	</jsp:attribute>
</t:genericpage>
