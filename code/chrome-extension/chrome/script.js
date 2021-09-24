function getRepoFromURL(url){
	var urlParts = url.split('/');
	return urlParts[3]+'/'+urlParts[4];
}

//Global variable so that upvote/downvote change values in current session too.
var jsonData = {}

// setup the websocket
var socket = new WebSocket('wss://examplore.cs.ucla.edu:4000/');
// var socket = new WebSocket('wss://127.0.0.1:4000/');

// Handle any errors that occur.
socket.onerror = function(error) {
	//alert('WebSocket Error: ' + error);
};	

// Make sure we're connected to the WebSocket before trying to send anything to the server
socket.onopen = function(event) {
	// parse HTML for <code> tags and send their content + post id + generated snippet id to server
	// three classes: "question", "answer", and "answer accepted-answer"
	var codeBlocks = document.getElementsByTagName("code");
	var prevParentPost = null;
	var parentPost;
	var blockIndex;
	
	var message = {
		snippets:[]
	};
	
	// iterate through the code blocks and create JSON objects out of them
	for (var i = 0; i < codeBlocks.length; i++)
	{
		// filter out code snippets from question post; we just want snippets from answers
		parentPost = getParentPost(codeBlocks[i]);
		if (parentPost != null)
		{
			if (parentPost != prevParentPost)
			{
				blockIndex = 0;
			}
			
			message.snippets.push({
				"id":getParentPost(codeBlocks[i]).getAttribute("data-answerid") + "-" + blockIndex,
				"snippet":codeBlocks[i].innerText //note: textContent strips out newlines
			});
			blockIndex++;
			
			prevParentPost = parentPost;
		}
	}
	
	// send the code example to the backend for parsing and analysis
	socket.send(JSON.stringify(message.snippets));
};

// Show a disconnected message when the WebSocket is closed.
socket.onclose = function(event) {
	//alert('Disconnected from your IDE.', false);
};

function getUpvote(apiCall, j){
	return jsonData[apiCall][j].pVote
}

function getDownvote(apiCall, j){
	return jsonData[apiCall][j].pDownvote
}

