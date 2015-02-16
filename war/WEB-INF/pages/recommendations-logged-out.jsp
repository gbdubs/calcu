<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | Recommendations
	</jsp:attribute>

	<jsp:attribute name="content">
		
		<div class="box box-solid">
			<div class="box-header bg-olive">
				<i class="fa fa-tasks"></i>
				<h3 class="box-title">Recommendations</h3>
			</div>
			<div class="box-body">
				<p>
					This is the recommendation page, where we take everything you tell us about yourself, and
					try to synthesize it in to the resources which will best be able to help you learn. We learn
					about you through the <a href="/personalize">personalize page</a>, as well as through what you
					rate, what you bookmark, and what you answer. 
				</p>
				<p>
					Once you log-in, and play around a bit, this page will host a wide array of recommendations.
					You will be able to:
					<ul>
						<li> Bookmark the content so that you can come back to it later </li>
						<li> Hide the content if you have seen it before </li>
						<li> Tell us you aren't interested in the subject matter of the recommendation
						so that we can make it better </li>
					</ul>
					Also on this page, we will be able to un-hide all of the recommendations that you have previously
					hidden. 
				</p>
				<p>
					But we can't give you any suggestions if we don't know anything about you! Log in using the link below
					to begin the experience!
				</p>
				<button class="btn btn-block btn-success login-button">Log In</button>
			</div>
		</div>
	</jsp:attribute>
	<jsp:attribute name="javascript">
		<script src="/_static/js/CalcU/recommendations.js"></script>
	</jsp:attribute>
</t:genericpage>