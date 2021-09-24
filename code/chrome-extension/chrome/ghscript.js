chrome.storage.local.get('methodName', function (result) {
	doSearch(result.methodName);
	document.getElementById("loc").scrollIntoView();
});

function doSearch(_methodName) {
	"use strict";
    if (($('.file').find($(".pl-en:contains(" + _methodName + ")")).html() === _methodName)) {
		// find the method declaration and begin highlight
		var parentLine = $('.file').find($(".pl-en:contains(" + _methodName + ")")).first().parent().html();
		var replaced = $('.file').find($(".pl-en:contains(" + _methodName + ")")).first().parent().html().replace(parentLine, '<span id="loc" style="background-color: #FFFF00">' + parentLine + '</span>');
		$('.file').find($(".pl-en:contains(" + _methodName + ")")).first().parent().html(replaced);
		
		// walk through table elements to highlight the rest of the method
		var tableData = document.getElementsByClassName("blob-code blob-code-inner js-file-line");
		var stack = [];
		var methodDecFound = false;
		var endOfMethod = false;
		for (var i=0; i<tableData.length && !endOfMethod; i++) {
			// keep '{' in a stack, pop when a '}' is found, and when stack is empty we've reached the end
			if (methodDecFound == true) {
				tableData[i].innerHTML = '<span style="background-color: #FFFF00">' + tableData[i].innerHTML + '</span>';
				
				if (tableData[i].innerText.indexOf('}') > -1) {
					stack.pop();
					console.log("pop" + i);
					if (stack.length == 0 || stack.length == undefined) {
						methodDecFound = false;
						endOfMethod = true;
						console.log("end of method");
					}
				}
				if (tableData[i].innerText.indexOf('{') > -1) {
					stack.push('{');
					console.log("push" + i);
				}
			}
			
			// find the highlighted code
			if (tableData[i].contains(document.getElementById("loc"))) {
				methodDecFound = true;
				stack.push('{');
				console.log("push" + i);
			}
		}
	}
}