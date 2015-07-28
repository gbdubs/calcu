<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | Upload Content State
	</jsp:attribute>
	
	<jsp:attribute name="content">
		<div class="centered">
			<div class="max-width-at-1000">
			
				<h2 class="welcome-banner margin-top-100">State Upload</h2>
			
				<h3 class="welcome-banner-subtitle margin-bottom-50">
					Here you can update the database based on the state file saved in /war/WEB-INF/data/content/state-file-x.txt
					To begin the process of uploading, press <i>the big button</i>, which will begin the process of this upload
					(which may take several minutes to complete). This is a <b>VERY</b> expensive operation to run in the cloud.
				</h3>
				
				<h2 class="thin">Which state-files do you want to upload right now?</h2>
				    
				
				
				<form action="/admin/upload" method="POST" >
					<!--<input type="text" name="numbers" class="full-width padding-5 margin-top-20" placeholder="Type in the state files you want to upload, seperated by commas"/>-->
				
					<button class="hexagon-button buttonless">
						<div class="text-blue">
							<div class="hexagon outer-hexagon"></div>
							<div class="hexagon-button-insides bg-white text-white">
								<div class="hexagon inner-hexagon"></div>
							</div>
							<i class="fa fa-fw fa-upload fa-5x badge-icon"></i>
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