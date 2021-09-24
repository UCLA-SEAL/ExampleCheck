// var app = chrome.runtime.getBackgroundPage();

 window.onload = function() {
    var a =0;
    function myFunction()
    {
        alert('hey')
        console.log('hi')
    }

    document.getElementById('clickme').onclick = myFunction;
}