message = {}
// Handle messages sent by the backend.
socket.onmessage = function(event) {
	"use strict";
	message = event.data;
	//else jsonData is already populated. 
	if (event!=''){
		jsonData = JSON.parse(message);
	}
	
	// hard-coded URL for testing
	// var tempURL = "https://github.com/coderplay/h2-bitmap/blob/833860f31d50cc060434340fb6226f913da5e7f5/h2/src/main/org/h2/store/fs/FilePathNio.java";
	
	// each API call has one or more required patterns + violations mapped to it
	// this generates a single popover for the API call whose pages correspond to the different violations
	for (var apiCall in jsonData) {
		// first iterate through the violations of this API call to find all unique snippet ids
		var csIdSet = new Set();
		for (var i=0; i < jsonData[apiCall].length; i++) {
			var csID = jsonData[apiCall][i].csID;
			csIdSet.add(csID);
		}
		
		var csIdArray = Array.from(csIdSet);
		for(var i=0; i < csIdArray.length; i++) {
			var id = csIdArray[i];
			// generate popover window for the API call in each snippet
			var content = "";
			var count = 0;
			for (var j=0; j < jsonData[apiCall].length; j++) {
				// save an instance of this API call's csID
				// this is kind of awkward but it would be more so to change the JSON message's structure
				var csID = jsonData[apiCall][j].csID;
				if (csID != id) {
					continue;
				} 
				// the actual index of the alt pattern is off by one (e.g. jsonData[apicCall][0] should be in data-page 1, etc.)
				var iOffset = count + 1;

				// TODO: this will be pExample when that part is implemented
				//var tempCodeString = '<span style="background-color: #FFFF00">try {</span> \n\r FileChannel.write(); \n\r<span style="background-color: #FFFF00">} catch (IOException e) { \n\rthrow new IllegalStateException(e); \n\r}</span>';
				// var tempCodeString = 'if (jsonElement != null){\n    jsonElement.getAsString();\n}';

				// if this is the first page, add outer div and make it visible
				if (count == 0) {
					content += '<div class="pagination-container"><div data-page="1">';
				}
				else {
					content += '<div data-page="'+ iOffset +'" style="display:none;">';
				}

				// add the rest of the data-page
				// the pID is the id of the pattern in this page; we'll use it to keep track of voting
				content += '<p>' + jsonData[apiCall][j].vioMessage + '</p>'
				+ '<table><tbody><tr>'
				// + '<td class="voteCell_'+ iOffset +'"><div class="upvote" id="' + jsonData[apiCall][j].pID +'"></div><div class="voteSpacer"></div>'
				// + '<div class="downvote" id="' + jsonData[apiCall][j].pID +'"></div></td>'
				+ '<td class="codeCell_'+ iOffset +'"><pre class="fix"><code>'+ jsonData[apiCall][j].pExample +'</code></pre></td></tr></tbody></table>';

				var votePid = jsonData[apiCall][j].pID;
				// Upvote buttons.
				content += '<table class="voteContainer" align="right"><tbody>'
				+ '<tr class="voteCell_'+ iOffset +'">'
						//+ '<td class="upvote" id="' + jsonData[apiCall][j].pID +'">'
						+ '<td class="voteButton upvote" id="' + votePid +'">'
						+ '<input type="image" src='+chrome.extension.getURL("images/upvote.svg")+' name="up" class="submit" value="" height="20px" id="increaseUpVote'+votePid+'"/></td>'
						+ '<td class="voteNumber" id="upvoteNumber'+votePid+'" apiCall='+apiCall+ ' j= '+j+ ' >'+getUpvote(apiCall, j)+'</td>'
						//+ '<td class="downvote" id="' + jsonData[apiCall][j].pID +'">'
						+ '<td class="voteButton downvote" >'
						+ '<input type="image" src='+chrome.extension.getURL("images/downvote.svg")+' name="down" class="submit" value="" height="20px" id="increaseDownVote'+votePid+'"/></td>'
						+ '<td class="voteNumber" id="downvoteNumber'+votePid+'" apiCall='+apiCall+' j='+j+' >'+getDownvote(apiCall, j)+'</td></tr></tbody></table>';

						if (jsonData[apiCall][j].hasOwnProperty('ex1')) {
							content+= 'See this in a GitHub example: </br>'
							+ '<a class="ghLink" id="'+jsonData[apiCall][j].ex1Method+'" target="_blank" href="'+jsonData[apiCall][j].ex1+'")">'+getRepoFromURL(jsonData[apiCall][j].ex1)+'</a></br>'
							+ '<a class="ghLink" id="'+jsonData[apiCall][j].ex2Method+'" target="_blank" href="'+jsonData[apiCall][j].ex2+'")">'+getRepoFromURL(jsonData[apiCall][j].ex2)+'</a></br>'
							+ '<a class="ghLink" id="'+jsonData[apiCall][j].ex3Method+'" target="_blank" href="'+jsonData[apiCall][j].ex3+'")">'+getRepoFromURL(jsonData[apiCall][j].ex3)+'</a>';
						}
						content += '</div>';
						count++;
					}

					content += '<div id="pagination-container" class="pagination-container"><ul class="pagination pagination-centered"><li class="active" data-page="1"><a href="#" >1</a></li>';

			// add any subsequent pagination buttons
			// the page offset is off by two since we've already added the first page
			// add one to the length of the array for +2 offset, -1 page
			for (var k=2; k < count + 1; k++) {
				content += '<li data-page="'+ k +'"><a href="#" >'+ k +'</a></li>';
			}
			
			content += '</ul></div></div>';
			doSearch(apiCall, id, content);
		}
	}
	// initialize all popovers
	// $('[data-toggle="popover"]').popover({
	// 	container: 'body'
	// }).popover('show');;

	return false;
}

