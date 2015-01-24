$(function() {
    "use strict";

    $(".delete-achievement-button").click(function(){
    	var uuid = $(this).closest("tr").data("uuid");
    	
    	console.log(uuid);
    	
    	$.ajax({
    		type: "POST",
    		url: "/admin/achievement",
    		data: "uuid="+uuid+"&action=delete"
    	});
    	
    	$(this).closest("tr").remove();
    });
    
});