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
					Here you can upload a state file to the cloud.  By default this will rely on UUIDs,
					and will not duplicate anything with the same UUID. Any Content without at UUID will
					be created (same with achievements, topics, etc.)  To begin, select the file you want
					to upload, then press <i>the big button</i>, which will begin the process of this upload
					(which may take several minutes to complete).
				</h3>
				
				<form action="${blobstoreUploadUrl}" method="POST" enctype="multipart/form-data">
					<input type="file" name="stateUpload" />
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