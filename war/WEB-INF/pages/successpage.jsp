<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | Sandbox
	</jsp:attribute>
	<jsp:attribute name="content">
<div class="box box-solid">
	<div class="box-header">
		<h3 class="box-title">Practice Problem by <a>UsernameGoesHere</a> <small>on 12/12/12 12:12</small> </h3>
	</div>
	<div class="box-body">
		<div class="box-group" id="accordion">
			<div class="box box-primary">
				<div class="box-header">
					<i class="fa fa-question fa-karma-score"><span class="karma-score karma-green">  +22 </span></i>
					<h4 class="box-title">
						This is the title to the question
					</h4>
				</div>
				<div class="box-body">
					<jsp:include page="/WEB-INF/templates/rating-system-h.jsp">
						<jsp:param name="contentId" value="You dont need no id" />
						<jsp:param name="formPostUrl" value="/UPLOADURLLLLL" />
					</jsp:include>
					Anim pariatur cliche reprehenderit, enim eiusmod high life accusamus terry richardson ad squid. 3 wolf moon officia aute, non cupidatat skateboard dolor brunch. Food truck quinoa nesciunt laborum eiusmod. Brunch 3 wolf moon tempor, sunt aliqua put a bird on it squid single-origin coffee nulla assumenda shoreditch et. Nihil anim keffiyeh helvetica, craft beer labore wes anderson cred nesciunt sapiente ea proident. Ad vegan excepteur butcher vice lomo. Leggings occaecat craft beer farm-to-table, raw denim aesthetic synth nesciunt you probably haven't heard of them accusamus labore sustainable VHS.
				</div>
			</div>
			<div class="panel box box-primary">				
				<div class="box-header">
					<i class="fa fa-bullseye fa-karma-score"><span class="karma-score">  +22 </span></i>
					<h4 class="box-title">
						<a data-toggle="collapse" data-parent="#accordion" href="#collapseOne" class="collapsed">
							Author's Solution
						</a>
					</h4>
				</div>
				<div id="collapseOne" class="panel-collapse collapse in">
					<div class="box-body">
						Anim pariatur cliche reprehenderit, enim eiusmod high life accusamus terry richardson ad squid. 3 wolf moon officia aute, non cupidatat skateboard dolor brunch. Food truck quinoa nesciunt laborum eiusmod. Brunch 3 wolf moon tempor, sunt aliqua put a bird on it squid single-origin coffee nulla assumenda shoreditch et. Nihil anim keffiyeh helvetica, craft beer labore wes anderson cred nesciunt sapiente ea proident. Ad vegan excepteur butcher vice lomo. Leggings occaecat craft beer farm-to-table, raw denim aesthetic synth nesciunt you probably haven't heard of them accusamus labore sustainable VHS.
					</div>
				</div>
			</div>
			<div class="panel box box-success">
				<div class="box-header">
					<i class="fa fa-check fa-karma-score"><span class="karma-score">  +22 </span></i>
					<h4 class="box-title">
						<a data-toggle="collapse" data-parent="#accordion" href="#collapseTwo" class="collapsed">
							Approved Solution
						</a>
					</h4>
				</div>
				<div id="collapseTwo" class="panel-collapse collapse" style="height: 0px;">
					<div class="box-body">
						Anim pariatur cliche reprehenderit, enim eiusmod high life accusamus terry richardson ad squid. 3 wolf moon officia aute, non cupidatat skateboard dolor brunch. Food truck quinoa nesciunt laborum eiusmod. Brunch 3 wolf moon tempor, sunt aliqua put a bird on it squid single-origin coffee nulla assumenda shoreditch et. Nihil anim keffiyeh helvetica, craft beer labore wes anderson cred nesciunt sapiente ea proident. Ad vegan excepteur butcher vice lomo. Leggings occaecat craft beer farm-to-table, raw denim aesthetic synth nesciunt you probably haven't heard of them accusamus labore sustainable VHS.
					</div>
				</div>
			</div>
			<div class="panel box box-warning">
				<div class="box-header">
					<h4 class="box-title">
						<i class="fa fa-lightbulb-o fa-karma-score"><span class="karma-score">  +22 </span></i>
						<a data-toggle="collapse" data-parent="#accordion" href="#collapseThree" class="collapsed">
							Other Solution
						</a>
					</h4>
				</div>
				<div id="collapseThree" class="panel-collapse collapse" style="height: 0px;">
					<div class="box-body">
						Anim pariatur cliche reprehenderit, enim eiusmod high life accusamus terry richardson ad squid. 3 wolf moon officia aute, non cupidatat skateboard dolor brunch. Food truck quinoa nesciunt laborum eiusmod. Brunch 3 wolf moon tempor, sunt aliqua put a bird on it squid single-origin coffee nulla assumenda shoreditch et. Nihil anim keffiyeh helvetica, craft beer labore wes anderson cred nesciunt sapiente ea proident. Ad vegan excepteur butcher vice lomo. Leggings occaecat craft beer farm-to-table, raw denim aesthetic synth nesciunt you probably haven't heard of them accusamus labore sustainable VHS.
					</div>
				</div>
			</div>
			<div class="panel box box-danger">
				<div class="box-header">
					<h4 class="box-title">
						<i class="fa fa-university fa-karma-score"><span class="karma-score">  +? </span></i>
						<a data-toggle="collapse" data-parent="#accordion" href="#collapseFour" class="collapsed">
							Answer this Problem
						</a>
					</h4>
				</div>
				<div id="collapseFour" class="panel-collapse collapse" style="height: 0px;">
					<div class="box-body">
						Anim pariatur cliche reprehenderit, enim eiusmod high life accusamus terry richardson ad squid. 3 wolf moon officia aute, non cupidatat skateboard dolor brunch. Food truck quinoa nesciunt laborum eiusmod. Brunch 3 wolf moon tempor, sunt aliqua put a bird on it squid single-origin coffee nulla assumenda shoreditch et. Nihil anim keffiyeh helvetica, craft beer labore wes anderson cred nesciunt sapiente ea proident. Ad vegan excepteur butcher vice lomo. Leggings occaecat craft beer farm-to-table, raw denim aesthetic synth nesciunt you probably haven't heard of them accusamus labore sustainable VHS.
					</div>
				</div>
			</div>
			
		</div>
	</div>
</div>
	</jsp:attribute>
	
</t:genericpage>