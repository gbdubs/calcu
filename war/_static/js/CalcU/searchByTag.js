$(function() {
    "use strict";

    $("#tags-input").tagsInput({
    	'width': '100%',
    	'height': 'auto',
    	'defaultText': 'Type Tags Here',
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
    
    $(".search-result-page").each(function(){
    	var parent = $(this).parent(".box-body");
    	var id = "#" + $(this).attr("id");
    	var tabId = id + "-tab";
    	$(tabId, parent).click(function(){
    		$(".search-result-page", parent).each(function(){
    			$(this).addClass("hidden");
    		});
    		$(id, parent).removeClass("hidden");
    		$(".result-page-tab", parent).each(function(){
    			$(this).removeClass("selected")
    		});
    		$(this).addClass("selected");
    	});
    });
    
});