$(function() {
    "use strict";

    if ($("#page-js").text().indexOf("baseline-landing") > -1) {
    	$("#what-to-expect-button").click(function(){
    		$("#what-to-expect-text").toggleClass("hidden");
    		if (! $("#about-the-science-text").hasClass("hidden")){
    			$("#about-the-science-text").addClass("hidden")
    		}
    	});
    	
    	$("#about-the-science-button").click(function(){
    		$("#about-the-science-text").toggleClass("hidden");
    		if (! $("#what-to-expect-text").hasClass("hidden")){
    			$("#what-to-expect-text").addClass("hidden")
    		}
    	});
    }
    
});