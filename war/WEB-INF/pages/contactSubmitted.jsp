<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericpage>
	<jsp:attribute name="content">
		<div class="error-page thank-you">
    		<h2 class="headline text-info">Thanks!</h2>
    		<div class="error-content">
        		<h3>Thank you for submitting feedback!</h3>
        		<p>
            		If you have requested it, we will try to get back to you with a response as soon as possible. Otherwise,
            		thank you for taking the time to submit your thoughts on this evolving project.  
            		Feel free to navigate <a href="/home">home</a> or <a href="/contact">submit more feedback</a>.
        		</p>
    		</div>
		</div>
	</jsp:attribute>
</t:genericpage>