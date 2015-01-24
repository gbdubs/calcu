<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | Practice Problem Dashboard
	</jsp:attribute>
	<jsp:attribute name="css">
		<link href="/_static/css/CalcU/badge.css" rel="stylesheet" type="text/css" />
	</jsp:attribute>
	<jsp:attribute name="content">
	
		<div class="box box-solid bg-light-blue-gradient">
			<div class="box-header">
				<i class="fa fa-leaf"></i>
				<h3 class="box-title">Completed Achievements <small class="text-white">  You did it! Time to <a class="text-white" href="http://24.media.tumblr.com/c09a42b3c7c409674f8b2ebc5a4dfaaf/tumblr_mnzkyucG471sqn45bo1_500.gif">celebrate</a>!</h3>
			</div>
			<div class="box-body">
				<div class="row">
					<c:forEach items="${finishedAchievements}" var="a">
					<div class="col-xs-12 col-sm-6 col-md-4 col-lg-3">
						<div class="achievement-box box box-solid ${a.secondaryColor} completed">
							<div class="achievement-badge ${a.color}">
								<div class="hexagon outer-hexagon"></div>
								<div class="achievement-badge-insides ${a.secondaryColor}">
									<div class="hexagon inner-hexagon"></div>
								</div>
								<i class="fa fa-fw ${a.icon} fa-5x badge-icon"></i>
							</div>
							<h3>${a.name}</h3>
							<h5><i>${a.qualification}</i></h5>
							<p>${a.description}</p>
						</div>
					</div>
					</c:forEach>
				</div>
			</div>
		</div>
	
		<div class="box box-solid bg-black-gradient">
			<div class="box-header">
				<i class="fa fa-leaf"></i>
				<h3 class="box-title">Incomplete Achievements <small class="text-white"> You aren't there yet, but <i><a class="text-white" href="http://media.giphy.com/media/G6ToILn1IjAFW/giphy.gif">soon</a></i>!</h3>
			</div>
			<div class="box-body">
				<div class="row">
					<c:forEach items="${unfinishedAchievements}" var="a">
					<div class="col-xs-12 col-sm-6 col-md-4 col-lg-3">
						<div class="achievement-box box box-solid ${a.secondaryColor}">
							<div class="achievement-badge ${a.color}">
								<div class="hexagon outer-hexagon"></div>
								<div class="achievement-badge-insides ${a.secondaryColor}">
									<div class="hexagon inner-hexagon"></div>
								</div>
								<i class="fa fa-fw ${a.icon} fa-5x badge-icon"></i>
							</div>
							<h3>${a.name}</h3>
							<h5><i>${a.qualification}</i></h5>
							<p>${a.description}</p>
						</div>
					</div>
					</c:forEach>
				</div>
			</div>
		</div>
	
	</jsp:attribute>
</t:genericpage>