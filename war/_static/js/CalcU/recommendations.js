$(function() {
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
	
	var actionContent = function(){
    	var userId = $(this).data("user");
		var content = $(this).data("content");
		var action = $(this).data("action");
		
    	$.ajax({
			type: "POST",
			url: "/recommendations",
			data: "userId="+userId+"&contentUuid="+content+"&action="+action
		});
    	
    	$(this).addClass('hovered');
    	
    	$(this).parent(".alert").fadeOut(1500, function() { $(this).remove(); });
    };
	
    $(".disinterested-button").click(actionContent);
    $(".hide-recommendation-button").click(actionContent);

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