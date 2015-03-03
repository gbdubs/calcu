<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | Recommendations
	</jsp:attribute>

	<jsp:attribute name="content">
		
		<div class="box box-solid">
			<div class="box-header bg-olive">
				<i class="fa fa-tasks"></i>
				<h3 class="box-title">Recommendations</h3>
			</div>
			<div class="box-body">
				<p>
					This is the recommendation page, where we take everything you tell us about yourself, and
					try to synthesize it in to the resources which will best be able to help you learn. If
					you find that these recommendations aren't what you are looking for, it means we haven't
					gotten to know you well enough yet!  Head over to the <a href="/personalize">personalize page</a>
					to let us get to know you better!
				</p>
				<p>
					On each piece of recommended content, you can provide us with feedback:
					<ul>
						<li><i class="fa fa-thumbs-up round-icon-button"></i>Tell us when you like a recommendation to get more like it! </li>
						<li><i class="fa fa-bookmark round-icon-button"></i>You can bookmark the content so that you can come back to it later </li>
						<li><i class="fa fa-eye-slash round-icon-button"></i>You can hide the content if you have seen it before. This doesn't tell us what you think about it </li>
						<li><i class="fa fa-thumbs-down round-icon-button"></i>Let us know when you aren't interested in the subject matter of the recommendation so that we can make it better </li>
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
						
							<i class="fa percentage-display">${rec.percentage}</i>
							
							<a href="${rec.url}"><b>${rec.title}</b></a>
							${rec.description}
							
							<i type="button" class="interested-button fa fa-thumbs-o-up" data-action="interested" data-user="${user.userId}" data-content="${rec.uuid}" title="I Like This!"></i>
							
							<c:choose>
								<c:when test="${bookmarked}">
									<i type="button" class="toggle-bookmark-button fa fa-bookmark" data-action="remove" data-user="${user.userId}" data-content="${rec.uuid}" title="Un-Bookmark This!"></i>
								</c:when>
								<c:otherwise>
									<i type="button" class="toggle-bookmark-button fa fa-bookmark-o" data-action="add" data-user="${user.userId}" data-content="${rec.uuid}" title="Bookmark This!"></i>
								</c:otherwise>
							</c:choose>
							<i type="button" class="hide-recommendation-button fa fa-eye-slash" data-action="hide" data-user="${user.userId}" data-content="${rec.uuid}" title="Hide this for now."></i>
							<i type="button" class="disinterested-button fa fa-thumbs-o-down" data-action="disinterested" data-user="${user.userId}" data-content="${rec.uuid}" title="I don't Like this."></i>
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
	<jsp:attribute name="javascript">
		<script src="/_static/js/CalcU/recommendations.js"></script>
	</jsp:attribute>
</t:genericpage>