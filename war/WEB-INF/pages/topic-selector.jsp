<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericpage>
	<jsp:attribute name="pageTitle">
		CalcU | Topic Selector
	</jsp:attribute>

	<jsp:attribute name="content">
		<div class="centered">
			<div class="max-width-at-1000 topic-selector-page">
				<h2 class="welcome-banner margin-top-100">Topic Selector</h2>
				<h3 class="margin-bottom-50 welcome-banner-subtitle">Choose a topic you're interested in, and we will find related content, practice problems, and examples for you.</h3>
				<div class="">
				<!-- FIRST COLUMN --> 
				<div class="col-lg-3 col-md-4 col-sm-4">
					<div class="box box-primary box-solid">
						<div class="box-header centered">
							<h3 class="box-title float-none">Topics</h3>
						</div>
						<div class="box-body align-left">
							<a id="ts-geometry" class="btn btn-block btn-default  ">
								Geometry
							</a>
							<a id="ts-algebra" class="btn btn-block btn-default  ">
								Algebra
							</a>
							<a id="ts-trigonometry" class="btn btn-block btn-default ">
								Trigonometry
							</a>
							<a id="ts-calculus" class="btn btn-block btn-default  ">
								Calculus
							</a>
						</div>
					</div>
				</div>
				
				<!-- SECOND COLUMN --> 
				<div class="col-lg-3 col-md-4 col-sm-4">
					<div id="ts-geometry-box" class="box box-success box-solid topic-box hidden">
						<div class="box-header centered topic-selector-header">
							<h3 class="box-title float-none">Geometry</h3>
						</div>
						<div class="box-body align-left">
							<a id="ts-polygons" class="btn btn-block btn-default ">
								Polygons
							</a>
							<a id="ts-points_lines" class="btn btn-block btn-default">
								Points Lines
							</a>
							<a id="ts-solids" class="btn btn-block btn-default">
								Solids
							</a>
							<a id="ts-coordinate_geometry" class="btn btn-block btn-default">
								Coordinate Geometry
							</a>
						</div>
					</div>
					<div id="ts-calculus-box" class="box box-success box-solid topic-box hidden">
						<div class="box-header centered topic-selector-header">
							<h3 class="box-title float-none">Calculus</h3>
						</div>
						<div class="box-body align-left">
							<a id="ts-derivatives" class="btn btn-block btn-default ">
								Derivatives
							</a>
							<a id="ts-antiderivatives" class="btn btn-block btn-default">
								Antiderivatives
							</a>
							<a id="ts-integration" class="btn btn-block btn-default">
								Integration
							</a>
							<a id="ts-rates_of_change" class="btn btn-block btn-default">
								Rates of Change
							</a>
						</div>
					</div>
					<div id="ts-algebra-box" class="box box-success box-solid topic-box hidden">
						<div class="box-header centered topic-selector-header">
							<h3 class="box-title float-none">Algebra</h3>
						</div>
						<div class="box-body align-left">
							<a id="ts-functions" class="btn btn-block btn-default ">
								Functions
							</a>
							<a id="ts-linear_systems" class="btn btn-block btn-default">
								Linear Systems
							</a>
							<a id="ts-matrices" class="btn btn-block btn-default">
								Matrices
							</a>
							<a id="ts-quadratic_equations" class="btn btn-block btn-default">
								Quadratic Equations
							</a>
						</div>
					</div>
				</div>
				
				<!-- THIRD COLUMN --> 
				
				<div class="col-lg-3 col-md-4 col-sm-4">
					<div id="ts-polygons-box" class="box box-success box-solid topic-box hidden">
						<div class="box-header centered topic-selector-header">
							<h3 class="box-title float-none">Polygons</h3>
						</div>
						<div class="box-body align-left">
							<a id="ts-triangles" class="btn btn-block btn-default ">
								Triangles
							</a>
							<a id="ts-quadrilaterals" class="btn btn-block btn-default ">
								Quadrilaterals
							</a>
							<a id="ts-squares" class="btn btn-block btn-default">
								Squares
							</a>
							<a id="ts-rectangles" class="btn btn-block btn-default">
								Rectangles
							</a>
						</div>
					</div>
					<div id="ts-derivatives-box" class="box box-success box-solid topic-box hidden">
						<div class="box-header centered topic-selector-header">
							<h3 class="box-title float-none">Derivatives</h3>
						</div>
						<div class="box-body align-left">
							<a id="ts-derivatives1" class="btn btn-block btn-default ">
								Derivatives1
							</a>
							<a id="ts-derivatives2" class="btn btn-block btn-default">
								Derivatives2
							</a>
							<a id="ts-derivatives3" class="btn btn-block btn-default">
								Derivatives3
							</a>
							<a id="ts-more_derivatives" class="btn btn-block btn-default">
								More Derivatives
							</a>
						</div>
					</div>
					<div id="ts-functions-box" class="box box-success box-solid topic-box hidden">
						<div class="box-header centered topic-selector-header">
							<h3 class="box-title float-none">Functions</h3>
						</div>
						<div class="box-body align-left">
							<a id="ts-relations" class="btn btn-block btn-default ">
								Relations
							</a>
							<a id="ts-linear_equations" class="btn btn-block btn-default">
								Linear Equations
							</a>
							<a id="ts-linear_functions" class="btn btn-block btn-default">
								Linear Functions
							</a>
							<a id="ts-linear_models" class="btn btn-block btn-default">
								Linear Models
							</a>
						</div>
					</div>
				</div>
				
				
				<!-- FOURTH COLUMN -->
				<div class="col-lg-3 col-md-4 col-sm-4">
					<div id="ts-triangles-box" class="box box-success box-solid topic-box hidden">
						<div class="box-header centered topic-selector-header">
							<h3 class="box-title float-none">Triangles</h3>
						</div>
						<div class="box-body align-left">
							<a id="ts-right_triangles" class="btn btn-block btn-default btn-selected ">
								Right Triangles
							</a>
							<a id="ts-congruent_triangles" class="btn btn-block btn-default">
								Congruent Triangles
							</a>
							<a id="ts-isosceles_triangle" class="btn btn-block btn-default">
								Isosceles Triangle
							</a>
							<a id="ts-scalene_triangle" class="btn btn-block btn-default">
								Scalene Triangle
							</a>
						</div>
					</div>
			
					<div id="ts-more_derivatives-box" class="box box-success box-solid topic-box hidden">
						<div class="box-header centered topic-selector-header">
							<h3 class="box-title float-none">More Derivatives</h3>
						</div>
						<div class="box-body align-left">
							<a id="ts-derivatives4" class="btn btn-block btn-default ">
								Derivatives4
							</a>
							<a id="ts-derivatives5" class="btn btn-block btn-default">
								Derivatives5
							</a>
							<a id="ts-derivatives6" class="btn btn-block btn-default">
								Derivatives6
							</a>
							<a id="ts-mo_derivatives" class="btn btn-block btn-default">
								Mo' Derivatives
							</a>
						</div>
					</div>
				
					<div id="ts-linear_models-box" class="box box-success box-solid topic-box hidden">
						<div class="box-header centered topic-selector-header">
							<h3 class="box-title float-none">Linear Models</h3>
						</div>
						<div class="box-body align-left">
							<a id="ts-linear_models2" class="btn btn-block btn-default">
								Linear Models2
							</a>
							<a id="ts-congruent_triangles" class="btn btn-block btn-default">
								Congruent Triangles
							</a>
							<a id="ts-isosceles_triangle" class="btn btn-block btn-default">
								Isosceles Triangle
							</a>
							<a id="ts-scalene_triangle" class="btn btn-block btn-default">
								Scalene Triangle
							</a>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div id="ts-geometry-info-box" class="max-width-at-1000 align-left hidden ts-info-box">
			<div class = "box box-primary box-solid">
				<div class="box-header">
					<div class="align-right">
						<h3 class="box-title">Topic Currently Selected: Geometry</h3>
						<div class="padding-top-4 margin-right-5">
							<input class="btn btn-danger" type="submit" value="Edit Topic">
							<input class="btn btn-success" type="submit" value="See Full Topic">
						</div>
					</div>
				</div>
				<div class="box-body">
					<div class="margin-left-100 margin-top-10 centered">
						<input class="btn btn-warning large-input-group-button margin-10" type="submit" value="See 18 Pieces of Related Content">
						<input class="btn btn-success large-input-group-button margin-10" type="submit" value="See 18 Related Practice Problems">
						<input class="btn btn-primary large-input-group-button margin-10" type="submit" value="See 18 Related Exercies">
					</div>
					<div class="alert alert-success margin-top-10">
						<button type="button" class="toggle-bookmark-button pull-right buttonless" data-action="add" data-user="185804764220139124118" data-content="7ac906de-de49-44bf-a145-7992baea248c">
							<i class="fa fa-bookmark-o"></i>
						</button>
						<a href="/practice-problem/dfd3fd7d-baae-43d4-949b-0bdcd9e0e36e"><b>Geometry Practice</b></a> 
							Find the power of the point (2,-1) with respect to 
							<span class="MathJax_Preview" style="color: inherit;"></span>
							<span class="MathJax" id="MathJax-Element-55-Frame"><nobr><span class="math" id="MathJax-Span-1161" role="math" style="width: 13.73em; display: inline-block;"><span style="display: inline-block; position: relative; width: 11.929em; height: 0px; font-size: 115%;"><span style="position: absolute; clip: rect(1.432em 1000.003em 2.86em -999.997em); top: -2.481em; left: 0.003em;"><span class="mrow" id="MathJax-Span-1162"><span class="mn" id="MathJax-Span-1163" style="font-family: STIXGeneral-Regular;">3</span><span class="msubsup" id="MathJax-Span-1164"><span style="display: inline-block; position: relative; width: 0.935em; height: 0px;"><span style="position: absolute; clip: rect(3.357em 1000.003em 4.165em -999.997em); top: -3.972em; left: 0.003em;"><span class="mi" id="MathJax-Span-1165" style="font-family: STIXGeneral-Italic;">x<span style="display: inline-block; overflow: hidden; height: 1px; width: 0.003em;"></span></span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span><span style="position: absolute; top: -4.345em; left: 0.5em;"><span class="texatom" id="MathJax-Span-1166"><span class="mrow" id="MathJax-Span-1167"><span class="mn" id="MathJax-Span-1168" style="font-size: 70.7%; font-family: STIXGeneral-Regular;">2</span></span></span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span></span></span><span class="mo" id="MathJax-Span-1169" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">+</span><span class="mn" id="MathJax-Span-1170" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">3</span><span class="msubsup" id="MathJax-Span-1171"><span style="display: inline-block; position: relative; width: 0.873em; height: 0px;"><span style="position: absolute; clip: rect(3.357em 1000.003em 4.351em -999.997em); top: -3.972em; left: 0.003em;"><span class="mi" id="MathJax-Span-1172" style="font-family: STIXGeneral-Italic;">y</span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span><span style="position: absolute; top: -4.345em; left: 0.438em;"><span class="texatom" id="MathJax-Span-1173"><span class="mrow" id="MathJax-Span-1174"><span class="mn" id="MathJax-Span-1175" style="font-size: 70.7%; font-family: STIXGeneral-Regular;">2</span></span></span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span></span></span><span class="mo" id="MathJax-Span-1176" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">+</span><span class="mn" id="MathJax-Span-1177" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">4</span><span class="mi" id="MathJax-Span-1178" style="font-family: STIXGeneral-Italic;">x<span style="display: inline-block; overflow: hidden; height: 1px; width: 0.003em;"></span></span><span class="mo" id="MathJax-Span-1179" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">+</span><span class="mn" id="MathJax-Span-1180" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">2</span><span class="mi" id="MathJax-Span-1181" style="font-family: STIXGeneral-Italic;">y</span><span class="mo" id="MathJax-Span-1182" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">+</span><span class="mn" id="MathJax-Span-1183" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">6</span><span class="mo" id="MathJax-Span-1184" style="font-family: STIXGeneral-Regular; padding-left: 0.314em;">=</span><span class="mn" id="MathJax-Span-1185" style="font-family: STIXGeneral-Regular; padding-left: 0.314em;">0</span><span class="mspace" id="MathJax-Span-1186" style="height: 0.003em; vertical-align: 0.003em; width: 0.189em; display: inline-block; overflow: hidden;"></span></span><span style="display: inline-block; width: 0px; height: 2.488em;"></span></span></span><span style="border-left-width: 0.004em; border-left-style: solid; display: inline-block; overflow: hidden; width: 0px; height: 1.361em; vertical-align: -0.282em;"></span></span></nobr></span>
							<script type="math/tex" id="MathJax-Element-55"> 3x^{2}+3y^{2}+4x+2y+6=0\, </script>
					</div>
					<div class="alert alert-success">
						<button type="button" class="toggle-bookmark-button pull-right buttonless" data-action="add" data-user="185804764220139124118" data-content="7ac906de-de49-44bf-a145-7992baea248c">
							<i class="fa fa-bookmark-o"></i>
						</button>
						<a href="/practice-problem/4408c3fa-a3c8-4557-88e9-d958549ec711"><b>Geometry Practice</b></a>
						The distance of a point <span class="MathJax_Preview" style="color: inherit;"></span><span class="MathJax" id="MathJax-Element-76-Frame"><nobr><span class="math" id="MathJax-Span-1612" role="math" style="width: 3.295em; display: inline-block;"><span style="display: inline-block; position: relative; width: 2.86em; height: 0px; font-size: 115%;"><span style="position: absolute; clip: rect(1.618em 1000.003em 2.86em -999.997em); top: -2.481em; left: 0.003em;"><span class="mrow" id="MathJax-Span-1613"><span class="mi" id="MathJax-Span-1614" style="font-family: STIXGeneral-Italic;">P</span><span class="mo" id="MathJax-Span-1615" style="font-family: STIXGeneral-Regular;">(</span><span class="mi" id="MathJax-Span-1616" style="font-family: STIXGeneral-Italic;">h</span><span class="mo" id="MathJax-Span-1617" style="font-family: STIXGeneral-Regular;">,</span><span class="mi" id="MathJax-Span-1618" style="font-family: STIXGeneral-Italic; padding-left: 0.189em;">k<span style="display: inline-block; overflow: hidden; height: 1px; width: 0.003em;"></span></span><span class="mo" id="MathJax-Span-1619" style="font-family: STIXGeneral-Regular;">)</span><span class="mspace" id="MathJax-Span-1620" style="height: 0.003em; vertical-align: 0.003em; width: 0.189em; display: inline-block; overflow: hidden;"></span></span><span style="display: inline-block; width: 0px; height: 2.488em;"></span></span></span><span style="border-left-width: 0.004em; border-left-style: solid; display: inline-block; overflow: hidden; width: 0px; height: 1.146em; vertical-align: -0.282em;"></span></span></nobr></span>
						<script type="math/tex" id="MathJax-Element-76"> P(h,k)\, </script>
						from a pair of lines passing thro'the origin is d units.Show that the equation of the pair of lines is 
						<span class="MathJax_Preview" style="color: inherit;"></span>
						<span class="MathJax" id="MathJax-Element-77-Frame"><nobr><span class="math" id="MathJax-Span-1621" role="math" style="width: 11.804em; display: inline-block;"><span style="display: inline-block; position: relative; width: 10.252em; height: 0px; font-size: 115%;"><span style="position: absolute; clip: rect(1.432em 1000.003em 2.86em -999.997em); top: -2.481em; left: 0.003em;"><span class="mrow" id="MathJax-Span-1622"><span class="mo" id="MathJax-Span-1623" style="font-family: STIXGeneral-Regular;">(</span><span class="mi" id="MathJax-Span-1624" style="font-family: STIXGeneral-Italic;">x<span style="display: inline-block; overflow: hidden; height: 1px; width: 0.003em;"></span></span><span class="mi" id="MathJax-Span-1625" style="font-family: STIXGeneral-Italic;">k<span style="display: inline-block; overflow: hidden; height: 1px; width: 0.003em;"></span></span><span class="mo" id="MathJax-Span-1626" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">−</span><span class="mi" id="MathJax-Span-1627" style="font-family: STIXGeneral-Italic; padding-left: 0.252em;">h</span><span class="mi" id="MathJax-Span-1628" style="font-family: STIXGeneral-Italic;">y</span><span class="msubsup" id="MathJax-Span-1629"><span style="display: inline-block; position: relative; width: 0.748em; height: 0px;"><span style="position: absolute; clip: rect(3.109em 1000.003em 4.351em -999.997em); top: -3.972em; left: 0.003em;"><span class="mo" id="MathJax-Span-1630" style="font-family: STIXGeneral-Regular;">)</span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span><span style="position: absolute; top: -4.345em; left: 0.314em;"><span class="texatom" id="MathJax-Span-1631"><span class="mrow" id="MathJax-Span-1632"><span class="mn" id="MathJax-Span-1633" style="font-size: 70.7%; font-family: STIXGeneral-Regular;">2</span></span></span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span></span></span><span class="mo" id="MathJax-Span-1634" style="font-family: STIXGeneral-Regular; padding-left: 0.314em;">=</span><span class="msubsup" id="MathJax-Span-1635" style="padding-left: 0.314em;"><span style="display: inline-block; position: relative; width: 0.997em; height: 0px;"><span style="position: absolute; clip: rect(3.109em 1000.003em 4.165em -999.997em); top: -3.972em; left: 0.003em;"><span class="mi" id="MathJax-Span-1636" style="font-family: STIXGeneral-Italic;">d<span style="display: inline-block; overflow: hidden; height: 1px; width: 0.003em;"></span></span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span><span style="position: absolute; top: -4.345em; left: 0.562em;"><span class="texatom" id="MathJax-Span-1637"><span class="mrow" id="MathJax-Span-1638"><span class="mn" id="MathJax-Span-1639" style="font-size: 70.7%; font-family: STIXGeneral-Regular;">2</span></span></span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span></span></span><span class="mo" id="MathJax-Span-1640" style="font-family: STIXGeneral-Regular;">(</span><span class="msubsup" id="MathJax-Span-1641"><span style="display: inline-block; position: relative; width: 0.935em; height: 0px;"><span style="position: absolute; clip: rect(3.357em 1000.003em 4.165em -999.997em); top: -3.972em; left: 0.003em;"><span class="mi" id="MathJax-Span-1642" style="font-family: STIXGeneral-Italic;">x<span style="display: inline-block; overflow: hidden; height: 1px; width: 0.003em;"></span></span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span><span style="position: absolute; top: -4.345em; left: 0.5em;"><span class="texatom" id="MathJax-Span-1643"><span class="mrow" id="MathJax-Span-1644"><span class="mn" id="MathJax-Span-1645" style="font-size: 70.7%; font-family: STIXGeneral-Regular;">2</span></span></span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span></span></span><span class="mo" id="MathJax-Span-1646" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">+</span><span class="msubsup" id="MathJax-Span-1647" style="padding-left: 0.252em;"><span style="display: inline-block; position: relative; width: 0.873em; height: 0px;"><span style="position: absolute; clip: rect(3.357em 1000.003em 4.351em -999.997em); top: -3.972em; left: 0.003em;"><span class="mi" id="MathJax-Span-1648" style="font-family: STIXGeneral-Italic;">y</span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span><span style="position: absolute; top: -4.345em; left: 0.438em;"><span class="texatom" id="MathJax-Span-1649"><span class="mrow" id="MathJax-Span-1650"><span class="mn" id="MathJax-Span-1651" style="font-size: 70.7%; font-family: STIXGeneral-Regular;">2</span></span></span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span></span></span><span class="mo" id="MathJax-Span-1652" style="font-family: STIXGeneral-Regular;">)</span><span class="mspace" id="MathJax-Span-1653" style="height: 0.003em; vertical-align: 0.003em; width: 0.189em; display: inline-block; overflow: hidden;"></span></span><span style="display: inline-block; width: 0px; height: 2.488em;"></span></span></span><span style="border-left-width: 0.004em; border-left-style: solid; display: inline-block; overflow: hidden; width: 0px; height: 1.361em; vertical-align: -0.282em;"></span></span></nobr></span>
						<script type="math/tex" id="MathJax-Element-77"> (xk-hy)^{2}=d^{2}(x^{2}+y^{2})\, </script>
					</div>
					<div class="alert alert-success">							
						<button type="button" class="toggle-bookmark-button pull-right buttonless" data-action="add" data-user="185804764220139124118" data-content="af335f4c-8538-4c46-87cf-62bee879c31b">
							<i class="fa fa-bookmark-o"></i>
						</button>
						<a href="/practice-problem/af335f4c-8538-4c46-87cf-62bee879c31b"><b>Geometry Practice</b></a>
							Find the equation of tangents to the ellipse <span class="MathJax_Preview" style="color: inherit;"></span><span class="MathJax" id="MathJax-Element-31-Frame"><nobr><span class="math" id="MathJax-Span-747" role="math" style="width: 6.401em; display: inline-block;"><span style="display: inline-block; position: relative; width: 5.531em; height: 0px; font-size: 115%;"><span style="position: absolute; clip: rect(1.432em 1000.003em 2.86em -999.997em); top: -2.481em; left: 0.003em;"><span class="mrow" id="MathJax-Span-748"><span class="mn" id="MathJax-Span-749" style="font-family: STIXGeneral-Regular;">2</span><span class="msubsup" id="MathJax-Span-750"><span style="display: inline-block; position: relative; width: 0.935em; height: 0px;"><span style="position: absolute; clip: rect(3.357em 1000.003em 4.165em -999.997em); top: -3.972em; left: 0.003em;"><span class="mi" id="MathJax-Span-751" style="font-family: STIXGeneral-Italic;">x<span style="display: inline-block; overflow: hidden; height: 1px; width: 0.003em;"></span></span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span><span style="position: absolute; top: -4.345em; left: 0.5em;"><span class="texatom" id="MathJax-Span-752"><span class="mrow" id="MathJax-Span-753"><span class="mn" id="MathJax-Span-754" style="font-size: 70.7%; font-family: STIXGeneral-Regular;">2</span></span></span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span></span></span><span class="mo" id="MathJax-Span-755" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">+</span><span class="msubsup" id="MathJax-Span-756" style="padding-left: 0.252em;"><span style="display: inline-block; position: relative; width: 0.873em; height: 0px;"><span style="position: absolute; clip: rect(3.357em 1000.003em 4.351em -999.997em); top: -3.972em; left: 0.003em;"><span class="mi" id="MathJax-Span-757" style="font-family: STIXGeneral-Italic;">y</span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span><span style="position: absolute; top: -4.345em; left: 0.438em;"><span class="texatom" id="MathJax-Span-758"><span class="mrow" id="MathJax-Span-759"><span class="mn" id="MathJax-Span-760" style="font-size: 70.7%; font-family: STIXGeneral-Regular;">2</span></span></span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span></span></span><span class="mo" id="MathJax-Span-761" style="font-family: STIXGeneral-Regular; padding-left: 0.314em;">=</span><span class="mn" id="MathJax-Span-762" style="font-family: STIXGeneral-Regular; padding-left: 0.314em;">8</span><span class="mspace" id="MathJax-Span-763" style="height: 0.003em; vertical-align: 0.003em; width: 0.189em; display: inline-block; overflow: hidden;"></span></span><span style="display: inline-block; width: 0px; height: 2.488em;"></span></span></span><span style="border-left-width: 0.004em; border-left-style: solid; display: inline-block; overflow: hidden; width: 0px; height: 1.361em; vertical-align: -0.282em;"></span></span></nobr></span><script type="math/tex" id="MathJax-Element-31"> 2x^{2}+y^{2}=8\, </script> which is parallel to <span class="MathJax_Preview" style="color: inherit;"></span><span class="MathJax" id="MathJax-Element-32-Frame"><nobr><span class="math" id="MathJax-Span-764" role="math" style="width: 7.208em; display: inline-block;"><span style="display: inline-block; position: relative; width: 6.276em; height: 0px; font-size: 115%;"><span style="position: absolute; clip: rect(1.618em 1000.003em 2.86em -999.997em); top: -2.481em; left: 0.003em;"><span class="mrow" id="MathJax-Span-765"><span class="mi" id="MathJax-Span-766" style="font-family: STIXGeneral-Italic;">x<span style="display: inline-block; overflow: hidden; height: 1px; width: 0.003em;"></span></span><span class="mo" id="MathJax-Span-767" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">−</span><span class="mn" id="MathJax-Span-768" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">2</span><span class="mi" id="MathJax-Span-769" style="font-family: STIXGeneral-Italic;">y</span><span class="mo" id="MathJax-Span-770" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">−</span><span class="mn" id="MathJax-Span-771" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">4</span><span class="mo" id="MathJax-Span-772" style="font-family: STIXGeneral-Regular; padding-left: 0.314em;">=</span><span class="mn" id="MathJax-Span-773" style="font-family: STIXGeneral-Regular; padding-left: 0.314em;">0</span><span class="mspace" id="MathJax-Span-774" style="height: 0.003em; vertical-align: 0.003em; width: 0.189em; display: inline-block; overflow: hidden;"></span></span><span style="display: inline-block; width: 0px; height: 2.488em;"></span></span></span><span style="border-left-width: 0.004em; border-left-style: solid; display: inline-block; overflow: hidden; width: 0px; height: 1.146em; vertical-align: -0.282em;"></span></span></nobr></span><script type="math/tex" id="MathJax-Element-32"> x-2y-4=0\, </script> .
					</div>
				</div>
			</div>
		</div>

		<div id="ts-algebra-info-box" class="max-width-at-1000 align-left hidden ts-info-box">
			<div class = "box box-primary box-solid">
				<div class="box-header">
					<div class="align-right">
						<h3 class="box-title">Topic Currently Selected: Algebra</h3>
						<div class="padding-top-4 margin-right-5">
							<input class="btn btn-danger" type="submit" value="Edit Topic">
							<input class="btn btn-success" type="submit" value="See Full Topic">
						</div>
					</div>
				</div>
				<div class="box-body">
					<div class="margin-left-100 margin-top-10 centered">
						<input class="btn btn-warning large-input-group-button margin-10" type="submit" value="See 18 Pieces of Related Content">
						<input class="btn btn-success large-input-group-button margin-10" type="submit" value="See 18 Related Practice Problems">
						<input class="btn btn-primary large-input-group-button margin-10" type="submit" value="See 18 Related Exercies">
					</div>
					<div class="alert alert-success margin-top-10">
						<button type="button" class="toggle-bookmark-button pull-right buttonless" data-action="add" data-user="185804764220139124118" data-content="7ac906de-de49-44bf-a145-7992baea248c">
							<i class="fa fa-bookmark-o"></i>
						</button>
						<a href="/practice-problem/dfd3fd7d-baae-43d4-949b-0bdcd9e0e36e"><b>Geometry Practice</b></a> 
							Find the power of the point (2,-1) with respect to 
							<span class="MathJax_Preview" style="color: inherit;"></span>
							<span class="MathJax" id="MathJax-Element-55-Frame"><nobr><span class="math" id="MathJax-Span-1161" role="math" style="width: 13.73em; display: inline-block;"><span style="display: inline-block; position: relative; width: 11.929em; height: 0px; font-size: 115%;"><span style="position: absolute; clip: rect(1.432em 1000.003em 2.86em -999.997em); top: -2.481em; left: 0.003em;"><span class="mrow" id="MathJax-Span-1162"><span class="mn" id="MathJax-Span-1163" style="font-family: STIXGeneral-Regular;">3</span><span class="msubsup" id="MathJax-Span-1164"><span style="display: inline-block; position: relative; width: 0.935em; height: 0px;"><span style="position: absolute; clip: rect(3.357em 1000.003em 4.165em -999.997em); top: -3.972em; left: 0.003em;"><span class="mi" id="MathJax-Span-1165" style="font-family: STIXGeneral-Italic;">x<span style="display: inline-block; overflow: hidden; height: 1px; width: 0.003em;"></span></span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span><span style="position: absolute; top: -4.345em; left: 0.5em;"><span class="texatom" id="MathJax-Span-1166"><span class="mrow" id="MathJax-Span-1167"><span class="mn" id="MathJax-Span-1168" style="font-size: 70.7%; font-family: STIXGeneral-Regular;">2</span></span></span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span></span></span><span class="mo" id="MathJax-Span-1169" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">+</span><span class="mn" id="MathJax-Span-1170" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">3</span><span class="msubsup" id="MathJax-Span-1171"><span style="display: inline-block; position: relative; width: 0.873em; height: 0px;"><span style="position: absolute; clip: rect(3.357em 1000.003em 4.351em -999.997em); top: -3.972em; left: 0.003em;"><span class="mi" id="MathJax-Span-1172" style="font-family: STIXGeneral-Italic;">y</span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span><span style="position: absolute; top: -4.345em; left: 0.438em;"><span class="texatom" id="MathJax-Span-1173"><span class="mrow" id="MathJax-Span-1174"><span class="mn" id="MathJax-Span-1175" style="font-size: 70.7%; font-family: STIXGeneral-Regular;">2</span></span></span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span></span></span><span class="mo" id="MathJax-Span-1176" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">+</span><span class="mn" id="MathJax-Span-1177" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">4</span><span class="mi" id="MathJax-Span-1178" style="font-family: STIXGeneral-Italic;">x<span style="display: inline-block; overflow: hidden; height: 1px; width: 0.003em;"></span></span><span class="mo" id="MathJax-Span-1179" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">+</span><span class="mn" id="MathJax-Span-1180" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">2</span><span class="mi" id="MathJax-Span-1181" style="font-family: STIXGeneral-Italic;">y</span><span class="mo" id="MathJax-Span-1182" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">+</span><span class="mn" id="MathJax-Span-1183" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">6</span><span class="mo" id="MathJax-Span-1184" style="font-family: STIXGeneral-Regular; padding-left: 0.314em;">=</span><span class="mn" id="MathJax-Span-1185" style="font-family: STIXGeneral-Regular; padding-left: 0.314em;">0</span><span class="mspace" id="MathJax-Span-1186" style="height: 0.003em; vertical-align: 0.003em; width: 0.189em; display: inline-block; overflow: hidden;"></span></span><span style="display: inline-block; width: 0px; height: 2.488em;"></span></span></span><span style="border-left-width: 0.004em; border-left-style: solid; display: inline-block; overflow: hidden; width: 0px; height: 1.361em; vertical-align: -0.282em;"></span></span></nobr></span>
							<script type="math/tex" id="MathJax-Element-55"> 3x^{2}+3y^{2}+4x+2y+6=0\, </script>
					</div>
					<div class="alert alert-success">
						<button type="button" class="toggle-bookmark-button pull-right buttonless" data-action="add" data-user="185804764220139124118" data-content="7ac906de-de49-44bf-a145-7992baea248c">
							<i class="fa fa-bookmark-o"></i>
						</button>
						<a href="/practice-problem/4408c3fa-a3c8-4557-88e9-d958549ec711"><b>Geometry Practice</b></a>
						The distance of a point <span class="MathJax_Preview" style="color: inherit;"></span><span class="MathJax" id="MathJax-Element-76-Frame"><nobr><span class="math" id="MathJax-Span-1612" role="math" style="width: 3.295em; display: inline-block;"><span style="display: inline-block; position: relative; width: 2.86em; height: 0px; font-size: 115%;"><span style="position: absolute; clip: rect(1.618em 1000.003em 2.86em -999.997em); top: -2.481em; left: 0.003em;"><span class="mrow" id="MathJax-Span-1613"><span class="mi" id="MathJax-Span-1614" style="font-family: STIXGeneral-Italic;">P</span><span class="mo" id="MathJax-Span-1615" style="font-family: STIXGeneral-Regular;">(</span><span class="mi" id="MathJax-Span-1616" style="font-family: STIXGeneral-Italic;">h</span><span class="mo" id="MathJax-Span-1617" style="font-family: STIXGeneral-Regular;">,</span><span class="mi" id="MathJax-Span-1618" style="font-family: STIXGeneral-Italic; padding-left: 0.189em;">k<span style="display: inline-block; overflow: hidden; height: 1px; width: 0.003em;"></span></span><span class="mo" id="MathJax-Span-1619" style="font-family: STIXGeneral-Regular;">)</span><span class="mspace" id="MathJax-Span-1620" style="height: 0.003em; vertical-align: 0.003em; width: 0.189em; display: inline-block; overflow: hidden;"></span></span><span style="display: inline-block; width: 0px; height: 2.488em;"></span></span></span><span style="border-left-width: 0.004em; border-left-style: solid; display: inline-block; overflow: hidden; width: 0px; height: 1.146em; vertical-align: -0.282em;"></span></span></nobr></span>
						<script type="math/tex" id="MathJax-Element-76"> P(h,k)\, </script>
						from a pair of lines passing thro'the origin is d units.Show that the equation of the pair of lines is 
						<span class="MathJax_Preview" style="color: inherit;"></span>
						<span class="MathJax" id="MathJax-Element-77-Frame"><nobr><span class="math" id="MathJax-Span-1621" role="math" style="width: 11.804em; display: inline-block;"><span style="display: inline-block; position: relative; width: 10.252em; height: 0px; font-size: 115%;"><span style="position: absolute; clip: rect(1.432em 1000.003em 2.86em -999.997em); top: -2.481em; left: 0.003em;"><span class="mrow" id="MathJax-Span-1622"><span class="mo" id="MathJax-Span-1623" style="font-family: STIXGeneral-Regular;">(</span><span class="mi" id="MathJax-Span-1624" style="font-family: STIXGeneral-Italic;">x<span style="display: inline-block; overflow: hidden; height: 1px; width: 0.003em;"></span></span><span class="mi" id="MathJax-Span-1625" style="font-family: STIXGeneral-Italic;">k<span style="display: inline-block; overflow: hidden; height: 1px; width: 0.003em;"></span></span><span class="mo" id="MathJax-Span-1626" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">−</span><span class="mi" id="MathJax-Span-1627" style="font-family: STIXGeneral-Italic; padding-left: 0.252em;">h</span><span class="mi" id="MathJax-Span-1628" style="font-family: STIXGeneral-Italic;">y</span><span class="msubsup" id="MathJax-Span-1629"><span style="display: inline-block; position: relative; width: 0.748em; height: 0px;"><span style="position: absolute; clip: rect(3.109em 1000.003em 4.351em -999.997em); top: -3.972em; left: 0.003em;"><span class="mo" id="MathJax-Span-1630" style="font-family: STIXGeneral-Regular;">)</span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span><span style="position: absolute; top: -4.345em; left: 0.314em;"><span class="texatom" id="MathJax-Span-1631"><span class="mrow" id="MathJax-Span-1632"><span class="mn" id="MathJax-Span-1633" style="font-size: 70.7%; font-family: STIXGeneral-Regular;">2</span></span></span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span></span></span><span class="mo" id="MathJax-Span-1634" style="font-family: STIXGeneral-Regular; padding-left: 0.314em;">=</span><span class="msubsup" id="MathJax-Span-1635" style="padding-left: 0.314em;"><span style="display: inline-block; position: relative; width: 0.997em; height: 0px;"><span style="position: absolute; clip: rect(3.109em 1000.003em 4.165em -999.997em); top: -3.972em; left: 0.003em;"><span class="mi" id="MathJax-Span-1636" style="font-family: STIXGeneral-Italic;">d<span style="display: inline-block; overflow: hidden; height: 1px; width: 0.003em;"></span></span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span><span style="position: absolute; top: -4.345em; left: 0.562em;"><span class="texatom" id="MathJax-Span-1637"><span class="mrow" id="MathJax-Span-1638"><span class="mn" id="MathJax-Span-1639" style="font-size: 70.7%; font-family: STIXGeneral-Regular;">2</span></span></span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span></span></span><span class="mo" id="MathJax-Span-1640" style="font-family: STIXGeneral-Regular;">(</span><span class="msubsup" id="MathJax-Span-1641"><span style="display: inline-block; position: relative; width: 0.935em; height: 0px;"><span style="position: absolute; clip: rect(3.357em 1000.003em 4.165em -999.997em); top: -3.972em; left: 0.003em;"><span class="mi" id="MathJax-Span-1642" style="font-family: STIXGeneral-Italic;">x<span style="display: inline-block; overflow: hidden; height: 1px; width: 0.003em;"></span></span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span><span style="position: absolute; top: -4.345em; left: 0.5em;"><span class="texatom" id="MathJax-Span-1643"><span class="mrow" id="MathJax-Span-1644"><span class="mn" id="MathJax-Span-1645" style="font-size: 70.7%; font-family: STIXGeneral-Regular;">2</span></span></span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span></span></span><span class="mo" id="MathJax-Span-1646" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">+</span><span class="msubsup" id="MathJax-Span-1647" style="padding-left: 0.252em;"><span style="display: inline-block; position: relative; width: 0.873em; height: 0px;"><span style="position: absolute; clip: rect(3.357em 1000.003em 4.351em -999.997em); top: -3.972em; left: 0.003em;"><span class="mi" id="MathJax-Span-1648" style="font-family: STIXGeneral-Italic;">y</span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span><span style="position: absolute; top: -4.345em; left: 0.438em;"><span class="texatom" id="MathJax-Span-1649"><span class="mrow" id="MathJax-Span-1650"><span class="mn" id="MathJax-Span-1651" style="font-size: 70.7%; font-family: STIXGeneral-Regular;">2</span></span></span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span></span></span><span class="mo" id="MathJax-Span-1652" style="font-family: STIXGeneral-Regular;">)</span><span class="mspace" id="MathJax-Span-1653" style="height: 0.003em; vertical-align: 0.003em; width: 0.189em; display: inline-block; overflow: hidden;"></span></span><span style="display: inline-block; width: 0px; height: 2.488em;"></span></span></span><span style="border-left-width: 0.004em; border-left-style: solid; display: inline-block; overflow: hidden; width: 0px; height: 1.361em; vertical-align: -0.282em;"></span></span></nobr></span>
						<script type="math/tex" id="MathJax-Element-77"> (xk-hy)^{2}=d^{2}(x^{2}+y^{2})\, </script>
					</div>
					<div class="alert alert-success">							
						<button type="button" class="toggle-bookmark-button pull-right buttonless" data-action="add" data-user="185804764220139124118" data-content="af335f4c-8538-4c46-87cf-62bee879c31b">
							<i class="fa fa-bookmark-o"></i>
						</button>
						<a href="/practice-problem/af335f4c-8538-4c46-87cf-62bee879c31b"><b>Geometry Practice</b></a>
							Find the equation of tangents to the ellipse <span class="MathJax_Preview" style="color: inherit;"></span><span class="MathJax" id="MathJax-Element-31-Frame"><nobr><span class="math" id="MathJax-Span-747" role="math" style="width: 6.401em; display: inline-block;"><span style="display: inline-block; position: relative; width: 5.531em; height: 0px; font-size: 115%;"><span style="position: absolute; clip: rect(1.432em 1000.003em 2.86em -999.997em); top: -2.481em; left: 0.003em;"><span class="mrow" id="MathJax-Span-748"><span class="mn" id="MathJax-Span-749" style="font-family: STIXGeneral-Regular;">2</span><span class="msubsup" id="MathJax-Span-750"><span style="display: inline-block; position: relative; width: 0.935em; height: 0px;"><span style="position: absolute; clip: rect(3.357em 1000.003em 4.165em -999.997em); top: -3.972em; left: 0.003em;"><span class="mi" id="MathJax-Span-751" style="font-family: STIXGeneral-Italic;">x<span style="display: inline-block; overflow: hidden; height: 1px; width: 0.003em;"></span></span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span><span style="position: absolute; top: -4.345em; left: 0.5em;"><span class="texatom" id="MathJax-Span-752"><span class="mrow" id="MathJax-Span-753"><span class="mn" id="MathJax-Span-754" style="font-size: 70.7%; font-family: STIXGeneral-Regular;">2</span></span></span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span></span></span><span class="mo" id="MathJax-Span-755" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">+</span><span class="msubsup" id="MathJax-Span-756" style="padding-left: 0.252em;"><span style="display: inline-block; position: relative; width: 0.873em; height: 0px;"><span style="position: absolute; clip: rect(3.357em 1000.003em 4.351em -999.997em); top: -3.972em; left: 0.003em;"><span class="mi" id="MathJax-Span-757" style="font-family: STIXGeneral-Italic;">y</span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span><span style="position: absolute; top: -4.345em; left: 0.438em;"><span class="texatom" id="MathJax-Span-758"><span class="mrow" id="MathJax-Span-759"><span class="mn" id="MathJax-Span-760" style="font-size: 70.7%; font-family: STIXGeneral-Regular;">2</span></span></span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span></span></span><span class="mo" id="MathJax-Span-761" style="font-family: STIXGeneral-Regular; padding-left: 0.314em;">=</span><span class="mn" id="MathJax-Span-762" style="font-family: STIXGeneral-Regular; padding-left: 0.314em;">8</span><span class="mspace" id="MathJax-Span-763" style="height: 0.003em; vertical-align: 0.003em; width: 0.189em; display: inline-block; overflow: hidden;"></span></span><span style="display: inline-block; width: 0px; height: 2.488em;"></span></span></span><span style="border-left-width: 0.004em; border-left-style: solid; display: inline-block; overflow: hidden; width: 0px; height: 1.361em; vertical-align: -0.282em;"></span></span></nobr></span><script type="math/tex" id="MathJax-Element-31"> 2x^{2}+y^{2}=8\, </script> which is parallel to <span class="MathJax_Preview" style="color: inherit;"></span><span class="MathJax" id="MathJax-Element-32-Frame"><nobr><span class="math" id="MathJax-Span-764" role="math" style="width: 7.208em; display: inline-block;"><span style="display: inline-block; position: relative; width: 6.276em; height: 0px; font-size: 115%;"><span style="position: absolute; clip: rect(1.618em 1000.003em 2.86em -999.997em); top: -2.481em; left: 0.003em;"><span class="mrow" id="MathJax-Span-765"><span class="mi" id="MathJax-Span-766" style="font-family: STIXGeneral-Italic;">x<span style="display: inline-block; overflow: hidden; height: 1px; width: 0.003em;"></span></span><span class="mo" id="MathJax-Span-767" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">−</span><span class="mn" id="MathJax-Span-768" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">2</span><span class="mi" id="MathJax-Span-769" style="font-family: STIXGeneral-Italic;">y</span><span class="mo" id="MathJax-Span-770" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">−</span><span class="mn" id="MathJax-Span-771" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">4</span><span class="mo" id="MathJax-Span-772" style="font-family: STIXGeneral-Regular; padding-left: 0.314em;">=</span><span class="mn" id="MathJax-Span-773" style="font-family: STIXGeneral-Regular; padding-left: 0.314em;">0</span><span class="mspace" id="MathJax-Span-774" style="height: 0.003em; vertical-align: 0.003em; width: 0.189em; display: inline-block; overflow: hidden;"></span></span><span style="display: inline-block; width: 0px; height: 2.488em;"></span></span></span><span style="border-left-width: 0.004em; border-left-style: solid; display: inline-block; overflow: hidden; width: 0px; height: 1.146em; vertical-align: -0.282em;"></span></span></nobr></span><script type="math/tex" id="MathJax-Element-32"> x-2y-4=0\, </script> .
					</div>
				</div>
			</div>
		</div>
		<div id="ts-trigonometry-info-box" class="max-width-at-1000 align-left hidden ts-info-box">
			<div class = "box box-primary box-solid">
				<div class="box-header">
					<div class="align-right">
						<h3 class="box-title">Topic Currently Selected: Trigonometry</h3>
						<div class="padding-top-4 margin-right-5">
							<input class="btn btn-danger" type="submit" value="Edit Topic">
							<input class="btn btn-success" type="submit" value="See Full Topic">
						</div>
					</div>
				</div>
				<div class="box-body">
					<div class="margin-left-100 margin-top-10 centered">
						<input class="btn btn-warning large-input-group-button margin-10" type="submit" value="See 18 Pieces of Related Content">
						<input class="btn btn-success large-input-group-button margin-10" type="submit" value="See 18 Related Practice Problems">
						<input class="btn btn-primary large-input-group-button margin-10" type="submit" value="See 18 Related Exercies">
					</div>
					<div class="alert alert-success margin-top-10">
						<button type="button" class="toggle-bookmark-button pull-right buttonless" data-action="add" data-user="185804764220139124118" data-content="7ac906de-de49-44bf-a145-7992baea248c">
							<i class="fa fa-bookmark-o"></i>
						</button>
						<a href="/practice-problem/dfd3fd7d-baae-43d4-949b-0bdcd9e0e36e"><b>Geometry Practice</b></a> 
							Find the power of the point (2,-1) with respect to 
							<span class="MathJax_Preview" style="color: inherit;"></span>
							<span class="MathJax" id="MathJax-Element-55-Frame"><nobr><span class="math" id="MathJax-Span-1161" role="math" style="width: 13.73em; display: inline-block;"><span style="display: inline-block; position: relative; width: 11.929em; height: 0px; font-size: 115%;"><span style="position: absolute; clip: rect(1.432em 1000.003em 2.86em -999.997em); top: -2.481em; left: 0.003em;"><span class="mrow" id="MathJax-Span-1162"><span class="mn" id="MathJax-Span-1163" style="font-family: STIXGeneral-Regular;">3</span><span class="msubsup" id="MathJax-Span-1164"><span style="display: inline-block; position: relative; width: 0.935em; height: 0px;"><span style="position: absolute; clip: rect(3.357em 1000.003em 4.165em -999.997em); top: -3.972em; left: 0.003em;"><span class="mi" id="MathJax-Span-1165" style="font-family: STIXGeneral-Italic;">x<span style="display: inline-block; overflow: hidden; height: 1px; width: 0.003em;"></span></span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span><span style="position: absolute; top: -4.345em; left: 0.5em;"><span class="texatom" id="MathJax-Span-1166"><span class="mrow" id="MathJax-Span-1167"><span class="mn" id="MathJax-Span-1168" style="font-size: 70.7%; font-family: STIXGeneral-Regular;">2</span></span></span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span></span></span><span class="mo" id="MathJax-Span-1169" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">+</span><span class="mn" id="MathJax-Span-1170" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">3</span><span class="msubsup" id="MathJax-Span-1171"><span style="display: inline-block; position: relative; width: 0.873em; height: 0px;"><span style="position: absolute; clip: rect(3.357em 1000.003em 4.351em -999.997em); top: -3.972em; left: 0.003em;"><span class="mi" id="MathJax-Span-1172" style="font-family: STIXGeneral-Italic;">y</span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span><span style="position: absolute; top: -4.345em; left: 0.438em;"><span class="texatom" id="MathJax-Span-1173"><span class="mrow" id="MathJax-Span-1174"><span class="mn" id="MathJax-Span-1175" style="font-size: 70.7%; font-family: STIXGeneral-Regular;">2</span></span></span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span></span></span><span class="mo" id="MathJax-Span-1176" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">+</span><span class="mn" id="MathJax-Span-1177" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">4</span><span class="mi" id="MathJax-Span-1178" style="font-family: STIXGeneral-Italic;">x<span style="display: inline-block; overflow: hidden; height: 1px; width: 0.003em;"></span></span><span class="mo" id="MathJax-Span-1179" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">+</span><span class="mn" id="MathJax-Span-1180" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">2</span><span class="mi" id="MathJax-Span-1181" style="font-family: STIXGeneral-Italic;">y</span><span class="mo" id="MathJax-Span-1182" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">+</span><span class="mn" id="MathJax-Span-1183" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">6</span><span class="mo" id="MathJax-Span-1184" style="font-family: STIXGeneral-Regular; padding-left: 0.314em;">=</span><span class="mn" id="MathJax-Span-1185" style="font-family: STIXGeneral-Regular; padding-left: 0.314em;">0</span><span class="mspace" id="MathJax-Span-1186" style="height: 0.003em; vertical-align: 0.003em; width: 0.189em; display: inline-block; overflow: hidden;"></span></span><span style="display: inline-block; width: 0px; height: 2.488em;"></span></span></span><span style="border-left-width: 0.004em; border-left-style: solid; display: inline-block; overflow: hidden; width: 0px; height: 1.361em; vertical-align: -0.282em;"></span></span></nobr></span>
							<script type="math/tex" id="MathJax-Element-55"> 3x^{2}+3y^{2}+4x+2y+6=0\, </script>
					</div>
					<div class="alert alert-success">
						<button type="button" class="toggle-bookmark-button pull-right buttonless" data-action="add" data-user="185804764220139124118" data-content="7ac906de-de49-44bf-a145-7992baea248c">
							<i class="fa fa-bookmark-o"></i>
						</button>
						<a href="/practice-problem/4408c3fa-a3c8-4557-88e9-d958549ec711"><b>Geometry Practice</b></a>
						The distance of a point <span class="MathJax_Preview" style="color: inherit;"></span><span class="MathJax" id="MathJax-Element-76-Frame"><nobr><span class="math" id="MathJax-Span-1612" role="math" style="width: 3.295em; display: inline-block;"><span style="display: inline-block; position: relative; width: 2.86em; height: 0px; font-size: 115%;"><span style="position: absolute; clip: rect(1.618em 1000.003em 2.86em -999.997em); top: -2.481em; left: 0.003em;"><span class="mrow" id="MathJax-Span-1613"><span class="mi" id="MathJax-Span-1614" style="font-family: STIXGeneral-Italic;">P</span><span class="mo" id="MathJax-Span-1615" style="font-family: STIXGeneral-Regular;">(</span><span class="mi" id="MathJax-Span-1616" style="font-family: STIXGeneral-Italic;">h</span><span class="mo" id="MathJax-Span-1617" style="font-family: STIXGeneral-Regular;">,</span><span class="mi" id="MathJax-Span-1618" style="font-family: STIXGeneral-Italic; padding-left: 0.189em;">k<span style="display: inline-block; overflow: hidden; height: 1px; width: 0.003em;"></span></span><span class="mo" id="MathJax-Span-1619" style="font-family: STIXGeneral-Regular;">)</span><span class="mspace" id="MathJax-Span-1620" style="height: 0.003em; vertical-align: 0.003em; width: 0.189em; display: inline-block; overflow: hidden;"></span></span><span style="display: inline-block; width: 0px; height: 2.488em;"></span></span></span><span style="border-left-width: 0.004em; border-left-style: solid; display: inline-block; overflow: hidden; width: 0px; height: 1.146em; vertical-align: -0.282em;"></span></span></nobr></span>
						<script type="math/tex" id="MathJax-Element-76"> P(h,k)\, </script>
						from a pair of lines passing thro'the origin is d units.Show that the equation of the pair of lines is 
						<span class="MathJax_Preview" style="color: inherit;"></span>
						<span class="MathJax" id="MathJax-Element-77-Frame"><nobr><span class="math" id="MathJax-Span-1621" role="math" style="width: 11.804em; display: inline-block;"><span style="display: inline-block; position: relative; width: 10.252em; height: 0px; font-size: 115%;"><span style="position: absolute; clip: rect(1.432em 1000.003em 2.86em -999.997em); top: -2.481em; left: 0.003em;"><span class="mrow" id="MathJax-Span-1622"><span class="mo" id="MathJax-Span-1623" style="font-family: STIXGeneral-Regular;">(</span><span class="mi" id="MathJax-Span-1624" style="font-family: STIXGeneral-Italic;">x<span style="display: inline-block; overflow: hidden; height: 1px; width: 0.003em;"></span></span><span class="mi" id="MathJax-Span-1625" style="font-family: STIXGeneral-Italic;">k<span style="display: inline-block; overflow: hidden; height: 1px; width: 0.003em;"></span></span><span class="mo" id="MathJax-Span-1626" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">−</span><span class="mi" id="MathJax-Span-1627" style="font-family: STIXGeneral-Italic; padding-left: 0.252em;">h</span><span class="mi" id="MathJax-Span-1628" style="font-family: STIXGeneral-Italic;">y</span><span class="msubsup" id="MathJax-Span-1629"><span style="display: inline-block; position: relative; width: 0.748em; height: 0px;"><span style="position: absolute; clip: rect(3.109em 1000.003em 4.351em -999.997em); top: -3.972em; left: 0.003em;"><span class="mo" id="MathJax-Span-1630" style="font-family: STIXGeneral-Regular;">)</span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span><span style="position: absolute; top: -4.345em; left: 0.314em;"><span class="texatom" id="MathJax-Span-1631"><span class="mrow" id="MathJax-Span-1632"><span class="mn" id="MathJax-Span-1633" style="font-size: 70.7%; font-family: STIXGeneral-Regular;">2</span></span></span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span></span></span><span class="mo" id="MathJax-Span-1634" style="font-family: STIXGeneral-Regular; padding-left: 0.314em;">=</span><span class="msubsup" id="MathJax-Span-1635" style="padding-left: 0.314em;"><span style="display: inline-block; position: relative; width: 0.997em; height: 0px;"><span style="position: absolute; clip: rect(3.109em 1000.003em 4.165em -999.997em); top: -3.972em; left: 0.003em;"><span class="mi" id="MathJax-Span-1636" style="font-family: STIXGeneral-Italic;">d<span style="display: inline-block; overflow: hidden; height: 1px; width: 0.003em;"></span></span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span><span style="position: absolute; top: -4.345em; left: 0.562em;"><span class="texatom" id="MathJax-Span-1637"><span class="mrow" id="MathJax-Span-1638"><span class="mn" id="MathJax-Span-1639" style="font-size: 70.7%; font-family: STIXGeneral-Regular;">2</span></span></span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span></span></span><span class="mo" id="MathJax-Span-1640" style="font-family: STIXGeneral-Regular;">(</span><span class="msubsup" id="MathJax-Span-1641"><span style="display: inline-block; position: relative; width: 0.935em; height: 0px;"><span style="position: absolute; clip: rect(3.357em 1000.003em 4.165em -999.997em); top: -3.972em; left: 0.003em;"><span class="mi" id="MathJax-Span-1642" style="font-family: STIXGeneral-Italic;">x<span style="display: inline-block; overflow: hidden; height: 1px; width: 0.003em;"></span></span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span><span style="position: absolute; top: -4.345em; left: 0.5em;"><span class="texatom" id="MathJax-Span-1643"><span class="mrow" id="MathJax-Span-1644"><span class="mn" id="MathJax-Span-1645" style="font-size: 70.7%; font-family: STIXGeneral-Regular;">2</span></span></span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span></span></span><span class="mo" id="MathJax-Span-1646" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">+</span><span class="msubsup" id="MathJax-Span-1647" style="padding-left: 0.252em;"><span style="display: inline-block; position: relative; width: 0.873em; height: 0px;"><span style="position: absolute; clip: rect(3.357em 1000.003em 4.351em -999.997em); top: -3.972em; left: 0.003em;"><span class="mi" id="MathJax-Span-1648" style="font-family: STIXGeneral-Italic;">y</span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span><span style="position: absolute; top: -4.345em; left: 0.438em;"><span class="texatom" id="MathJax-Span-1649"><span class="mrow" id="MathJax-Span-1650"><span class="mn" id="MathJax-Span-1651" style="font-size: 70.7%; font-family: STIXGeneral-Regular;">2</span></span></span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span></span></span><span class="mo" id="MathJax-Span-1652" style="font-family: STIXGeneral-Regular;">)</span><span class="mspace" id="MathJax-Span-1653" style="height: 0.003em; vertical-align: 0.003em; width: 0.189em; display: inline-block; overflow: hidden;"></span></span><span style="display: inline-block; width: 0px; height: 2.488em;"></span></span></span><span style="border-left-width: 0.004em; border-left-style: solid; display: inline-block; overflow: hidden; width: 0px; height: 1.361em; vertical-align: -0.282em;"></span></span></nobr></span>
						<script type="math/tex" id="MathJax-Element-77"> (xk-hy)^{2}=d^{2}(x^{2}+y^{2})\, </script>
					</div>
					<div class="alert alert-success">							
						<button type="button" class="toggle-bookmark-button pull-right buttonless" data-action="add" data-user="185804764220139124118" data-content="af335f4c-8538-4c46-87cf-62bee879c31b">
							<i class="fa fa-bookmark-o"></i>
						</button>
						<a href="/practice-problem/af335f4c-8538-4c46-87cf-62bee879c31b"><b>Geometry Practice</b></a>
							Find the equation of tangents to the ellipse <span class="MathJax_Preview" style="color: inherit;"></span><span class="MathJax" id="MathJax-Element-31-Frame"><nobr><span class="math" id="MathJax-Span-747" role="math" style="width: 6.401em; display: inline-block;"><span style="display: inline-block; position: relative; width: 5.531em; height: 0px; font-size: 115%;"><span style="position: absolute; clip: rect(1.432em 1000.003em 2.86em -999.997em); top: -2.481em; left: 0.003em;"><span class="mrow" id="MathJax-Span-748"><span class="mn" id="MathJax-Span-749" style="font-family: STIXGeneral-Regular;">2</span><span class="msubsup" id="MathJax-Span-750"><span style="display: inline-block; position: relative; width: 0.935em; height: 0px;"><span style="position: absolute; clip: rect(3.357em 1000.003em 4.165em -999.997em); top: -3.972em; left: 0.003em;"><span class="mi" id="MathJax-Span-751" style="font-family: STIXGeneral-Italic;">x<span style="display: inline-block; overflow: hidden; height: 1px; width: 0.003em;"></span></span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span><span style="position: absolute; top: -4.345em; left: 0.5em;"><span class="texatom" id="MathJax-Span-752"><span class="mrow" id="MathJax-Span-753"><span class="mn" id="MathJax-Span-754" style="font-size: 70.7%; font-family: STIXGeneral-Regular;">2</span></span></span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span></span></span><span class="mo" id="MathJax-Span-755" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">+</span><span class="msubsup" id="MathJax-Span-756" style="padding-left: 0.252em;"><span style="display: inline-block; position: relative; width: 0.873em; height: 0px;"><span style="position: absolute; clip: rect(3.357em 1000.003em 4.351em -999.997em); top: -3.972em; left: 0.003em;"><span class="mi" id="MathJax-Span-757" style="font-family: STIXGeneral-Italic;">y</span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span><span style="position: absolute; top: -4.345em; left: 0.438em;"><span class="texatom" id="MathJax-Span-758"><span class="mrow" id="MathJax-Span-759"><span class="mn" id="MathJax-Span-760" style="font-size: 70.7%; font-family: STIXGeneral-Regular;">2</span></span></span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span></span></span><span class="mo" id="MathJax-Span-761" style="font-family: STIXGeneral-Regular; padding-left: 0.314em;">=</span><span class="mn" id="MathJax-Span-762" style="font-family: STIXGeneral-Regular; padding-left: 0.314em;">8</span><span class="mspace" id="MathJax-Span-763" style="height: 0.003em; vertical-align: 0.003em; width: 0.189em; display: inline-block; overflow: hidden;"></span></span><span style="display: inline-block; width: 0px; height: 2.488em;"></span></span></span><span style="border-left-width: 0.004em; border-left-style: solid; display: inline-block; overflow: hidden; width: 0px; height: 1.361em; vertical-align: -0.282em;"></span></span></nobr></span><script type="math/tex" id="MathJax-Element-31"> 2x^{2}+y^{2}=8\, </script> which is parallel to <span class="MathJax_Preview" style="color: inherit;"></span><span class="MathJax" id="MathJax-Element-32-Frame"><nobr><span class="math" id="MathJax-Span-764" role="math" style="width: 7.208em; display: inline-block;"><span style="display: inline-block; position: relative; width: 6.276em; height: 0px; font-size: 115%;"><span style="position: absolute; clip: rect(1.618em 1000.003em 2.86em -999.997em); top: -2.481em; left: 0.003em;"><span class="mrow" id="MathJax-Span-765"><span class="mi" id="MathJax-Span-766" style="font-family: STIXGeneral-Italic;">x<span style="display: inline-block; overflow: hidden; height: 1px; width: 0.003em;"></span></span><span class="mo" id="MathJax-Span-767" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">−</span><span class="mn" id="MathJax-Span-768" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">2</span><span class="mi" id="MathJax-Span-769" style="font-family: STIXGeneral-Italic;">y</span><span class="mo" id="MathJax-Span-770" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">−</span><span class="mn" id="MathJax-Span-771" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">4</span><span class="mo" id="MathJax-Span-772" style="font-family: STIXGeneral-Regular; padding-left: 0.314em;">=</span><span class="mn" id="MathJax-Span-773" style="font-family: STIXGeneral-Regular; padding-left: 0.314em;">0</span><span class="mspace" id="MathJax-Span-774" style="height: 0.003em; vertical-align: 0.003em; width: 0.189em; display: inline-block; overflow: hidden;"></span></span><span style="display: inline-block; width: 0px; height: 2.488em;"></span></span></span><span style="border-left-width: 0.004em; border-left-style: solid; display: inline-block; overflow: hidden; width: 0px; height: 1.146em; vertical-align: -0.282em;"></span></span></nobr></span><script type="math/tex" id="MathJax-Element-32"> x-2y-4=0\, </script> .
					</div>
				</div>
			</div>
		</div>
		
		<div id="ts-calculus-info-box" class="max-width-at-1000 align-left hidden ts-info-box">
			<div class = "box box-primary box-solid">
				<div class="box-header">
					<div class="align-right">
						<h3 class="box-title">Topic Currently Selected: Calculus</h3>
						<div class="padding-top-4 margin-right-5">
							<input class="btn btn-danger" type="submit" value="Edit Topic">
							<input class="btn btn-success" type="submit" value="See Full Topic">
						</div>
					</div>
				</div>
				<div class="box-body">
					<div class="margin-left-100 margin-top-10 centered">
						<input class="btn btn-warning large-input-group-button margin-10" type="submit" value="See 18 Pieces of Related Content">
						<input class="btn btn-success large-input-group-button margin-10" type="submit" value="See 18 Related Practice Problems">
						<input class="btn btn-primary large-input-group-button margin-10" type="submit" value="See 18 Related Exercies">
					</div>
					<div class="alert alert-success margin-top-10">
						<button type="button" class="toggle-bookmark-button pull-right buttonless" data-action="add" data-user="185804764220139124118" data-content="7ac906de-de49-44bf-a145-7992baea248c">
							<i class="fa fa-bookmark-o"></i>
						</button>
						<a href="/practice-problem/dfd3fd7d-baae-43d4-949b-0bdcd9e0e36e"><b>Geometry Practice</b></a> 
							Find the power of the point (2,-1) with respect to 
							<span class="MathJax_Preview" style="color: inherit;"></span>
							<span class="MathJax" id="MathJax-Element-55-Frame"><nobr><span class="math" id="MathJax-Span-1161" role="math" style="width: 13.73em; display: inline-block;"><span style="display: inline-block; position: relative; width: 11.929em; height: 0px; font-size: 115%;"><span style="position: absolute; clip: rect(1.432em 1000.003em 2.86em -999.997em); top: -2.481em; left: 0.003em;"><span class="mrow" id="MathJax-Span-1162"><span class="mn" id="MathJax-Span-1163" style="font-family: STIXGeneral-Regular;">3</span><span class="msubsup" id="MathJax-Span-1164"><span style="display: inline-block; position: relative; width: 0.935em; height: 0px;"><span style="position: absolute; clip: rect(3.357em 1000.003em 4.165em -999.997em); top: -3.972em; left: 0.003em;"><span class="mi" id="MathJax-Span-1165" style="font-family: STIXGeneral-Italic;">x<span style="display: inline-block; overflow: hidden; height: 1px; width: 0.003em;"></span></span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span><span style="position: absolute; top: -4.345em; left: 0.5em;"><span class="texatom" id="MathJax-Span-1166"><span class="mrow" id="MathJax-Span-1167"><span class="mn" id="MathJax-Span-1168" style="font-size: 70.7%; font-family: STIXGeneral-Regular;">2</span></span></span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span></span></span><span class="mo" id="MathJax-Span-1169" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">+</span><span class="mn" id="MathJax-Span-1170" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">3</span><span class="msubsup" id="MathJax-Span-1171"><span style="display: inline-block; position: relative; width: 0.873em; height: 0px;"><span style="position: absolute; clip: rect(3.357em 1000.003em 4.351em -999.997em); top: -3.972em; left: 0.003em;"><span class="mi" id="MathJax-Span-1172" style="font-family: STIXGeneral-Italic;">y</span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span><span style="position: absolute; top: -4.345em; left: 0.438em;"><span class="texatom" id="MathJax-Span-1173"><span class="mrow" id="MathJax-Span-1174"><span class="mn" id="MathJax-Span-1175" style="font-size: 70.7%; font-family: STIXGeneral-Regular;">2</span></span></span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span></span></span><span class="mo" id="MathJax-Span-1176" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">+</span><span class="mn" id="MathJax-Span-1177" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">4</span><span class="mi" id="MathJax-Span-1178" style="font-family: STIXGeneral-Italic;">x<span style="display: inline-block; overflow: hidden; height: 1px; width: 0.003em;"></span></span><span class="mo" id="MathJax-Span-1179" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">+</span><span class="mn" id="MathJax-Span-1180" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">2</span><span class="mi" id="MathJax-Span-1181" style="font-family: STIXGeneral-Italic;">y</span><span class="mo" id="MathJax-Span-1182" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">+</span><span class="mn" id="MathJax-Span-1183" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">6</span><span class="mo" id="MathJax-Span-1184" style="font-family: STIXGeneral-Regular; padding-left: 0.314em;">=</span><span class="mn" id="MathJax-Span-1185" style="font-family: STIXGeneral-Regular; padding-left: 0.314em;">0</span><span class="mspace" id="MathJax-Span-1186" style="height: 0.003em; vertical-align: 0.003em; width: 0.189em; display: inline-block; overflow: hidden;"></span></span><span style="display: inline-block; width: 0px; height: 2.488em;"></span></span></span><span style="border-left-width: 0.004em; border-left-style: solid; display: inline-block; overflow: hidden; width: 0px; height: 1.361em; vertical-align: -0.282em;"></span></span></nobr></span>
							<script type="math/tex" id="MathJax-Element-55"> 3x^{2}+3y^{2}+4x+2y+6=0\, </script>
					</div>
					<div class="alert alert-success">
						<button type="button" class="toggle-bookmark-button pull-right buttonless" data-action="add" data-user="185804764220139124118" data-content="7ac906de-de49-44bf-a145-7992baea248c">
							<i class="fa fa-bookmark-o"></i>
						</button>
						<a href="/practice-problem/4408c3fa-a3c8-4557-88e9-d958549ec711"><b>Geometry Practice</b></a>
						The distance of a point <span class="MathJax_Preview" style="color: inherit;"></span><span class="MathJax" id="MathJax-Element-76-Frame"><nobr><span class="math" id="MathJax-Span-1612" role="math" style="width: 3.295em; display: inline-block;"><span style="display: inline-block; position: relative; width: 2.86em; height: 0px; font-size: 115%;"><span style="position: absolute; clip: rect(1.618em 1000.003em 2.86em -999.997em); top: -2.481em; left: 0.003em;"><span class="mrow" id="MathJax-Span-1613"><span class="mi" id="MathJax-Span-1614" style="font-family: STIXGeneral-Italic;">P</span><span class="mo" id="MathJax-Span-1615" style="font-family: STIXGeneral-Regular;">(</span><span class="mi" id="MathJax-Span-1616" style="font-family: STIXGeneral-Italic;">h</span><span class="mo" id="MathJax-Span-1617" style="font-family: STIXGeneral-Regular;">,</span><span class="mi" id="MathJax-Span-1618" style="font-family: STIXGeneral-Italic; padding-left: 0.189em;">k<span style="display: inline-block; overflow: hidden; height: 1px; width: 0.003em;"></span></span><span class="mo" id="MathJax-Span-1619" style="font-family: STIXGeneral-Regular;">)</span><span class="mspace" id="MathJax-Span-1620" style="height: 0.003em; vertical-align: 0.003em; width: 0.189em; display: inline-block; overflow: hidden;"></span></span><span style="display: inline-block; width: 0px; height: 2.488em;"></span></span></span><span style="border-left-width: 0.004em; border-left-style: solid; display: inline-block; overflow: hidden; width: 0px; height: 1.146em; vertical-align: -0.282em;"></span></span></nobr></span>
						<script type="math/tex" id="MathJax-Element-76"> P(h,k)\, </script>
						from a pair of lines passing thro'the origin is d units.Show that the equation of the pair of lines is 
						<span class="MathJax_Preview" style="color: inherit;"></span>
						<span class="MathJax" id="MathJax-Element-77-Frame"><nobr><span class="math" id="MathJax-Span-1621" role="math" style="width: 11.804em; display: inline-block;"><span style="display: inline-block; position: relative; width: 10.252em; height: 0px; font-size: 115%;"><span style="position: absolute; clip: rect(1.432em 1000.003em 2.86em -999.997em); top: -2.481em; left: 0.003em;"><span class="mrow" id="MathJax-Span-1622"><span class="mo" id="MathJax-Span-1623" style="font-family: STIXGeneral-Regular;">(</span><span class="mi" id="MathJax-Span-1624" style="font-family: STIXGeneral-Italic;">x<span style="display: inline-block; overflow: hidden; height: 1px; width: 0.003em;"></span></span><span class="mi" id="MathJax-Span-1625" style="font-family: STIXGeneral-Italic;">k<span style="display: inline-block; overflow: hidden; height: 1px; width: 0.003em;"></span></span><span class="mo" id="MathJax-Span-1626" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">−</span><span class="mi" id="MathJax-Span-1627" style="font-family: STIXGeneral-Italic; padding-left: 0.252em;">h</span><span class="mi" id="MathJax-Span-1628" style="font-family: STIXGeneral-Italic;">y</span><span class="msubsup" id="MathJax-Span-1629"><span style="display: inline-block; position: relative; width: 0.748em; height: 0px;"><span style="position: absolute; clip: rect(3.109em 1000.003em 4.351em -999.997em); top: -3.972em; left: 0.003em;"><span class="mo" id="MathJax-Span-1630" style="font-family: STIXGeneral-Regular;">)</span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span><span style="position: absolute; top: -4.345em; left: 0.314em;"><span class="texatom" id="MathJax-Span-1631"><span class="mrow" id="MathJax-Span-1632"><span class="mn" id="MathJax-Span-1633" style="font-size: 70.7%; font-family: STIXGeneral-Regular;">2</span></span></span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span></span></span><span class="mo" id="MathJax-Span-1634" style="font-family: STIXGeneral-Regular; padding-left: 0.314em;">=</span><span class="msubsup" id="MathJax-Span-1635" style="padding-left: 0.314em;"><span style="display: inline-block; position: relative; width: 0.997em; height: 0px;"><span style="position: absolute; clip: rect(3.109em 1000.003em 4.165em -999.997em); top: -3.972em; left: 0.003em;"><span class="mi" id="MathJax-Span-1636" style="font-family: STIXGeneral-Italic;">d<span style="display: inline-block; overflow: hidden; height: 1px; width: 0.003em;"></span></span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span><span style="position: absolute; top: -4.345em; left: 0.562em;"><span class="texatom" id="MathJax-Span-1637"><span class="mrow" id="MathJax-Span-1638"><span class="mn" id="MathJax-Span-1639" style="font-size: 70.7%; font-family: STIXGeneral-Regular;">2</span></span></span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span></span></span><span class="mo" id="MathJax-Span-1640" style="font-family: STIXGeneral-Regular;">(</span><span class="msubsup" id="MathJax-Span-1641"><span style="display: inline-block; position: relative; width: 0.935em; height: 0px;"><span style="position: absolute; clip: rect(3.357em 1000.003em 4.165em -999.997em); top: -3.972em; left: 0.003em;"><span class="mi" id="MathJax-Span-1642" style="font-family: STIXGeneral-Italic;">x<span style="display: inline-block; overflow: hidden; height: 1px; width: 0.003em;"></span></span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span><span style="position: absolute; top: -4.345em; left: 0.5em;"><span class="texatom" id="MathJax-Span-1643"><span class="mrow" id="MathJax-Span-1644"><span class="mn" id="MathJax-Span-1645" style="font-size: 70.7%; font-family: STIXGeneral-Regular;">2</span></span></span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span></span></span><span class="mo" id="MathJax-Span-1646" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">+</span><span class="msubsup" id="MathJax-Span-1647" style="padding-left: 0.252em;"><span style="display: inline-block; position: relative; width: 0.873em; height: 0px;"><span style="position: absolute; clip: rect(3.357em 1000.003em 4.351em -999.997em); top: -3.972em; left: 0.003em;"><span class="mi" id="MathJax-Span-1648" style="font-family: STIXGeneral-Italic;">y</span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span><span style="position: absolute; top: -4.345em; left: 0.438em;"><span class="texatom" id="MathJax-Span-1649"><span class="mrow" id="MathJax-Span-1650"><span class="mn" id="MathJax-Span-1651" style="font-size: 70.7%; font-family: STIXGeneral-Regular;">2</span></span></span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span></span></span><span class="mo" id="MathJax-Span-1652" style="font-family: STIXGeneral-Regular;">)</span><span class="mspace" id="MathJax-Span-1653" style="height: 0.003em; vertical-align: 0.003em; width: 0.189em; display: inline-block; overflow: hidden;"></span></span><span style="display: inline-block; width: 0px; height: 2.488em;"></span></span></span><span style="border-left-width: 0.004em; border-left-style: solid; display: inline-block; overflow: hidden; width: 0px; height: 1.361em; vertical-align: -0.282em;"></span></span></nobr></span>
						<script type="math/tex" id="MathJax-Element-77"> (xk-hy)^{2}=d^{2}(x^{2}+y^{2})\, </script>
					</div>
					<div class="alert alert-success">							
						<button type="button" class="toggle-bookmark-button pull-right buttonless" data-action="add" data-user="185804764220139124118" data-content="af335f4c-8538-4c46-87cf-62bee879c31b">
							<i class="fa fa-bookmark-o"></i>
						</button>
						<a href="/practice-problem/af335f4c-8538-4c46-87cf-62bee879c31b"><b>Geometry Practice</b></a>
							Find the equation of tangents to the ellipse <span class="MathJax_Preview" style="color: inherit;"></span><span class="MathJax" id="MathJax-Element-31-Frame"><nobr><span class="math" id="MathJax-Span-747" role="math" style="width: 6.401em; display: inline-block;"><span style="display: inline-block; position: relative; width: 5.531em; height: 0px; font-size: 115%;"><span style="position: absolute; clip: rect(1.432em 1000.003em 2.86em -999.997em); top: -2.481em; left: 0.003em;"><span class="mrow" id="MathJax-Span-748"><span class="mn" id="MathJax-Span-749" style="font-family: STIXGeneral-Regular;">2</span><span class="msubsup" id="MathJax-Span-750"><span style="display: inline-block; position: relative; width: 0.935em; height: 0px;"><span style="position: absolute; clip: rect(3.357em 1000.003em 4.165em -999.997em); top: -3.972em; left: 0.003em;"><span class="mi" id="MathJax-Span-751" style="font-family: STIXGeneral-Italic;">x<span style="display: inline-block; overflow: hidden; height: 1px; width: 0.003em;"></span></span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span><span style="position: absolute; top: -4.345em; left: 0.5em;"><span class="texatom" id="MathJax-Span-752"><span class="mrow" id="MathJax-Span-753"><span class="mn" id="MathJax-Span-754" style="font-size: 70.7%; font-family: STIXGeneral-Regular;">2</span></span></span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span></span></span><span class="mo" id="MathJax-Span-755" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">+</span><span class="msubsup" id="MathJax-Span-756" style="padding-left: 0.252em;"><span style="display: inline-block; position: relative; width: 0.873em; height: 0px;"><span style="position: absolute; clip: rect(3.357em 1000.003em 4.351em -999.997em); top: -3.972em; left: 0.003em;"><span class="mi" id="MathJax-Span-757" style="font-family: STIXGeneral-Italic;">y</span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span><span style="position: absolute; top: -4.345em; left: 0.438em;"><span class="texatom" id="MathJax-Span-758"><span class="mrow" id="MathJax-Span-759"><span class="mn" id="MathJax-Span-760" style="font-size: 70.7%; font-family: STIXGeneral-Regular;">2</span></span></span><span style="display: inline-block; width: 0px; height: 3.978em;"></span></span></span></span><span class="mo" id="MathJax-Span-761" style="font-family: STIXGeneral-Regular; padding-left: 0.314em;">=</span><span class="mn" id="MathJax-Span-762" style="font-family: STIXGeneral-Regular; padding-left: 0.314em;">8</span><span class="mspace" id="MathJax-Span-763" style="height: 0.003em; vertical-align: 0.003em; width: 0.189em; display: inline-block; overflow: hidden;"></span></span><span style="display: inline-block; width: 0px; height: 2.488em;"></span></span></span><span style="border-left-width: 0.004em; border-left-style: solid; display: inline-block; overflow: hidden; width: 0px; height: 1.361em; vertical-align: -0.282em;"></span></span></nobr></span><script type="math/tex" id="MathJax-Element-31"> 2x^{2}+y^{2}=8\, </script> which is parallel to <span class="MathJax_Preview" style="color: inherit;"></span><span class="MathJax" id="MathJax-Element-32-Frame"><nobr><span class="math" id="MathJax-Span-764" role="math" style="width: 7.208em; display: inline-block;"><span style="display: inline-block; position: relative; width: 6.276em; height: 0px; font-size: 115%;"><span style="position: absolute; clip: rect(1.618em 1000.003em 2.86em -999.997em); top: -2.481em; left: 0.003em;"><span class="mrow" id="MathJax-Span-765"><span class="mi" id="MathJax-Span-766" style="font-family: STIXGeneral-Italic;">x<span style="display: inline-block; overflow: hidden; height: 1px; width: 0.003em;"></span></span><span class="mo" id="MathJax-Span-767" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">−</span><span class="mn" id="MathJax-Span-768" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">2</span><span class="mi" id="MathJax-Span-769" style="font-family: STIXGeneral-Italic;">y</span><span class="mo" id="MathJax-Span-770" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">−</span><span class="mn" id="MathJax-Span-771" style="font-family: STIXGeneral-Regular; padding-left: 0.252em;">4</span><span class="mo" id="MathJax-Span-772" style="font-family: STIXGeneral-Regular; padding-left: 0.314em;">=</span><span class="mn" id="MathJax-Span-773" style="font-family: STIXGeneral-Regular; padding-left: 0.314em;">0</span><span class="mspace" id="MathJax-Span-774" style="height: 0.003em; vertical-align: 0.003em; width: 0.189em; display: inline-block; overflow: hidden;"></span></span><span style="display: inline-block; width: 0px; height: 2.488em;"></span></span></span><span style="border-left-width: 0.004em; border-left-style: solid; display: inline-block; overflow: hidden; width: 0px; height: 1.146em; vertical-align: -0.282em;"></span></span></nobr></span><script type="math/tex" id="MathJax-Element-32"> x-2y-4=0\, </script> .
					</div>
				</div>
			</div>
		</div>
	</jsp:attribute>
	<jsp:attribute name="javascriptDependencies">
		topic-selector
	</jsp:attribute>
</t:genericpage>