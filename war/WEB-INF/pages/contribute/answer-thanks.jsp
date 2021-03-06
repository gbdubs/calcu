<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | Thank You!
	</jsp:attribute>

	<jsp:attribute name="content">
		<div class="error-page thank-you">
    		<h2 class="headline text-info">Thanks!</h2>
    		<div class="error-content">
        		<h3>(For Your Answer!)</h3>
        		<p>
            		Thank you for submitting an Answer!  If you submitted anonymously, this is the last
            		you will hear about it. 
            		Feel free to <a href="/contribute/practice-problem/dashboard">see your answers</a> or <a href="/contribute/">return home</a>.
        		</p>
    		</div>
		</div>
	</jsp:attribute>
</t:genericpage>