// find the location of the API call, highlight it, and generate a popover on it
function doSearch(_apiCall, _csID, _content) {
	"use strict";
	// $('#popoverLink' + _csID + _apiCall).html(_apiCall);
	if (_csID != null) {
		var parentPostID = _csID.substr(0, _csID.indexOf('-')); 
		// TODO: use csIndex to find the code snippet the text is in
		var csIndex = _csID.substr(_csID.indexOf('-') + 1);
		// If the text is in a span of class "pln" or "typ", surround the text with a popover and highlight
		// else, the text we found is not in the SO code snippet proper or is not an exact match
		if (($('#answer-' + parentPostID).find($(".pln:contains(" + _apiCall + ")")).html() === _apiCall)) {
			var replaced = $('#answer-' + parentPostID).find($(".pln:contains(" + _apiCall + ")")).first().html().replace(_apiCall, '<a data-toggle="popover" id="popoverLink' + _csID + _apiCall + '" data-title="Potential API Misuse" data-container="body" data-html="true"><span class="api-misuse_'+_apiCall+'" style="background-color: #FFFF00">' + _apiCall + '</span></a>');
			$('#answer-' + parentPostID).find($(".pln:contains(" + _apiCall + ")")).first().html(replaced);
			if (document.getElementById('popoverLink' + _csID + _apiCall) != null) {
				document.getElementById('popoverLink' + _csID + _apiCall).setAttribute('data-content', _content);
			}
		} else if (($('#answer-' + parentPostID).find($(".typ:contains(" + _apiCall + ")")).html() === _apiCall)) {
			var typ_elements = $('#answer-' + parentPostID).find($(".typ:contains(" + _apiCall + ")")); 
			// a quick dirty fix here---assume the first one is the type declaration and the second one is the constructor call
			var replaced = typ_elements.first().html().replace(_apiCall, '<a data-toggle="popover" id="popoverLink' + _csID + _apiCall + '" data-title="Potential API Misuse" data-container="body" data-html="true"><span class="api-misuse_'+_apiCall+'" style="background-color: #FFFF00">' + _apiCall + '</span></a>');
			if(typ_elements.length > 1) {
				if(typ_elements.first().html() === _apiCall && typ_elements.eq(1).html() === _apiCall) {
					// the first time to highlight the constructor call
					typ_elements.eq(1).html(replaced);
				} else {
					// the popover is opened, need to close it
					if (document.getElementById('popoverLink' + _csID + _apiCall) != null) {
						document.getElementById('popoverLink' + _csID + _apiCall).setAttribute('data-content', _content);
					}
				}
			} else {
				// the constructor call is likely to be the first call, highlight it
				typ_elements.first().html(replaced);
			}

			// set the popover to invisible at first
			if (document.getElementById('popoverLink' + _csID + _apiCall) != null) {
				document.getElementById('popoverLink' + _csID + _apiCall).setAttribute('data-content', _content);
			}
		} else if($('#answer-' + parentPostID).find($('.api-misuse_'+_apiCall)).html() === _apiCall) {
			// the popover is opened, need to close it
			if (document.getElementById('popoverLink' + _csID + _apiCall) != null) {
				document.getElementById('popoverLink' + _csID + _apiCall).setAttribute('data-content', _content);
			}
		}

		$('#popoverLink' + _csID + _apiCall).popover();
		// var waypoint = new Waypoint({
		// 	element: document.getElementById('answer-' + parentPostID),
		// 	handler: function(direction) {
		// 		$('#popoverLink' + _csID + _apiCall).popover('show');
		// 	}
		// })

	}
}

// script for running the pagination
$(document.body).on('shown.bs.popover', function () {
	var paginationHandler = function(){
    // store pagination container so we only select it once
    var $paginationContainer = $(".pagination-container"),
    $pagination = $paginationContainer.find('.pagination');

    // click event
    $pagination.find("li a").on('click.pageChange',function(e){
    	e.preventDefault();
        // get parent li's data-page attribute and current page
        var parentLiPage = $(this).parent('li').data("page"),
        currentPage = parseInt( $(".pagination-container div[data-page]:visible").data('page') ),
        numPages = $paginationContainer.find("div[data-page]").length;
        
        $('.active').not($($(this).parent('li'))).removeClass('active');
        $(this).parent('li').toggleClass('active');

        // make sure they aren't clicking the current page
        if ( parseInt(parentLiPage) !== parseInt(currentPage) ) {
            // hide the current page
            $paginationContainer.find("div[data-page]:visible").hide();

            if ( parentLiPage === '+' ) {
                // next page
                $paginationContainer.find("div[data-page="+( currentPage+1>numPages ? numPages : currentPage+1 )+"]").show();
              } else if ( parentLiPage === '-' ) {
                // previous page
                $paginationContainer.find("div[data-page="+( currentPage-1<1 ? 1 : currentPage-1 )+"]").show();
              } else {
                // specific page
                $paginationContainer.find("div[data-page="+parseInt(parentLiPage)+"]").show();
              }

            }
          });
  };
  $( document ).ready( paginationHandler );
});

