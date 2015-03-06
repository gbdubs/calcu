$(function() {
    "use strict";

    if ($("#page-js").text().indexOf("practiceProblemCreation") > -1) { 
    
	    $("#tags-input").tagsInput({
	    	'width': '100%',
	    	'height': 'auto',
	    	'defaultText': 'To Add Tag, Press Enter',
	    	'removeWithBackspace': true,
	    });
	     
	    $(".wysihtml5-beam-me-up").wysihtml5();
   
    }
});