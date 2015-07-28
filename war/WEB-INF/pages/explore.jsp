<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | Explore
	</jsp:attribute>
	<jsp:attribute name="content">
		<div class="centered">
			<h2 class="welcome-banner margin-top-100">Explore Our Content</h2>
			<h3 class="margin-bottom-25 welcome-banner-subtitle">Here is a random selection of our large database of content!  Feel free to browse here, and use our other features to explore specific areas.</h3>
			<h4 class="margin-bottom-50 welcome-banner-subtitle"> Click on the plus button to view the results. If you'd like to view the page where the content originates, click on the name in the box header. </h4>
		</div>
		
		<div class ="row margin-bottom-50">	
			<div class="col-lg-4 col-md-6 col-sm-12">
				<c:forEach items="${exploreContent}" var="content">								
					<div class="box box-${content.boxColor} box-solid no-overflow explore-box collapsed-box box-height-explore">
						<div class="box-header">
							<h4 class="box-title"><a href="${content.url}" class="text-white">${content.title}</a></h4>
							<div class="pull-right box-tools">
								<c:set var="bookmarked" value="false" />
								<c:forEach var="bookmarkUuid" items="${bookmarkUuids}">
									<c:if test="${bookmarkUuid eq content.uuid}">
										<c:set var="bookmarked" value="true" />
									</c:if>
								</c:forEach>
								<c:choose>
									<c:when test="${bookmarked}">
										<button type="button" class="toggle-bookmark-button buttonless btn btn-${content.boxColor}" data-action="remove" data-user="${user.userId}" data-content="${content.uuid}">
											<i class="fa fa-bookmark"></i>
										</button>
									</c:when>
									<c:otherwise>
										<button type="button" class="toggle-bookmark-button buttonless btn btn-${content.boxColor}" data-action="add" data-user="${user.userId}" data-content="${content.uuid}">
											<i class="fa fa-bookmark-o"></i>
										</button>
									</c:otherwise>
								</c:choose>
								<button class="btn btn-${content.boxColor} btn-sm" type="button" data-widget="collapse" data-toggle="tooltip" title="" data-original-title="Collapse"><i class="fa fa-plus"></i></button>
							</div>
						</div>
						<div class="box-body" style="display:none;">
							${content.body}
						</div>
					</div>
				</c:forEach>
			</div>
		
			<div class="col-lg-4 col-md-6 col-sm-12">
				<c:forEach items="${exploreContent1}" var="content">								
					<div class="box box-${content.boxColor} box-solid no-overflow explore-box collapsed-box box-height-explore">
						<div class="box-header">
							<h4 class="box-title"><a href="${content.url}" class="text-white">${content.title}</a></h4>
							<div class="pull-right box-tools">
								<c:set var="bookmarked" value="false" />
								<c:forEach var="bookmarkUuid" items="${bookmarkUuids}">
									<c:if test="${bookmarkUuid eq content.uuid}">
										<c:set var="bookmarked" value="true" />
									</c:if>
								</c:forEach>
								<c:choose>
									<c:when test="${bookmarked}">
										<button type="button" class="toggle-bookmark-button buttonless btn btn-${content.boxColor}" data-action="remove" data-user="${user.userId}" data-content="${content.uuid}">
											<i class="fa fa-bookmark"></i>
										</button>
									</c:when>
									<c:otherwise>
										<button type="button" class="toggle-bookmark-button buttonless btn btn-${content.boxColor}" data-action="add" data-user="${user.userId}" data-content="${content.uuid}">
											<i class="fa fa-bookmark-o"></i>
										</button>
									</c:otherwise>
								</c:choose>
								<button class="btn btn-${content.boxColor} btn-sm" type="button" data-widget="collapse" data-toggle="tooltip" title="" data-original-title="Collapse"><i class="fa fa-plus"></i></button>
							</div>
						</div>
						<div class="box-body" style="display:none;">
							${content.body}
						</div>
					</div>
				</c:forEach>
			</div>
	
			<div class="col-lg-4 col-md-6 col-sm-12 explore-column-hidden">
				<c:forEach items="${exploreContent2}" var="content">								
					<div class="box box-${content.boxColor} box-solid no-overflow explore-box collapsed-box box-height-explore">
						<div class="box-header">
							<h4 class="box-title"><a href="${content.url}" class="text-white">${content.title}</a></h4>
							<div class="pull-right box-tools">
								<c:set var="bookmarked" value="false" />
								<c:forEach var="bookmarkUuid" items="${bookmarkUuids}">
									<c:if test="${bookmarkUuid eq content.uuid}">
										<c:set var="bookmarked" value="true" />
									</c:if>
								</c:forEach>
								<c:choose>
									<c:when test="${bookmarked}">
										<button type="button" class="toggle-bookmark-button buttonless btn btn-${content.boxColor}" data-action="remove" data-user="${user.userId}" data-content="${content.uuid}">
											<i class="fa fa-bookmark"></i>
										</button>
									</c:when>
									<c:otherwise>
										<button type="button" class="toggle-bookmark-button buttonless btn btn-${content.boxColor}" data-action="add" data-user="${user.userId}" data-content="${content.uuid}">
											<i class="fa fa-bookmark-o"></i>
										</button>
									</c:otherwise>
								</c:choose>
								<button class="btn btn-${content.boxColor} btn-sm" type="button" data-widget="collapse" data-toggle="tooltip" title="" data-original-title="Collapse"><i class="fa fa-plus"></i></button>
							</div>
						</div>
						<div class="box-body" style="display:none;">
							${content.body}
						</div>
					</div>
				</c:forEach>
			</div>
		</div>
	
		<div class="centered">
			<h3 class="welcome-banner-subtitle">Now that you have seen some of the pieces of our site, check out some of our features to get started in a specific area.</h3>
			<div class="hexagon-table">
				<div class="hexagon-row centered">
					<a href="/baseline" class="hexagon-button">
						<div class="text-light-blue">
							<div class="hexagon outer-hexagon"></div>
							<div class="hexagon-button-insides bg-white text-white">
								<div class="hexagon inner-hexagon"></div>
							</div>
							<i class="fa fa-fw fa-area-chart fa-5x badge-icon"></i>
							<h4 class="hexagon-title">Baseline</h4>
						</div>
					</a>
					<br class="visible-xs">
					<br class="visible-xs">
					<a href="/recommendations" class="hexagon-button">
						<div class="text-yellow">
							<div class="hexagon outer-hexagon"></div>
							<div class="hexagon-button-insides bg-white text-white">
								<div class="hexagon inner-hexagon"></div>
							</div>
							<i class="fa fa-fw fa-road fa-5x badge-icon"></i>
							<h4 class="hexagon-title">Recommendations</h4>
						</div>
					</a>
					<br class="visible-xs">
					<br class="visible-xs">


					<a href="/search" class="hexagon-button">
						<div class="text-green">
							<div class="hexagon outer-hexagon"></div>
							<div class="hexagon-button-insides bg-white text-white">
								<div class="hexagon inner-hexagon"></div>
							</div>
							<i class="fa fa-fw fa-search fa-5x badge-icon"></i>
							<h4 class="hexagon-title">Search by Tag</h4>
						</div>
					</a>
				</div>
			</div>
		</div>
	</jsp:attribute>
	<jsp:attribute name="javascriptDependencies">
		explore
	</jsp:attribute>
</t:genericpage>
	