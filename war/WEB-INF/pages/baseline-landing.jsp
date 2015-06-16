<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | Baseline
	</jsp:attribute>

	<jsp:attribute name="content">
		<div class="centered margin-top-100">
			<c:if test="${!beenHereBefore}">
				<h2 class="welcome-banner">Baseline</h2>
				<h3>Nice to meet you! Let us know where you are, so we can get you to where you want to go.</h3>
				<h3>To Get Started, press the Arrow, to learn about the process, follow the other links.</h3>
			</c:if>
			
			
			<c:if test="${beenHereBefore}">
				<h2 class="welcome-banner">Re-Baseline</h2>
				<h3>Let's get to know you better! Then, we can get you to where you want to go.</h3>
				<h3>To Get Started, press the Arrow, to learn about the process, follow the other links.</h3>
			</c:if>
		<!--
			<div class="hexagon-button">
				<div class="text-green">
					<div class="hexagon outer-hexagon"></div>
					<div class="hexagon-button-insides bg-white text-white">
						<div class="hexagon inner-hexagon"></div>
					</div>
					<i class="fa fa-fw fa-long-arrow-right fa-5x badge-icon"></i>
				</div>
			</div>
			
			<div class="hexagon-decoration">
				<div class="text-blue">
					<div class="hexagon outer-hexagon"></div>
					<div class="hexagon-decoration-insides bg-yellow text-yellow">
						<div class="hexagon inner-hexagon"></div>
					</div>
					<i class="fa fa-fw fa-long-arrow-right fa-5x badge-icon"></i>
				</div>
			</div>
			
			<div class="hexagon-text">
				<div class="text-blue">
					<div class="hexagon outer-hexagon"></div>
					<div class="hexagon-text-insides bg-white text-white">
						<div class="hexagon inner-hexagon"></div>
					</div>
					<div class="hex-text-wrapper-1">
						<div class="hex-text-wrapper-2">
							<div class="hex-text-wrapper-3">
								<h3 class="no-margin">Trigonometry and Algebra Two
								</h3>
							</div>
						</div>
					</div>
				</div>
			</div>
		-->	
			
			
			
			<div class="hexagon-table-wrapper">
				<div class="hexagon-table">
				<div class="hexagon-row">
					<div class="hexagon-button">
						<div class="text-olive">
							<div class="hexagon outer-hexagon"></div>
							<div class="hexagon-button-insides bg-white text-white">
								<div class="hexagon inner-hexagon"></div>
							</div>
							<i class="fa fa-fw fa-long-arrow-right fa-5x badge-icon"></i>
						</div>
					</div>
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
					<div class="hexagon-blank"></div>
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
		</div>
	</jsp:attribute>
</t:genericpage>