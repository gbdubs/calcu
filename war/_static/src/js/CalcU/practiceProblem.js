$(function() {
	"use strict";

	if ($("#page-js").text().indexOf("practiceProblem") > -1) { 

		var removeBookmarkOnClick = function(){
			console.log("This shouldn't be run!");
		};

		var addBookmarkOnClick = function(){
			var userId = $(this).data("user");
			var content = $(this).data("content");
			var action = "add";

			$.ajax({
				type: "POST",
				url: "/bookmark",
				data: "userId="+userId+"&contentUuid="+content+"&action="+action
			});

			$(this).unbind();
			$("a", this).text("Un-Bookmark Problem");
			$(this).click(removeBookmarkOnClick);
		};

		removeBookmarkOnClick = function(){
			var userId = $(this).data("user");
			var content = $(this).data("content");
			var action = "remove";
			$.ajax({
				type: "POST",
				url: "/bookmark",
				data: "userId="+userId+"&contentUuid="+content+"&action="+action
			});
			$(this).unbind();
			$("a", this).text("Bookmark Problem");
			$(this).click(addBookmarkOnClick);
		}


		$(".add-bookmark-button").click(addBookmarkOnClick);

		$(".remove-bookmark-button").click(removeBookmarkOnClick);
		
		$(".peek-button").click(function(){
			var peekButton = this;
			var panel = $(this).closest(".panel");
			var body = $(".panel-collapse", panel);
			var style = {
					"display" : "block",
					"max-height"  : "50px",
					"overflow": "hidden"
			}
			$(".box-title > a", panel).click(function(){
				$(peekButton).remove();
				$(body).removeAttr("style");
				$(this).off();
			});
			var currentMax = 50;
			$(body).css(style);
			$(this).off();
			$(this).click(function(){
				currentMax = currentMax + 50;
				var height = {"max-height": currentMax + "px"};
				$(body).css(height);
				if ($(body).height() < currentMax){
					$(body).removeAttr("style").addClass("in");
					$(this).remove();
				}
			});
		});
	}
});