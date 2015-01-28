<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | Explore
	</jsp:attribute>
	<jsp:attribute name="content">
		<div class="row">
			<div class="col-lg-3 col-md-12 col-sm-12 col-xs-12">
				<div class="box box-solid bg-blue-gradient">
					<div class="box-header">
						<i class="fa fa-fire fa-karma-score"></i>
						<h3 class="box-title">Hot Off The Presses</h3>
					</div>	
					<div class="box-body content-rotating">
						<div id="new-content-panel-0" class="row">
							<c:forEach items="${newContent}" var="content" varStatus="loop">
					<c:if test="${loop.index % 6 == 0 && loop.index > 0}">
						</div>
						<div id="new-content-panel-${loop.index/6}" class="row hidden">
					</c:if>			
					
							<c:set var="bookmarked" value="false" />
							<c:forEach var="bookmarkUuid" items="${bookmarkUuids}">
							  <c:if test="${bookmarkUuid eq content.uuid}">
								<c:set var="bookmarked" value="true" />
							  </c:if>
							</c:forEach>
					
								<div class="col-lg-12 col-md-4 col-sm-6 col-xs-12">
									<div class="box box-${content.boxColor}">
										<div class="box-header">
											<i class="fa buttonless-secondary ${content.boxIcon}"></i>
											<h4 class="box-title">${content.title}</h4>
											<c:choose>
												<c:when test="${bookmarked}">
													<button type="button" class="remove-bookmark-button bookmarked-button pull-right buttonless" data-user="${user.userId}" data-content="${content.uuid}">
														<i class="fa fa-bookmark"></i>
													</button>
												</c:when>
												<c:otherwise>
													<button type="button" class="add-bookmark-button pull-right buttonless" data-user="${user.userId}" data-content="${content.uuid}">
														<i class="fa fa-bookmark"></i>
													</button>
												</c:otherwise>
											</c:choose>
										</div>
										<div class="box-body text-black">
											${content.abbreviatedBody}
										</div>
									</div>
								</div>		
							</c:forEach>
						</div>
						<div class="btn-group pull-right">
							<button class="btn btn-default hidden"></button>
						</div>
					</div>
				</div>
			</div>
			<div class="col-lg-3 col-md-4 col-sm-6 col-xs-12">			
				<div class="box box-solid bg-yellow-gradient">
					<div class="box-header">
						<i class="fa fa-trophy fa-karma-score"></i>
						<h3 class="box-title">Only the Best</h3>
					</div>	
					<div class="box-body content-rotating">
						<div id="best-content-panel-0" class="row">
							<c:forEach items="${bestContent}" var="content" varStatus="loop">
					<c:if test="${loop.index % 4 == 0 && loop.index > 0}">
						</div>
						<div id="best-content-panel-${loop.index/4}" class="row hidden">
					</c:if>
						
							<c:set var="bookmarked" value="false" />
							<c:forEach var="bookmarkUuid" items="${bookmarkUuids}">
							  <c:if test="${bookmarkUuid eq content.uuid}">
								<c:set var="bookmarked" value="true" />
							  </c:if>
							</c:forEach>
						
								<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
									<div class="box box-${content.boxColor}">
										<div class="box-header">
											<i class="fa buttonless-secondary ${content.boxIcon}"></i>
											<h4 class="box-title">${content.title}</h4>
											<c:choose>
												<c:when test="${bookmarked}">
													<button type="button" class="remove-bookmark-button bookmarked-button pull-right buttonless" data-user="${user.userId}" data-content="${content.uuid}">
														<i class="fa fa-bookmark"></i>
													</button>
												</c:when>
												<c:otherwise>
													<button type="button" class="add-bookmark-button pull-right buttonless" data-user="${user.userId}" data-content="${content.uuid}">
														<i class="fa fa-bookmark"></i>
													</button>
												</c:otherwise>
											</c:choose>
										</div>
										<div class="box-body text-black">
											${content.abbreviatedBody}
										</div>
									</div>
								</div>				
							</c:forEach>
						</div>
						<div class="btn-group pull-right">
							<button class="btn btn-default hidden"></button>
						</div>
					</div>
				</div>
			</div>
			<div class="col-lg-3 col-md-4 col-sm-6 col-xs-12">	
				<div class="box box-solid bg-green-gradient">
					<div class="box-header">
						<i class="fa fa-globe fa-karma-score"></i>
						<h3 class="box-title">Random Content</h3>
					</div>	
					<div class="box-body content-rotating">
						<div id="random-content-panel-0" class="row">
							<c:forEach items="${randomContent}" var="content" varStatus="loop">
					<c:if test="${loop.index % 4 == 0 && loop.index > 0}">
						</div>
						<div id="random-content-panel-${loop.index/4}" class="row hidden">
					</c:if>	
					
							<c:set var="bookmarked" value="false" />
							<c:forEach var="bookmarkUuid" items="${bookmarkUuids}">
							  <c:if test="${bookmarkUuid eq content.uuid}">
								<c:set var="bookmarked" value="true" />
							  </c:if>
							</c:forEach>
							
								<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
									<div class="box box-${content.boxColor}">
										<div class="box-header">
											<i class="fa buttonless-secondary ${content.boxIcon}"></i>
											<h4 class="box-title">${content.title}</h4>
											<c:choose>
												<c:when test="${bookmarked}">
													<button type="button" class="remove-bookmark-button bookmarked-button pull-right buttonless" data-user="${user.userId}" data-content="${content.uuid}">
														<i class="fa fa-bookmark"></i>
													</button>
												</c:when>
												<c:otherwise>
													<button type="button" class="add-bookmark-button pull-right buttonless" data-user="${user.userId}" data-content="${content.uuid}">
														<i class="fa fa-bookmark"></i>
													</button>
												</c:otherwise>
											</c:choose>
										</div>
										<div class="box-body text-black">
											${content.abbreviatedBody}
										</div>
									</div>
								</div>
									
							</c:forEach>
						</div>
						<div class="btn-group pull-right">
							<button class="btn btn-default hidden"></button>
						</div>
					</div>
				</div>
			</div>
			<div class="col-lg-3 col-md-4 col-sm-12 col-xs-12">
				<div class="box box-solid bg-purple-gradient">
					<div class="box-header">
						<i class="fa fa-tasks fa-karma-score"></i>
						<h3 class="box-title">Suggested For You</h3>
					</div>	
					<div class="box-body content-rotating">
						<div id="suggested-content-panel-0" class="row">
							<c:forEach items="${suggestedContent}" var="content" varStatus="loop">
					<c:if test="${loop.index % 4 == 0 && loop.index > 0}">
						</div>
						<div id="suggested-content-panel-${loop.index/4}" class="row hidden">
					</c:if>	
							
							
							<c:set var="bookmarked" value="false" />
							<c:forEach var="bookmarkUuid" items="${bookmarkUuids}">
							  <c:if test="${bookmarkUuid eq content.uuid}">
								<c:set var="bookmarked" value="true" />
							  </c:if>
							</c:forEach>
					
					
								<div class="col-lg-12 col-md-12 col-sm-6 col-xs-12">
									<div class="box box-${content.boxColor}">
										<div class="box-header">
											<i class="fa buttonless-secondary ${content.boxIcon}"></i>
											<h4 class="box-title">${content.title}</h4>
											<c:choose>
												<c:when test="${bookmarked}">
													<button type="button" class="remove-bookmark-button bookmarked-button pull-right buttonless" data-user="${user.userId}" data-content="${content.uuid}">
														<i class="fa fa-bookmark"></i>
													</button>
												</c:when>
												<c:otherwise>
													<button type="button" class="add-bookmark-button pull-right buttonless" data-user="${user.userId}" data-content="${content.uuid}">
														<i class="fa fa-bookmark"></i>
													</button>
												</c:otherwise>
											</c:choose>
										</div>
										<div class="box-body text-black">
											${content.abbreviatedBody}
										</div>
									</div>
								</div>				
							</c:forEach>
						</div>
						<div class="btn-group pull-right">
							<button class="btn btn-default hidden"></button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</jsp:attribute>	
	<jsp:attribute name="javascript">
		<script src="/_static/js/CalcU/explore.js"></script>
	</jsp:attribute>
</t:genericpage>
	