// Close the popover on any outside click
// $('html').on('click', function(e) {
//   if (typeof $(e.target).data('original-title') == 'undefined' && !$(e.target).parents().is('.popover.in')) {
//     $('[data-original-title]').popover('hide');
//   }
// });

$('html').on('click', function(e) {
	$('[data-toggle="popover"]').each(function() {
		//Check if the popover is active / contain an aria-describedby with a value
		if ($(this).attr('aria-describedby') != null ) { 
			var id = $(this).attr('aria-describedby');
			//Look if the click is a child of the popover box or if it's the button itself or a child of the button
			if (!$(e.target).closest(".popover-content").length && $(e.target).attr("aria-describedby") != id && !$(e.target).closest('[aria-describedby="'+id+'"').length) { 
                //trigger a click as if you clicked the button
                $('[aria-describedby="'+id+'"]').trigger( "click" );
                socket.onmessage('');
                // $('[aria-describedby="'+id+'"]').popover('toggle')
              }
            }
          });
});


$(document).on( "click", ".upvote", function (ele) {
	id = ele.target.id;
	pid = id.slice(14);
	currentUpvotes = parseInt($("#upvoteNumber" + pid).html());
	$("#upvoteNumber" + pid).html(currentUpvotes+1); 
	$("#"+id).attr("src", chrome.extension.getURL("images/upvote2.svg"));
	// make a JSON message
	// should send server whether it's an up or downvote,
	// plus its id to identify which pattern it belongs to
	socket.send('{"vote":'+(currentUpvotes+1)+',"downvote": null, "id":"'+pid+'"}');
	apiCall = $("#upvoteNumber" + pid).attr('apiCall')
	j = $("#upvoteNumber" + pid).attr('j')
	jsonData[apiCall][j].pVote = currentUpvotes+1 
	// socket = new WebSocket('ws://131.179.224.239:4000/');

	// disable the button so the user can't send more than one upvote
	this.disabled = true;
});

$(document).on( "click", ".downvote", function (ele) {
	// socket.send('{"vote":-1, "id":"'+this.id+'"}');
	id = ele.target.id;
	pid = id.slice(16);
	currentDownvotes = parseInt($("#downvoteNumber" + pid).html());
	socket.send('{"vote": null, "downvote":'+(currentDownvotes+1)+', "id":"'+pid+'"}');
	// $("#downvoteNumber" + pid).html(currentDownvotes+1); 
	document.getElementById("downvoteNumber" + pid).innerHTML = currentDownvotes+1
	$("#"+id).attr("src", chrome.extension.getURL("images/downvote2.svg"));

	apiCall = $("#downvoteNumber" + pid).attr('apiCall')
	j = $("#downvoteNumber" + pid).attr('j')
	jsonData[apiCall][j].pDownvote = currentDownvotes+1 
	//$(this).css("border-top", "12px solid red");
	this.disabled = true;
});

// this function walks through the calling element's parents until it reaches
// the question/answer post div in which the code resides
function getParentPost(_child) {
	var object = _child;
	while (object.className != "question" 
		&& object.className != "answer" 
		&& object.className != "answer accepted-answer") 
	{
		object = object.parentNode;
	}

	if (object.className == "question")
	{
		object = null;
	}
	return object;
}

$(document).on( "click", ".ghLink", function () {
	chrome.storage.local.set({"methodName": this.id}, function() {});
});
