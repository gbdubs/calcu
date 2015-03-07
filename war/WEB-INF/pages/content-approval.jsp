<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | Approve Content
	</jsp:attribute>
	
	<jsp:attribute name="content">
		<div class="box box-solid">
			<div class="box-header bg-orange solid-box-header">
				<i class="fa fa-cube fa-karma-score hidden-xs"></i>
				<h3 class="box-title">Welcome to Content Approval Mode!</h3>
			</div>
			<div class="box-body">
				<p>
					INSTRUCTIONS GO HERE
				</p>
				<div class="content-approval-sidebar">
					<ul>
						<c:forEach var="pc" items="${potentialContent}" varStatus="loop">
							<li id="potential-content-sidebar-${loop.index}"></li>
						</c:forEach>
					</ul>
				</div>
				<div class="content-approval-viewer">
					<c:forEach var="pc" items="${potentialContent}" varStatus="loop">
						<c:choose>
							<c:when test="${loop.index > 0}">
								<div class="content-approval-content-display hidden">
							</c:when>
							<c:otherwise>
								<div class="content-approval-content-display">
							</c:otherwise>
						</c:choose>
							<label>Title</label>
							<h3 class="content-approval-title">${pc.title}</h3>
							<label>Body</label>
							<div class="content-appproval-body">${pc.body}</div>				
							<label>Solution</label>
							<div class="content-approval-solution">${pc.solution}</div>
							<label>Tags</label>
							<div class="content-approval-tags">${pc.tagsAsString}</div>
						</div>
					</c:forEach>
				</div>
			</div>
		</div>
		
	</jsp:attribute>
	<jsp:attribute name="javascriptDependencies">
		admin-console
	</jsp:attribute>
</t:genericpage>