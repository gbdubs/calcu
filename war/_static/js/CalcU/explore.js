$(function() {
    "use strict";
    
    $(".content-rotating").each(function () {
    	console.log("here");
    	var contentRotating = this;
    	var buttonGroup = $('.btn-group', this);
    	var defaultButton = $('.btn', buttonGroup);
    	var i = 1;
    	$(".row", this).each( function () {
    		var panel = this;
    		var id = $(this).attr('id');
    		var newId = id + "-button";
    		var newButton = $(defaultButton).clone().appendTo(buttonGroup);
    		$(newButton).attr('id', newId).removeClass('hidden').text('' + i);
    		i = i + 1;
    		
    		$(newButton).click( function () {
    			$('.row', contentRotating).addClass("hidden");
    			$(panel).removeClass("hidden");
    			$(".btn", buttonGroup).removeClass('btn-primary').addClass('btn-default');
    			$(this).addClass('btn-primary').removeClass('btn-default');
    		});
    	});
    });
    
    
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
		$(this).addClass("bookmarked-button").click(removeBookmarkOnClick);
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
		$(this).removeClass("bookmarked-button").click(addBookmarkOnClick);
	}
	
	
	$(".add-bookmark-button").click(addBookmarkOnClick);
	
	$(".remove-bookmark-button").click(removeBookmarkOnClick);
});