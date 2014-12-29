<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericpage>
	<jsp:attribute name="content">
		<div class="error-page">
    		<h2 class="headline text-info">403</h2>
    		<div class="error-content">
        		<h3>Somebody has been looking for something they shouldn't...</h3>
        		<p>
            		You do not have permission to view the page that you have requested.  If you think you should have access to 
            		this page, you should try to <a class="login-button">log in</a>, and see if that corrects the error. 
        		</p>
        		<form class="search-form">
            		<div class="input-group">
                		<input type="text" name="search" class="form-control" placeholder="Search">
                		<div class="input-group-btn">
                    		<button type="submit" name="submit" class="btn btn-primary"><i class="fa fa-search"></i></button>
                		</div>
    		        </div>
        		</form>
    		</div>
		</div>
	</jsp:attribute>
</t:genericpage>