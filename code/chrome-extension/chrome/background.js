// Copyright 2018 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

'use strict';

chrome.runtime.onInstalled.addListener(function() {
  chrome.storage.sync.set({color: '#3aa757'}, function() {
    console.log("The color is green.");
  });
});

chrome.browserAction.onClicked.addListener(function(tab) {
 chrome.tabs.executeScript(null, {file: "testScript.js"});
});

//TODO: Fetch all upvotes from SQL.
var allVotes = {
  "123": [0,1],
  "1234": [3,4],
  "78": [90, 1],
  "79": [100,0],
  "80": [12,5],
  "81": [20, 50],
  "82": [15,23]
}

chrome.runtime.onMessage.addListener(
  function(request, sender, sendResponse) {
    console.log(sender.tab ?
      "from a content script:" + sender.tab.url :
      "from the extension");
    console.log('-->'+request.getAllVotes);
    // if (typeof request.getAllVotes === undefined || Object.keys(request.getAllVotes).length === 0){
    //   sendResponse({"allVotes": allVotes});
    // }
    if (request.getAllVotes){
      sendResponse({"allVotes": allVotes});
    }
});

var upvotes = 0;

chrome.runtime.onMessage.addListener(
  function(request, sender, sendResponse) {
    console.log(sender.tab ?
      "from a content script:" + sender.tab.url :
      "from the extension");
    if (request.getVote){
      sendResponse({vote: upvotes});
    }
    if (Number.isInteger(request.vote)) {
      upvotes+=1
      sendResponse({vote: upvotes});
    }
    if (request.upvote){
      console.log(request);
      var pid = request[Object.keys(request)[0]];
      console.log("..."+pid)
      allVotes[pid] = request.pid;
      allVotes[pid][0]+=1;
      sendResponse({vote: allVotes[pid][0]});
    }
  });

