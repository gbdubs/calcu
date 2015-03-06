<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="latex-playground hidden">
	<div class="lpg-header">
		<button class="btn btn-primary lpg-instructions-toggle pull-left">Info</button>
		<h4>LaTex Playground</h4>
		<button class="btn btn-success lpg-toggle lpg-close">Close</button>
	</div>
	<div class="lpg-instructions bg-light-blue">
		&nbsp;&nbsp;&nbsp;You can use this (draggable and resizable) Playground to assemble your LaTex expressions, with no requisite knowledge of LaTex. 
		Simply use the buttons in the drop downs to insert new elements into your expression. You can edit the specifics by just editing the contents of the upper text-box.
		Press Enter to see the rendered result of the expression as you work.
		When finished, choose whether you want the expression to be inline (displayed as part of a sentence), or full line (displayed on its own).
		Then add it in to your problem/question's body/solution by clicking that button. Simple! Now that you have got it, click Info to hide these instructions!
	</div>
	<div class="lpg-body">
		<div class="btn-group lpg-toolbar">
			<div class="btn-group">
				<button type="button" class="btn btn-default dropdown-toggle lpg-button-disabled" data-toggle="dropdown" aria-expanded="false">Expressions  <span class="fa fa-caret-down"></span></button>
				<ul class="dropdown-menu">
				  <li class="lpg-button" data-tex="\int{a}^{b} {x^2} dx">\(\int{a}^{b} {x^2} dx\)</li>
				  <li class="lpg-button no-padding" data-tex="\sum\limits_{i=1}^{n} i^2">\(\sum\limits_{i=1}^{n} i^2\)</li>
				  <li class="lpg-button no-padding" data-tex="\prod\limits_{n=1}^{\infty} {f(n)}">\(\prod\limits_{n=1}^{\infty} {f(n)}\)</li>
				  <li class="lpg-button" data-tex="\lim_{x\to\infty} f(x)">\(\lim_{x\to\infty} f(x)\)</li>
				  <li class="lpg-button" data-tex="\frac{dy}{dx}">\(\frac{dy}{dx}\)</li>
				  <li class="lpg-button" data-tex="\frac{d^2 u}{dt^2}">\(\frac{d^2 u}{dt^2}\)</li>
				  <li class="lpg-button" data-tex="\frac{\partial x}{\partial t}">\(\frac{\partial x}{\partial t}\)</li>
				  <li class="lpg-button" data-tex="\overline{n * n}">\(\overline{n * n}\)</li>
				  <li class="lpg-button" data-tex="\sqrt{x - 2x + 1}">\(\sqrt{x - 2x + 1}\)</li>
				  <li class="lpg-button" data-tex="\sqrt[3]{-1}">\(\sqrt[3]{-1}\)</li>
				</ul>
			</div>
			<div class="btn-group">
				<button type="button" class="btn btn-default dropdown-toggle lpg-button-disabled" data-toggle="dropdown" aria-expanded="false">Math Symbols  <span class="fa fa-caret-down"></span></button>
				<div class="dropdown-menu lpg-symbol-browser">
					<button class="btn btn-default lpg-button" type="button" data-tex="\equiv">\(\equiv\)</button>
					<button class="btn btn-default lpg-button" type="button" data-tex="\approx">\(\approx\)</button>
					<button class="btn btn-default lpg-button" type="button" data-tex="\cong">\(\cong\)</button>
					<button class="btn btn-default lpg-button" type="button" data-tex="\neq">\(\neq\)</button>
					<button class="btn btn-default lpg-button" type="button" data-tex="\pm">\(\pm\)</button>
					<button class="btn btn-default lpg-button" type="button" data-tex="\mp">\(\mp\)</button>
					<button class="btn btn-default lpg-button" type="button" data-tex="\times">\(\times\)</button>
					<button class="btn btn-default lpg-button" type="button" data-tex="\div">\(\div\)</button>
					<button class="btn btn-default lpg-button" type="button" data-tex="\circ">\(\circ\)</button>
					<button class="btn btn-default lpg-button" type="button" data-tex="\vee">\(\vee\)</button>
					<button class="btn btn-default lpg-button" type="button" data-tex="\wedge">\(\wedge\)</button>
					<button class="btn btn-default lpg-button" type="button" data-tex="\geq">\(\geq\)</button>
					<button class="btn btn-default lpg-button" type="button" data-tex="\leq">\(\leq\)</button>
					<button class="btn btn-default lpg-button" type="button" data-tex="\subset">\(\subset\)</button>
					<button class="btn btn-default lpg-button" type="button" data-tex="\subseteq">\(\subseteq\)</button>
					<button class="btn btn-default lpg-button" type="button" data-tex="\in">\(\in\)</button>
					<button class="btn btn-default lpg-button" type="button" data-tex="\Rightarrow">\(\Rightarrow\)</button>
					<button class="btn btn-default lpg-button" type="button" data-tex="\Leftarrow">\(\Leftarrow\)</button>
					<button class="btn btn-default lpg-button" type="button" data-tex="\Leftrightarrow">\(\Leftrightarrow\)</button>
					<button class="btn btn-default lpg-button" type="button" data-tex="\leadsto">\(\leadsto\)</button>
					<button class="btn btn-default lpg-button" type="button" data-tex="\rightarrow">\(\rightarrow\)</button>
					<button class="btn btn-default lpg-button" type="button" data-tex="\leftarrow">\(\leftarrow\)</button>
					<button class="btn btn-default lpg-button" type="button" data-tex="\leftrightarrow">\(\leftrightarrow\)</button>
					<button class="btn btn-default lpg-button" type="button" data-tex="\aleph">\(\aleph\)</button>
					<button class="btn btn-default lpg-button" type="button" data-tex="\emptyset">\(\emptyset\)</button>
					<button class="btn btn-default lpg-button" type="button" data-tex="\otimes">\(\otimes\)</button>
					<button class="btn btn-default lpg-button" type="button" data-tex="\exists">\(\exists\)</button>
					<button class="btn btn-default lpg-button" type="button" data-tex="\forall">\(\forall\)</button>
					<button class="btn btn-default lpg-button" type="button" data-tex="\neg">\(\neg\)</button>
					<button class="btn btn-default lpg-button" type="button" data-tex="\cup">\(\cup\)</button>
					<button class="btn btn-default lpg-button" type="button" data-tex="\cap">\(\cap\)</button>
					<button class="btn btn-default lpg-button" type="button" data-tex="\sin">\(\sin\)</button>
					<button class="btn btn-default lpg-button" type="button" data-tex="\cos">\(\cos\)</button>
					<button class="btn btn-default lpg-button" type="button" data-tex="\tan">\(\tan\)</button>
					<button class="btn btn-default lpg-button" type="button" data-tex="\csc">\(\csc\)</button>
					<button class="btn btn-default lpg-button" type="button" data-tex="\sec">\(\sec\)</button>
					<button class="btn btn-default lpg-button" type="button" data-tex="\cot">\(\cot\)</button>
					<button class="btn btn-default lpg-button" type="button" data-tex="\sum">\(\sum\)</button>
					<button class="btn btn-default lpg-button" type="button" data-tex="\prod">\(\prod\)</button>
					<button class="btn btn-default lpg-button" type="button" data-tex="\int">\(\int\)</button>
					<button class="btn btn-default lpg-button" type="button" data-tex="\iint">\(\iint\)</button>
					<button class="btn btn-default lpg-button" type="button" data-tex="\iiint">\(\iiint\)</button>
					<button class="btn btn-default lpg-button" type="button" data-tex="\oint">\(\oint\)</button>
				</div>
			</div>
			<div class="btn-group">
				<button type="button" class="btn btn-default dropdown-toggle lpg-button-disabled" data-toggle="dropdown" aria-expanded="false">Greek Letters  <span class="fa fa-caret-down"></span></button>
				<div class="dropdown-menu lpg-symbol-browser">
				  <button class="btn btn-default lpg-button" type="button" data-tex="\alpha">\(\alpha\)</button>
				  <button class="btn btn-default lpg-button" type="button" data-tex="\beta">\(\beta\)</button>
				  <button class="btn btn-default lpg-button" type="button" data-tex="\delta">\(\delta\)</button>
				  <button class="btn btn-default lpg-button" type="button" data-tex="\Delta">\(\Delta\)</button>
				  <button class="btn btn-default lpg-button" type="button" data-tex="\epsilon">\(\epsilon\)</button>
				  <button class="btn btn-default lpg-button" type="button" data-tex="\zeta">\(\zeta\)</button>
				  <button class="btn btn-default lpg-button" type="button" data-tex="\eta">\(\eta\)</button>
				  <button class="btn btn-default lpg-button" type="button" data-tex="\theta">\(\theta\)</button>
				  <button class="btn btn-default lpg-button" type="button" data-tex="\Theta">\(\Theta\)</button>
				  <button class="btn btn-default lpg-button" type="button" data-tex="\gamma">\(\gamma\)</button>
				  <button class="btn btn-default lpg-button" type="button" data-tex="\Gamma">\(\Gamma\)</button>
				  <button class="btn btn-default lpg-button" type="button" data-tex="\kappa">\(\kappa\)</button>
				  <button class="btn btn-default lpg-button" type="button" data-tex="\lambda">\(\lambda\)</button>
				  <button class="btn btn-default lpg-button" type="button" data-tex="\Lambda">\(\Lambda\)</button>
				  <button class="btn btn-default lpg-button" type="button" data-tex="\mu">\(\mu\)</button>
				  <button class="btn btn-default lpg-button" type="button" data-tex="\nu">\(\nu\)</button>
				  <button class="btn btn-default lpg-button" type="button" data-tex="\xi">\(\xi\)</button>
				  <button class="btn btn-default lpg-button" type="button" data-tex="\Xi">\(\Xi\)</button>
				  <button class="btn btn-default lpg-button" type="button" data-tex="\pi">\(\pi\)</button>
				  <button class="btn btn-default lpg-button" type="button" data-tex="\Pi">\(\Pi\)</button>
				  <button class="btn btn-default lpg-button" type="button" data-tex="\rho">\(\rho\)</button>
				  <button class="btn btn-default lpg-button" type="button" data-tex="\sigma">\(\sigma\)</button>
				  <button class="btn btn-default lpg-button" type="button" data-tex="\Sigma">\(\Sigma\)</button>
				  <button class="btn btn-default lpg-button" type="button" data-tex="\tau">\(\tau\)</button>
				  <button class="btn btn-default lpg-button" type="button" data-tex="\upsilon">\(\upsilon\)</button>
				  <button class="btn btn-default lpg-button" type="button" data-tex="\Upsilon">\(\Upsilon\)</button>
				  <button class="btn btn-default lpg-button" type="button" data-tex="\phi">\(\phi\)</button>
				  <button class="btn btn-default lpg-button" type="button" data-tex="\Phi">\(\Phi\)</button>
				  <button class="btn btn-default lpg-button" type="button" data-tex="\chi">\(\chi\)</button>
				  <button class="btn btn-default lpg-button" type="button" data-tex="\psi">\(\psi\)</button>
				  <button class="btn btn-default lpg-button" type="button" data-tex="\Psi">\(\Psi\)</button>
				  <button class="btn btn-default lpg-button" type="button" data-tex="\omega">\(\omega\)</button>
				  <button class="btn btn-default lpg-button" type="button" data-tex="\Omega">\(\Omega\)</button>
				</div>
			</div>
		</div>
		<label>LaTex Expression</label>	
		<span>	
			<input type="text" class="lpg-input-box form-control" placeholder="Start Assembling Your LaTex Expression here!"/>
		</span>
		<label>Result</label>
		<div class="lpg-result-box" id="lpg-result-box"></div>
	</div>
	<div class="lpg-footer">
		<select id="lpg-latex-line-select" class="bg-green form-control">
			<option value="inline">Inline</option>
			<option value="full-line">Full Line</option>
		</select>
		<c:if test="${param.hasSolution}">
			<button class="btn btn-success lpg-insert-into-solution">Insert Into Solution</button>
		</c:if>
		<button class="btn btn-success lpg-insert-into-body">Insert Into Body</button>
	</div>
</div>



