$(function() {
    "use strict";
    
    if ($("#page-js").text().indexOf("tutorial") > -1) { 
    	$(".large-input-group-button").click(function(){
    		var value = $("#tags-input").val();
    		var url = "/search/" + value;
    		window.open(url, '_blank');
    	})
    	
    	var allBadges = $(".achievement-badge");
    	
    	$(allBadges).click(function(){
    		$(this).toggleClass("completed");
    	});
    	
    	$(".fa-bookmark-o.toggle-me").click(function(){
    		$(this).toggleClass("fa-bookmark-o").toggleClass("fa-bookmark");
    	});
    }
});