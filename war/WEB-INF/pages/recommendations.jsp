<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | Recommendations
	</jsp:attribute>

	<jsp:attribute name="content">
		
		<div class="centered">
			<h2 class="welcome-banner margin-top-100">Recommendations</h2>
			<h3 class="margin-bottom-50 welcome-banner-subtitle max-width-at-900">
				This is where we take everything you tell us about yourself and
				synthesize it in to the resources which will help you learn.
			</h3> 
			<br>
			<div class="max-width-at-1000 align-left margin-bottom-50">
				<p>
					This is the recommendation page, where we take everything you tell us about yourself, and
					try to synthesize it in to the resources which will best be able to help you learn. If
					you find that these recommendations aren't what you are looking for, it means we haven't
					gotten to know you well enough yet!  Head over to the <a href="/personalize">personalize page</a>
					to let us get to know you better!
				</p>
				<p>
					On each piece of recommended content, you can provide us with feedback:
					<ul class="basic-list">
						<li class="hexagon-bullet-line-item"><i class="hexagon-icon-button hexagon-interested"><i class="fa fa-fw fa-thumbs-up"></i></i>Tell us when you like a recommendation to get more like it! </li>
						<li class="hexagon-bullet-line-item"><i class="hexagon-icon-button hexagon-bookmark"><i class="fa fa-fw fa-bookmark"></i></i>You can bookmark the content so that you can come back to it later </li>
						<li class="hexagon-bullet-line-item"><i class="hexagon-icon-button hexagon-recommendations"><i class="fa fa-fw fa-eye-slash "></i></i>You can hide the content if you have seen it before. This doesn't tell us what you think about it </li>
						<li class="hexagon-bullet-line-item"><i class="hexagon-icon-button hexagon-disinterested"><i class="fa fa-fw fa-thumbs-down"></i></i>Let us know when you aren't interested in the subject matter of the recommendation so that we can make it better </li>
					</ul>
					To unhide all previously hidden recommendations, press the button at the bottom of this page.
					As this feature is at the heart of the website, please don't hesitate to contact us with suggestions on how it could be improved. Get After it!
				</p>
				
				<c:if test="${fn:length(recommendationsMenu) == 0}">
					<div class="box box-solid box-warning">
						<div class="box-header">
							<i class="fa fa-street-view"></i>
							<h3 class="box-title">Not Enough Information To Recommend</h3>
						</div>
						<div class="box-body">
							<p>
								We don't yet have enough information about you to recommend any new content to you... If you complete the <a href="/personalize">Personalize Activity</a> at least 
								once, we will have enough information to recommend you some great resources. Feel free to complete it multiple times if you want, and remember that you express
								interest in anything that you view, bookmark, rate, or comment on. The more you integrate with the site, the better your recommendations will be!
							</p>
							<a href="/personalize" class="btn btn-warning btn-block">Personalize Now</a>
						</div>
					</div>
				</c:if>
				
				<div class="all-search-results">
					<c:set var="numPages" value="${1}"/>
					<div class="search-result-page" id="rec-result-page-1">
					<c:forEach items="${recommendationsMenu}" var="rec" varStatus="loop">
						
						<c:if test="${loop.index % 10 == 0 && loop.index > 0}">
							</div>
							<c:set var="numPages" value="${numPages + 1}"/>
							<div class="search-result-page hidden" id="rec-result-page-${numPages}">
						</c:if>
						
						<c:set var="bookmarked" value="false" />
						<c:forEach var="bookmarkUuid" items="${bookmarkUuids}">
						  <c:if test="${bookmarkUuid eq rec.uuid}">
							<c:set var="bookmarked" value="true" />
						  </c:if>
						</c:forEach>

						<div class="alert alert-recommendation alert-${rec.color}">
							
							<a href="${rec.url}"><b>${rec.title}</b></a>
							<div class="max-height-wrapper">
								${rec.description}
							</div>
							<c:choose>
								<c:when test="${rec.value == 'interested'}">
									<i type="button" class="interested-button hexagon-icon-button hexagon-interested" data-action="interested" data-user="${user.userId}" data-content="${rec.uuid}" title="I Like This!"><i class="fa fa-thumbs-up"></i></i>
								</c:when>
								<c:otherwise>
									<i type="button" class="interested-button hexagon-icon-button hexagon-interested" data-action="interested" data-user="${user.userId}" data-content="${rec.uuid}" title="I Like This!"><i class="fa fa-thumbs-o-up"></i></i>
								</c:otherwise>
							</c:choose>
							<c:choose>
								<c:when test="${bookmarked}">
									<i type="button" class="toggle-bookmark-button hexagon-icon-button hexagon-bookmark" data-action="remove" data-user="${user.userId}" data-content="${rec.uuid}" title="Un-Bookmark This!"><i class="fa fa-bookmark"></i></i>
								</c:when>
								<c:otherwise>
									<i type="button" class="toggle-bookmark-button hexagon-icon-button hexagon-bookmark" data-action="add" data-user="${user.userId}" data-content="${rec.uuid}" title="Bookmark This!"><i class="fa fa-bookmark-o"></i></i>
								</c:otherwise>
							</c:choose>
							<i type="button" class="hide-recommendation-button hexagon-icon-button hexagon-recommendations" data-action="hide" data-user="${user.userId}" data-content="${rec.uuid}" title="Hide this for now."><i class=" fa fa-eye-slash"></i></i>
							<c:choose>
								<c:when test="${rec.value == 'disinterested'}">
									<i type="button" class="disinterested-button hexagon-icon-button hexagon-disinterested" data-action="disinterested" data-user="${user.userId}" data-content="${rec.uuid}" title="I don't Like this."><i class="fa fa-thumbs-down"></i></i>
								</c:when>
								<c:otherwise>
									<i type="button" class="disinterested-button hexagon-icon-button hexagon-disinterested" data-action="disinterested" data-user="${user.userId}" data-content="${rec.uuid}" title="I don't Like this."><i class="fa fa-thumbs-o-down"></i></i>
								</c:otherwise>
							</c:choose>
						</div>
					</c:forEach>
					</div>
					
					<c:if test="${numPages > 1}">
						<div class="overflower">
							<button class="btn btn-success pull-left show-all-recommendations" data-action="showAll" data-user="${user.userId}">Unhide All Hidden Recommendations</button>
							<div class="btn-group no-margin pull-right">
								<c:forEach begin="1" end="${numPages}" var="i">
									<button class="result-page-tab btn btn-default" id="rec-result-page-${i}-tab">${i}</button>
								</c:forEach>
							</div>
						</div>
					</c:if>
				</div>
			</div>
		</div>
	</jsp:attribute>

	<jsp:attribute name="javascriptDependencies">
		recommendations
	</jsp:attribute>
</t:genericpage>