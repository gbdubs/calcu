$(function() {
    "use strict";
    
    if ($("#page-js").text().indexOf("rating-system") > -1) { 
    
	    $(".rating-system-horizontal").each(function(){
	    	var button = $(".submit-rating", this);
	    	var contentUuid = $(this).data("uuid");
	    	var userId = $(this).data("user");
	    	
	    	var helpfulness = $("[name=helpfulness]", this);
	    	var difficulty = $("[name=difficulty]", this);
	    	var quality = $("[name=quality]", this);
	    	
	    	$(button).click(function(){
	    		var h = $(helpfulness).val();
	    		var d = $(difficulty).val();
	    		var q = $(quality).val();
	    		
	    		$.ajax({
	        		type: "POST",
	        		url: "/rating/submit",
	        		data: "userId="+userId+"&contentUuid="+contentUuid+"&helpfulness="+h+"&difficulty="+d+"&quality="+q,
	        		success: function(data){
	        			$(button).removeClass("btn-default").addClass("btn-success").text("Successfully Rated").addClass("disabled").unbind();
	        			$(helpfulness).trigger('configure', {"readOnly": true});
	        			$(difficulty).trigger('configure', {"readOnly": true});
	        			$(quality).trigger('configure', {"readOnly": true});
	        			
	        		}
	        	});
	    	});
	    });
	    
    }
});