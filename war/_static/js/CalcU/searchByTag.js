$(function() {
    "use strict";

    $("#tags-input").tagsInput({
    	'width': '100%',
    	'height': 'auto',
    	'defaultText': 'To Add Tag Press Enter',
    	'removeWithBackspace': true,
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