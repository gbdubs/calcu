<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | Download Content State
	</jsp:attribute>
	
	<jsp:attribute name="content">
		<div class="centered">
			<div class="max-width-at-1000">
			
				<h2 class="welcome-banner margin-top-100">State Download</h2>
			
				<h3 class="welcome-banner-subtitle margin-bottom-50">
					Here you can press <i>the big button</i>, which will begin the process of
					writing your database to Google Cloud Storage for download.
				</h3>
				
				<form action="/admin/download" method="post">
					<button class="hexagon-button buttonless">
						<div class="text-green">
							<div class="hexagon outer-hexagon"></div>
							<div class="hexagon-button-insides bg-white text-white">
								<div class="hexagon inner-hexagon"></div>
							</div>
							<i class="fa fa-fw fa-play fa-5x badge-icon"></i>
						</div>
					</button>
				</form>
				
			</div>
		</div>
		
	</jsp:attribute>
	<jsp:attribute name="javascriptDependencies">
		content-approval
	</jsp:attribute>
</t:genericpage>