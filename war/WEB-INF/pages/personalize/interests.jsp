<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | Personalize Interests
	</jsp:attribute>

	<jsp:attribute name="content">
		
		<div class="box box-success box-solid">
			<div class="box-header">
				<i class="fa fa-refresh"></i>
				<h3 class="box-title">Interest Personalization</h3>
			</div>
			<div class="box-body">
				<p>
					Select all of the things which interest you (Grey with Black Text), and
					deselect all of the things you are no longer interested in (Green with White Text).
				<div class="row">
					<c:forEach items="${interests}" var="interest">
						<div class="interest-button-wrapper col-xs-6 col-sm-4 col-md-3 col-lg-2">
							<c:if test="${interest.value}">
								<button class="toggle-interest-button btn btn-success"
									data-interest="${interest.key}"
									data-user="${user.userId}"
									data-action="remove"
								>
							</c:if>
							<c:if test="${!interest.value}">
								<button class="toggle-interest-button btn btn-default" 
									data-interest="${interest.key}"
									data-user="${user.userId}"
									data-action="add"
								>
							</c:if>
								${interest.key}
							</button>
						</div>
					</c:forEach>
				</div>
			</div>
		</div>
	</jsp:attribute>
	<jsp:attribute name="javascript">
		<script src="/_static/js/CalcU/personalize-interests.js"></script>
	</jsp:attribute>
</t:genericpage>