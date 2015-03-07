$(function(){
	
	"use strict";
	if ($("#page-js").text().indexOf("content-approval") > -1) {
		
		var allSidebarLis = $(".content-approval-sidebar li");
		
		$(allSidebarLis).click(function(){
			var id = $(this).attr('id');
			
			$(allSidebarLis).removeClass("selected");
			
			$(this).addClass("selected");
			
			$(".content-approval-content-display").addClass("hidden");
			
			$("#" + id + "-viewer").removeClass("hidden");
		});
	}
});