<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | Page Not Found
	</jsp:attribute>

	<jsp:attribute name="content">
		<div class="error-page">
    		<h2 class="headline text-info">404</h2>
    		<div class="error-content">
        		<h3>Oops! This is not the page you are looking for!</h3>
        		<p>
            		We could not find the page you were looking for. Or maybe you should move along and not ask questions.
            		We apologize, but fee free to <a href="/home">return to the home page</a> or try using the search form.
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