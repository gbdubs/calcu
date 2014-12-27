<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
	<head>
		<title>User Preferences</title>
	</head>
	<body>
		<c:choose>
			<c:when test="${user != null}">
				<p>
					Welcome, ${username}! You have ${karma}.
				</p>
				<form action="/change-email-prefs" method="post">
					<div class="user-email-preferences">
		                <label for="email-pref-reply">Reply Email Preference</label>
		                <select class="form-control" name="reply-pref" id="email-pref-reply" value="${replyPref}">
		                    <option value="every" <c:if test="${replyPref =='every'}">selected</c:if>>EVERY: Email me for every reply to a question of mine.</option>
		                    <option value="best" <c:if test="${replyPref =='best'}">selected</c:if>>BEST: Only email me a reply to a question if it recieves 5 votes.</option>
		                    <option value="none" <c:if test="${replyPref =='none'}">selected</c:if>>NONE: Please do not email me for any reply to a question that I ask.</option>
		                </select>
		                <label for="email-pref-karma">Karma Email Preference</label>
		                <select class="form-control" name="karma-pref" id="email-pref-karma" value="${karmaPref}">
		                    <option value="best" <c:if test="${karmaPref =='best'}">selected</c:if>>BEST: Email me when a post of mine gets lots of Karma, or I get a gift.</option>        
		                    <option value="gift" <c:if test="${karmaPref == 'gift'}">selected</c:if>>GIFT: Only email me when another user gives me Karma as a gift.</option>
		                    <option value="none" <c:if test="${karmaPref == 'none'}">selected</c:if>>NONE: Please do not email me no matter the amount of karma I am given.</option>
		                </select>
		                <label for="email-pref-recommend">Recommendation Email Preference</label>
		                <select class="form-control" name="recommend-pref" id="email-pref-recommend" value="${recommendPref}">
		                    <option value="daily" <c:if test="${recommendPref =='daily'}">selected</c:if>>DAILY: Email me daily with new resources you think might be useful to me.</option>
		                    <option value="weekly" <c:if test="${recommendPref =='weekly'}">selected</c:if>>WEEKLY: Email me weekly with new resources that might be useful to me.</option>
		                    <option value="none" <c:if test="${recommendPref =='none'}">selected</c:if>>NONE: Please do not send me emails with recommendations.</option>
		                </select>
		            </div>
		            <input type="submit" value="set">
				</form>
				<form action="/change-username" method="post">
					<label for="username">Your User Name:</label> 
					<input name="username" type="text" value="${username}">
					<input type="submit" value="set">
				</form>
				<p>
					When you are ready to, you can <a href="${logoutUrl}">logout</a>.
				</p>
			</c:when>
			<c:otherwise>
				<p>
					You cannot set user preferences without being logged in!
					Please <a href="${loginUrl}">login</a> to continue.
				</p>
			</c:otherwise>
		</c:choose>
	</body>
</html>
