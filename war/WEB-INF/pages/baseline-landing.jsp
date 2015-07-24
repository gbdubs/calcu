<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | Baseline
	</jsp:attribute>

	<jsp:attribute name="content">
		<div class="centered margin-top-100">
			<c:if test="${userLoggedIn}">
				<c:if test="${!beenHereBefore}">
					<h2 class="welcome-banner">Baseline</h2>
					<h3 class="welcome-banner-subtitle">Nice to meet you! Let us know where you are, so we can get you to where you want to go.</h3>
					<h3 class="welcome-banner-subtitle">To Get Started, press the Arrow, to learn about the process, follow the other links.</h3>
				</c:if>
			
			
				<c:if test="${beenHereBefore}">
					<h2 class="welcome-banner">Re-Baseline</h2>
					<h3>Let's get to know you better! Then, we can get you to where you want to go.</h3>
					<h3>To Get Started, press the Arrow, to learn about the process, follow the other links.</h3>
				</c:if>
			</c:if>
			<c:if test="${!userLoggedIn}">
				<h2 class="welcome-banner">Baseline</h2>
				<h3 class="welcome-banner-subtitle">We would like to meet you! Please log in to allow us to adapt the site to your needs.</h3>
				<h3><a class="welcome-banner-subtitle login-button">Log in now to begin the process</a></h3>
			</c:if>

			
			<c:if test="${userLoggedIn}">
				<div class="hexagon-table-wrapper">
					<div class="hexagon-table">
						<div class="hexagon-row">
							<a href="/baseline/1" class="hexagon-button">
								<div class="text-olive">
									<div class="hexagon outer-hexagon"></div>
									<div class="hexagon-button-insides bg-white text-white">
										<div class="hexagon inner-hexagon"></div>
									</div>
									<i class="fa fa-fw fa-long-arrow-right fa-5x badge-icon"></i>
								</div>
							</a>
						</div>
						<br>
						<div class="hexagon-row hidden-xs">
							<div class="hexagon-decoration">
								<div class="text-light-blue">
									<div class="hexagon outer-hexagon"></div>
									<div class="hexagon-button-insides bg-white text-white">
										<div class="hexagon inner-hexagon"></div>
									</div>
									<i class="fa fa-fw fa-area-chart fa-5x badge-icon"></i>
								</div>
							</div>
							<div class="hexagon-decoration">
								<div class="text-green">
									<div class="hexagon outer-hexagon"></div>
									<div class="hexagon-button-insides bg-white text-white">
										<div class="hexagon inner-hexagon"></div>
									</div>
									<i class="fa fa-fw fa-flask fa-5x badge-icon"></i>
								</div>
							</div>
						</div>
						<br>
						<div class="hexagon-row hidden-xs">
							<div class="hexagon-text hexagon-button" id="what-to-expect-button">
								<div class="text-blue">
									<div class="hexagon outer-hexagon"></div>
									<div class="hexagon-button-insides bg-white text-white">
										<div class="hexagon inner-hexagon"></div>
									</div>
									<div class="hex-text-wrapper-1">
										<div class="hex-text-wrapper-2">
											<div class="hex-text-wrapper-3">
												<h3 class="no-margin">What to Expect</h3>
											</div>
										</div>
									</div>
								</div>
							</div>
							<div class="hexagon-blank"></div>
							<div class="hexagon-text hexagon-button" id="about-the-science-button">
								<div class="text-yellow">
									<div class="hexagon outer-hexagon"></div>
									<div class="hexagon-button-insides bg-white text-white">
										<div class="hexagon inner-hexagon"></div>
									</div>
									<div class="hex-text-wrapper-1">
										<div class="hex-text-wrapper-2">
											<div class="hex-text-wrapper-3">
												<h3 class="no-margin">About the Science</h3>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="hexagon-big-row">
							<div class="hexagon-text hexagon-text-big hidden" id="what-to-expect-text">
								<div class="text-blue">
									<div class="hexagon outer-hexagon"></div>
									<div class="hexagon-button-insides bg-white text-white">
										<div class="hexagon inner-hexagon"></div>
									</div>
									<div class="hex-text-wrapper-1">
										<div class="hex-text-wrapper-2">
											<div class="hex-text-wrapper-3">
												<h3 class="no-margin">What to Expect</h3>
												<p>What to ExpectWhat to ExpectWhat to ExpectWhat to ExpectWhat to ExpectWhat to ExpectWhat to ExpectWhat to ExpectWhat to ExpectWhat to ExpectWhat to ExpectWhat to ExpectWhat to ExpectWhat to ExpectWhat to ExpectWhat to Expect</p>
											</div>
										</div>
									</div>
								</div>
							</div>
							<div class="hexagon-text hexagon-text-big hidden" id="about-the-science-text">
								<div class="text-yellow">
									<div class="hexagon outer-hexagon"></div>
									<div class="hexagon-button-insides bg-white text-white">
										<div class="hexagon inner-hexagon"></div>
									</div>
									<div class="hex-text-wrapper-1">
										<div class="hex-text-wrapper-2">
											<div class="hex-text-wrapper-3">
												<h3 class="no-margin">FOR SCIENCE!!!h3>
												<p>What to ExpectWhat to ExpectWhat to ExpectWhat to ExpectWhat to ExpectWhat to ExpectWhat to ExpectWhat to ExpectWhat to ExpectWhat to ExpectWhat to ExpectWhat to ExpectWhat to ExpectWhat to ExpectWhat to ExpectWhat to Expect</p>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="hexagon-row centered visible-xs">
							<div class="hexagon-text hexagon-button">
								<div class="text-blue">
									<div class="hexagon outer-hexagon"></div>
									<div class="hexagon-button-insides bg-white text-white">
										<div class="hexagon inner-hexagon"></div>
									</div>
									<div class="hex-text-wrapper-1">
										<div class="hex-text-wrapper-2">
											<div class="hex-text-wrapper-3">
												<h3 class="no-margin">What to Expect</h3>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="hexagon-row centered visible-xs">
							<div class="hexagon-text hexagon-button">
								<div class="text-yellow">
									<div class="hexagon outer-hexagon"></div>
									<div class="hexagon-button-insides bg-white text-white">
										<div class="hexagon inner-hexagon"></div>
									</div>
									<div class="hex-text-wrapper-1">
										<div class="hex-text-wrapper-2">
											<div class="hex-text-wrapper-3">
												<h3 class="no-margin">About the Science</h3>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</c:if>
			<br>
			<br>
			<br>
		</div>
	</jsp:attribute>
	<jsp:attribute name="javascriptDependencies">
		baseline-landing
	</jsp:attribute>
</t:genericpage>