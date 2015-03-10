$(function() {
	"use strict";
	
	if ($("#page-js").text().indexOf("recommendations") > -1) { 
	
		var toggleBookmarkButton = function(){
	    	var userId = $(this).data("user");
			var content = $(this).data("content");
			var action = $(this).data("action");
			
	    	$.ajax({
				type: "POST",
				url: "/bookmark",
				data: "userId="+userId+"&contentUuid="+content+"&action="+action
			});
	    	
	    	if (action === "add") {
	    		$(this).data("action","remove");
	    		$(this).removeClass("fa-bookmark-o").addClass("fa-bookmark");
	    	} else {
	    		$(this).data("action","add");
	    		$(this).removeClass("fa-bookmark").addClass("fa-bookmark-o");
	    	}
	    };
		
		$(".toggle-bookmark-button").click(toggleBookmarkButton);
		
		var hideRecommendedContent = function(){
	    	var userId = $(this).data("user");
			var content = $(this).data("content");
			var action = $(this).data("action");
			
	    	$.ajax({
				type: "POST",
				url: "/recommendations",
				data: "userId="+userId+"&contentUuid="+content+"&action="+action
			});
	    	
	    	$(this).addClass('hovered');
	    	
	    	var fading = $(this).parent(".alert").addClass("fading");
	    	setTimeout(function () {$(fading).remove();}, 1200);
	    	
	    };
		
	    $(".hide-recommendation-button").click(hideRecommendedContent);
	    
	    var disinterestedContent = function () {
	    	var notYetSent = $(this).hasClass("fa-thumbs-o-down");
	    	var alreadySent = $(this).hasClass("fa-thumbs-down");
	    	
	    	var userId = $(this).data("user");
			var content = $(this).data("content");
			var action = $(this).data("action");
			
	    	if (notYetSent) {
	    		$.ajax({
	        		type: "POST",
	    			url: "/recommendations",
	    			data: "userId="+userId+"&contentUuid="+content+"&action="+action
	        	});
	    		
	    		$(this).addClass("fa-thumbs-down").removeClass("fa-thumbs-o-down");
	    		$(".interested-button", $(this).parent()).removeClass("fa-thumbs-up").addClass("fa-thumbs-o-up");
	    	} else if (alreadySent) {
	    		action = "unmark-" + action;
	    		$.ajax({
	        		type: "POST",
	    			url: "/recommendations",
	    			data: "userId="+userId+"&contentUuid="+content+"&action="+action
	        	});
	    		
	    		$(this).removeClass("fa-thumbs-down").addClass("fa-thumbs-o-down");
	    	}
	    }
	    
	    $(".disinterested-button").click(disinterestedContent);
	
	    var interestedContent = function () {
	    	var notYetSent = $(this).hasClass("fa-thumbs-o-up");
	    	var alreadySent = $(this).hasClass("fa-thumbs-up");
	    	
	    	var userId = $(this).data("user");
			var content = $(this).data("content");
			var action = $(this).data("action");
			
	    	if (notYetSent) {
	    		$.ajax({
	        		type: "POST",
	    			url: "/recommendations",
	    			data: "userId="+userId+"&contentUuid="+content+"&action="+action
	        	});
	    		$(this).addClass("fa-thumbs-up").removeClass("fa-thumbs-o-up");
	    		$(".disinterested-button", $(this).parent()).removeClass("fa-thumbs-down").addClass("fa-thumbs-o-down");
	    	} else if (alreadySent) {
	    		action = "unmark-" + action;
	    		
	    		$.ajax({
	        		type: "POST",
	    			url: "/recommendations",
	    			data: "userId="+userId+"&contentUuid="+content+"&action="+action
	        	});
	    		
	    		$(this).removeClass("fa-thumbs-up").addClass("fa-thumbs-o-up");
	    	}
	    }
	    
	    $(".interested-button").click(interestedContent);
	    
	    var showAll = function (){
	    	var userId = $(this).data("user");
			var action = $(this).data("action");
			
	    	$.ajax({
				type: "POST",
				url: "/recommendations",
				data: "userId="+userId+"&action="+action
			});
	    	
	    	$(this).addClass('disabled');
	    	$(this).text('All Recommendations Unhidden');
	    };
	    
	    $(".show-all-recommendations").click(showAll);
	    
	    $(".search-result-page").each(function(){
	    	var parent = $(this).parent(".all-search-results");
	    	var id = "#" + $(this).attr("id");
	    	var tabId = id + "-tab";
	    	$(tabId, parent).click(function(){
	    		$(".search-result-page", parent).each(function(){
	    			$(this).addClass("hidden");
	    		});
	    		$(id, parent).removeClass("hidden");
	    		$(".result-page-tab", parent).each(function(){
	    			$(this).removeClass("btn-success").addClass("btn-default")
	    		});
	    		$(this).removeClass("btn-default").addClass("btn-success");
	    	});
	    });
	}
});