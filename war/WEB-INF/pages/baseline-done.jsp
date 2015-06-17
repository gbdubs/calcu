<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | Baseline
	</jsp:attribute>

	<jsp:attribute name="content">
		<div class="centered margin-top-100">
			<h2 class="welcome-banner">Congratulations!</h2>
			<h3>Congratulations on finishing the baseline test! You are well on your way to improving your math skills</h3>
			<h3>As we speak, our servers are analyzing your results to try to give you the best experience for you.</h3>
			<h3>While you wait for that (1-2 minutes), familiarize yourself with some other features of the site.</h3>
			
			
			<div class="hexagon-table-wrapper">
				<div class="hexagon-table">
					<div class="hexagon-row">
						<a href="/search" class="hexagon-button">
							<div class="text-blue">
								<div class="hexagon outer-hexagon"></div>
								<div class="hexagon-button-insides bg-white text-white">
									<div class="hexagon inner-hexagon"></div>
								</div>
								<i class="fa fa-fw fa-search fa-5x badge-icon"></i>
							</div>
						</a>
						<br class="visible-xs">
						<a href="/user/me" class="hexagon-button">
							<div class="text-olive">
								<div class="hexagon outer-hexagon"></div>
								<div class="hexagon-button-insides bg-white text-white">
									<div class="hexagon inner-hexagon"></div>
								</div>
								<i class="fa fa-fw fa-user fa-5x badge-icon"></i>
							</div>
						</a>
					</div>
					<br>
					<div class="hexagon-row">
						<a href="/contribute"  class="hexagon-button">
							<div class="text-red">
								<div class="hexagon outer-hexagon"></div>
								<div class="hexagon-button-insides bg-white text-white">
									<div class="hexagon inner-hexagon"></div>
								</div>
								<i class="fa fa-fw fa-graduation-cap fa-5x badge-icon"></i>
							</div>
						</a>
						<div class="hexagon-blank hidden-xs"></div>
						<a href="/achievements" class="hexagon-button">
							<div class="text-yellow">
								<div class="hexagon outer-hexagon"></div>
								<div class="hexagon-button-insides bg-white text-white">
									<div class="hexagon inner-hexagon"></div>
								</div>
								<i class="fa fa-fw fa-trophy fa-5x badge-icon"></i>
							</div>
						</a>
					</div>
					<br>
					<div class="hexagon-row">
						<a href="/about" class="hexagon-button">
							<div class="text-black">
								<div class="hexagon outer-hexagon"></div>
								<div class="hexagon-button-insides bg-white text-white">
									<div class="hexagon inner-hexagon"></div>
								</div>
								<i class="fa fa-fw fa-cogs fa-5x badge-icon"></i>
							</div>
						</a>
						<br class="visible-xs">
						<a href="/contact" class="hexagon-button">
							<div class="text-purple">
								<div class="hexagon outer-hexagon"></div>
								<div class="hexagon-button-insides bg-white text-white">
									<div class="hexagon inner-hexagon"></div>
								</div>
								<i class="fa fa-fw fa-heart fa-5x badge-icon"></i>
							</div>
						</a>
					</div>
				</div>
			</div>
		</div>
	</jsp:attribute>
</t:genericpage>