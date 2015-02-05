<table class="table">
	<tbody>
		<tr>
			<th>Source</th>
			<th>Karma</th>
			<th>Proportion</th>
		</tr>
		<tr>
			<td>Contributing Practice Problems</td><td>${karmaFromPracticeProblems}</td>
			<td>
				<div class="progress sm progress-striped active"><div class="progress-bar progress-bar-success" role="progressbar" style="width: ${100 * (karmaFromPracticeProblems + 1) / (karmaTotal +1)}%"></div></div>
			</td>					
		</tr>
		<tr>
			<td>Contributing Questions</td><td>${karmaFromQuestions}</td>
			<td>
				<div class="progress sm progress-striped active"><div class="progress-bar progress-bar-primary" role="progressbar" style="width: ${100 * (karmaFromQuestions + 1) / (karmaTotal +1)}%"></div></div>
			</td>					
		</tr>
		<tr>
			<td>Contributing Text Content</td><td>${karmaFromTextContent}</td>
			<td>
				<div class="progress sm progress-striped active"><div class="progress-bar progress-bar-warning" role="progressbar" style="width: ${100 * (karmaFromTextContent + 1) / (karmaTotal +1)}%"></div></div>
			</td>					
		</tr>
		<tr>
			<td>Answering/Commenting on Content</td><td>${karmaFromAnswers}</td>
			<td>
				<div class="progress sm progress-striped active"><div class="progress-bar progress-bar-danger" role="progressbar" style="width: ${100 * (karmaFromAnswers + 1) / (karmaTotal +1)}%"></div></div>
			</td>					
		</tr>
		<tr>
			<td>Rating Others</td><td>${karmaFromRatingOthers}</td>
			<td>
				<div class="progress sm progress-striped active"><div class="progress-bar progress-bar-info" role="progressbar" style="width: ${100 * (karmaFromRatingOthers + 1) / (karmaTotal +1)}%"></div></div>
			</td>					
		</tr>
		<tr>
			<td>Answering in Answer Mode</td><td>${karmaFromAnswerMode}</td>
			<td>
				<div class="progress sm progress-striped active"><div class="progress-bar progress-bar-primary" role="progressbar" style="width: ${100 * (karmaFromAnswerMode + 1) / (karmaTotal +1)}%"></div></div>
			</td>					
		</tr>
		<tr>
			<td>Your Answers/Comments Quality</td><td>${karmaFromApprovedAnswers}</td>
			<td>
				<div class="progress sm progress-striped active"><div class="progress-bar progress-bar-success" role="progressbar" style="width: ${100 * (karmaFromApprovedAnswers + 1) / (karmaTotal +1)}%"></div></div>
			</td>					
		</tr>
	</tbody>
</table>