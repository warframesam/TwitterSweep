const MAX_TWEETS = 20;
var tweetCount = 0;

var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    /*if (connected) {
        $("#tweets").show();
    }
    else {
        $("#tweets").hide();
    }*/
}

function connect() {
    var socket = new SockJS('/twittersweep-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        sendFilter();
        //if(!usernameSet.size==0 && !keywordSet.size==0)
        document.getElementById('tweet-container').insertAdjacentHTML('afterbegin','<div class="d-flex justify-content-center" id="loading"><div class="spinner-border text-primary" role="status"><span class="sr-only">Loading...</span></div></div>');
		
        stompClient.subscribe('/twittersweep/tweets', function (message) {
        
    	if(JSON.parse(message.body).text.localeCompare("")!=0)
            showTweets(JSON.parse(message.body).text);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    if(document.getElementById("loading")!=null)
    	document.getElementById("loading").remove();
    setConnected(false);
    console.log("Disconnected");
}

function sendFilter() {
    stompClient.send("/app/filter", {}, JSON.stringify({'usernames':Array.from(usernameSet), 'keywords':Array.from(keywordSet)}));
}

function showTweets(tweet) {
    var divContainer = document.getElementById('tweet-container');
    tweetCount++;

	if(tweetCount == MAX_TWEETS) {
		divContainer.removeChild(divContainer.lastChild);
		tweetCount--;
    }
    if(document.getElementById("loading")!=null)
    	document.getElementById("loading").remove();
    
    divContainer.insertAdjacentHTML('afterbegin','<div class="card bg-primary m-4"><div class="card-body text-white">' + tweet + '</div></div>');
}