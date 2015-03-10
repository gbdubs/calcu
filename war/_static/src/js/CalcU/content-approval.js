$(function(){
	
	"use strict";
	if ($("#page-js").text().indexOf("content-approval") > -1) {
		
		var allSidebarLis = $(".content-approval-sidebar li");
		
		$(allSidebarLis).click(function(){
			var id = $(this).attr('id');
			
			$(allSidebarLis).removeClass("selected");
			
			$(this).addClass("selected");
			
			$(".content-approval-content-display").addClass("hidden");
			
			var editingScreen = $("#" + id + "-viewer");
			
			if (! $(editingScreen).hasClass("wysihtml5-activated")){
				$(".wysihtml5-beam-me-up-later", editingScreen).wysihtml5();
			}
			
			$(editingScreen).removeClass("hidden").addClass("wysihtml5-activated");
		});
		
		var contentApprovalActionButtons = $(".content-approval-action-buttons");
		
		$(".content-approval-action-button").click(function(){
			var action = $(this).data("action");
			var contentType = $(this).data("type");
			var uuid = $(this).parent().data("uuid");
		
			if (action == "save"){
				var parent = $(this).closest(".content-approval-content-display");
				var title = $("[name=title]", parent).val();
				var body = $("[name=body]", parent).val();
				var solution = $("[name=solution]", parent).val();
				var tags = $("[name=tags]", parent).val();
				
				$.ajax({
					type: "POST",
					url: "/admin/content-approval",
					data: "uuid="+uuid+"&action="+action+"&title="+title+"&body="+body+"&solution="+solution+"&tags="+tags
				});
				
			} else {
				$.ajax({
					type: "POST",
					url: "/admin/content-approval",
					data: "uuid="+uuid+"&action="+action+"&contentType="+contentType
				});
			}
			
		
			var color = "";
			if ($(this).hasClass("btn-success")){
				color = "green";
			} else if ($(this).hasClass("btn-warning")){
				color = "yellow";
			} else if ($(this).hasClass("btn-primary")){
				color = "light-blue";
			} else if ($(this).hasClass("btn-info")){
				color = "aqua";
			} else if ($(this).hasClass("btn-danger")){
				color = "red";
			}
			
			$(this).closest(".content-approval-action-buttons").children("button")
				.removeClass().addClass("btn").addClass("btn-default").addClass("disabled");
			$(this).removeClass("btn-default").addClass("bg-" + color).addClass("disabled")
			
			var id = $(this).closest(".content-approval-content-display").attr("id");
			
			var newId = "#" + id.substring(0, id.length - 7);
			$(newId).addClass("bg-" + color);
		});
	}
	
});