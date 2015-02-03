<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | Achievements
	</jsp:attribute>
	<jsp:attribute name="css">
		<link href="/_static/css/CalcU/badge.css" rel="stylesheet" type="text/css" />
	</jsp:attribute>
	<jsp:attribute name="content">
		<div class="box box-solid box-success">
			<div class="box-header">
				<i class="fa fa-trophy"></i>
				<h3 class="box-title">Karma Overview/Rues</h3>
				<div class="box-tools pull-right">
					<button class="btn btn-success btn-sm" data-widget="collapse"><i class="fa fa-minus"></i></button>
				</div>
			</div>
			<div class="box-body">
				<p>
					Karma is a simple way to reward you for using the site and for helping other people.
					When you interact with the site in positive ways, you get karma. When people use the 
					site in non-positive ways, that user loses karma. There are a couple of ways you can earn karma.
				</p>
				<ul>
					<li>Rating other user's work gets you <b>1</b> Karma, regardless of if the rating is good or bad.</li>
					<li>Contributing a problem, question, or other content gets you <b>5</b> Karma immediately.</li>
					<li>When other users rate your content, you will get <b>between -2 and 4</b> Karma, depending on the quality of their rating.</li>
					<li>Answering a question gives you <b>3</b> Karma immediately, and between <b>-2 and 4</b> Karma per other user's Rating.</li>
					<li>Answer questions in Turbo Mode gets you an additional karma boost of <b>between 1 and 10</b> Karma immediately.
					<li>If your answer becomes an "approved answer", you get an additional <b>10</b> Karma immediately.</li>
				</ul>
			</div>
		</div>
		
		<div class="box box-solid bg-black-gradient">
			<div class="box-header">
				<i class="fa fa-leaf"></i>
				<h3 class="box-title">Achievements</h3>
				<div class="box-tools pull-right">
					<button class="btn btn-default btn-sm" data-widget="collapse"><i class="fa fa-minus"></i></button>
				</div>
			</div>
			<div class="box-body">
				<div class="row">
					<c:forEach items="${achievements}" var="a">
					<div class="col-xs-12 col-sm-4 col-md-4 col-lg-3">
						<div class="achievement-box <c:if test="${a.value}">completed</c:if> box box-solid ${a.key.secondaryColor}">
							<div class="achievement-badge ${a.key.color}">
								<div class="hexagon outer-hexagon"></div>
								<div class="achievement-badge-insides ${a.key.secondaryColor}">
									<div class="hexagon inner-hexagon"></div>
								</div>
								<i class="fa fa-fw ${a.key.icon} fa-5x badge-icon"></i>
							</div>
							<h3>${a.key.name}</h3>
							<h5><i>${a.key.qualification}</i></h5>
							<p>${a.key.description}</p>
						</div>
					</div>
					</c:forEach>
				</div>
			</div>
		</div>
	
	</jsp:attribute>
</t:genericpage>