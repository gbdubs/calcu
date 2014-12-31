<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | Login to Access ${pageName}
	</jsp:attribute>
	<jsp:attribute name="content">
		<div class="error-page">
    		<h2 class="headline text-info">Login!</h2>
    		<div class="error-content">
        		<h3>Login Required</h3>
        		<p>
            		In order to access ${pageName}, you will need to <a class="login-button">log in</a>. If you think you should be able to access this area without logging in, please contact us with which area you are accessing, and why it should be publicly available.
        		</p>
    		</div>
		</div>
	</jsp:attribute>
</t:genericpage>