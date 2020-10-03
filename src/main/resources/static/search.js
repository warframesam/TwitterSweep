var usernameSet = new Set();
var keywordSet = new Set();

document.getElementById("username-search").addEventListener("keypress", function(event) {
    if(event.code=="Enter")
        addUsernames();
});

document.getElementById("keyword-search").addEventListener("keypress", function(event) {
    if(event.code=="Enter")
        addKeywords();
});

function addUsernames() {
    var searchText = document.getElementById("username-search").value;
    var divContainer = document.getElementById("username-container");
    if(searchText.localeCompare("")!=0 && !usernameSet.has(searchText)) {
        usernameSet.add(searchText);
        divContainer.insertAdjacentHTML('beforeend', '<button class="btn bg-secondary btn-sm m-1"><font class="text-white">' + searchText + '</font><font class="font-weight-bold text-primary cross" id="username-' + searchText + '-cross" onmouseover="crossDanger(this.id)" onmouseout="crossPrimary(this.id)" onclick="deleteKey(this.id)"> X</font></button>');
        
        if($('#connect').prop('disabled')) {
	        if(document.getElementById('loading')==null) {
	        	document.getElementById('tweet-container').innerHTML = '';
	    		document.getElementById('tweet-container').insertAdjacentHTML('afterbegin','<div class="d-flex justify-content-center" id="loading"><div class="spinner-border text-primary" role="status"><span class="sr-only">Loading...</span></div></div>');
	    		tweetCount = 0;
	    	}
    	sendFilter();
    	}
    }
}
function addKeywords() {
    var searchText = document.getElementById("keyword-search").value;
    var divContainer = document.getElementById("keyword-container");
    if(searchText.localeCompare("")!=0 && !keywordSet.has(searchText)) {
        keywordSet.add(searchText);
        divContainer.insertAdjacentHTML('beforeend', '<button class="btn bg-secondary btn-sm m-1"><font class="text-white">' + searchText + '</font><font class="font-weight-bold text-primary cross" id="keyword-' + searchText + '-cross" onmouseover="crossDanger(this.id)" onmouseout="crossPrimary(this.id)" onclick="deleteKey(this.id)"> X</font></button>');
        
        if($('#connect').prop('disabled')) {
	        if(document.getElementById('loading')==null) {
	        	document.getElementById('tweet-container').innerHTML = '';
	    		document.getElementById('tweet-container').insertAdjacentHTML('afterbegin','<div class="d-flex justify-content-center" id="loading"><div class="spinner-border text-primary" role="status"><span class="sr-only">Loading...</span></div></div>');
	    		tweetCount = 0;
	    	}
    	sendFilter();
    	}
    }
}
function crossDanger(crossId) {
    document.getElementById(crossId).classList.remove("text-primary");
    document.getElementById(crossId).classList.add("text-danger");
}
function crossPrimary(crossId) {
    document.getElementById(crossId).classList.remove("text-danger");
    document.getElementById(crossId).classList.add("text-primary");
}
function deleteKey(crossId) {
    var crossIdStr = new String(crossId);
    document.getElementById(crossId).parentElement.remove();
    if(crossIdStr.substring(0,crossIdStr.indexOf('-'))=='username')
        usernameSet.delete(crossIdStr.substring(crossIdStr.indexOf('-')+1,crossIdStr.lastIndexOf('-')));
    else
        keywordSet.delete(crossIdStr.substring(crossIdStr.indexOf('-')+1,crossIdStr.lastIndexOf('-')));
    
    if($('#connect').prop('disabled')) {
	    if(document.getElementById('loading')==null) {
	    	document.getElementById('tweet-container').innerHTML = '';
			if(usernameSet.size!=0 || keywordSet.size!=0)
				document.getElementById('tweet-container').insertAdjacentHTML('afterbegin','<div class="d-flex justify-content-center" id="loading"><div class="spinner-border text-primary" role="status"><span class="sr-only">Loading...</span></div></div>');
	    	tweetCount = 0;
	    }
	    else {
	    	if(usernameSet.size==0 && keywordSet.size==0)
	    		document.getElementById('loading').remove();
	    }
	    	
    	sendFilter();
	}
}