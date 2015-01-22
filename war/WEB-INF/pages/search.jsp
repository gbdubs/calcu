<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | Search
	</jsp:attribute>
	<jsp:attribute name="content">
		<div class="box box-solid">
			<div class="box-header">
				<h3 class="box-title">Search All The Best<small>  Search by Tag to see suggestions for which there are practice problems, questions, and original content</small></h3>
			</div>
			<div class="box-body">
				<label for="tags-input">Tags/Categories To Search</label>
				<input name="tagsInput" id="tags-input" value="${practiceProblem.tags}" />
			</div>
		</div>	
	</jsp:attribute>	
	<jsp:attribute name="javascript">
		<script src="/_static/js/plugins/jQuery-Tags-Input-master/jquery.tagsinput.min.js"></script>
		<link rel="stylesheet" type="text/css" href="jquery.tagsinput.css" />
		<script src="/_static/js/CalcU/searchByTag.js"></script>
	</jsp:attribute>
</t:genericpage>
	