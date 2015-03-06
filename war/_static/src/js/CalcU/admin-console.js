$(function() {
    "use strict";

    if ($("#page-js").text().indexOf("admin-console") > -1) {
    	
	    $(".delete-achievement-button").click(function(){
	    	var uuid = $(this).closest("tr").data("uuid");
	    	
	    	$.ajax({
	    		type: "POST",
	    		url: "/admin/achievement",
	    		data: "uuid="+uuid+"&action=delete"
	    	});
	    	
	    	$(this).closest("tr").remove();
	    });
	    
	    $(".delete-report-button").click(function(){
	    	var uuid = $(this).closest("tr").data("uuid");
	    	
	    	$.ajax({
	    		type: "POST",
	    		url: "/report/manage",
	    		data: "reportUuid="+uuid+"&action=delete-report"
	    	});
	    	
	    	$(this).closest("tr").remove();
	    });
	    
	    $(".delete-reported-content-button").click(function(){
	    	var uuid = $(this).closest("tr").data("uuid");
	    	
	    	$.ajax({
	    		type: "POST",
	    		url: "/report/manage",
	    		data: "reportUuid="+uuid+"&action=delete-content"
	    	});
	    	
	    	$(this).closest("tr").remove();
	    });
	    
	    $(".flag-reporter-button").click(function(){
	    	var uuid = $(this).closest("tr").data("uuid");
	    	
	    	$.ajax({
	    		type: "POST",
	    		url: "/report/manage",
	    		data: "reportUuid="+uuid+"&action=flag-reporter"
	    	});
	    	
	    	$(this).closest("tr").remove();
	    });
	    
    }
    
});