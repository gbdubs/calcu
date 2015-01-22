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
    	
    	console.log("Here1");
    	
    	$.ajax({
    		type: "POST",
    		url: "/bookmark",
    		data: "userId="+userId+"&contentUuid="+content+"&action="+action
    	});
    	
    	$(this).unbind();
    	$(this).addClass("bookmarked-button").click(removeBookmarkOnClick);
    	$(this).parent(".alert").removeClass("alert-info").addClass("alert-success");
    };
    
    removeBookmarkOnClick = function(){
    	var userId = $(this).data("user");
    	var content = $(this).data("content");
    	var action = "remove";
    	
    	console.log("Here2");
    	
    	$.ajax({
    		type: "POST",
    		url: "/bookmark",
    		data: "userId="+userId+"&contentUuid="+content+"&action="+action
    	});
    	
    	$(this).unbind();
    	$(this).removeClass("bookmarked-button").click(addBookmarkOnClick);
    	$(this).parent(".alert").removeClass("alert-success").addClass("alert-info");
    }
    
    
    $(".add-bookmark-button").click(addBookmarkOnClick);
    
    $(".remove-bookmark-button").click(removeBookmarkOnClick);
    
});