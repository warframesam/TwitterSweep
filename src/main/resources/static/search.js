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
    var buttonText = document.getElementById("username-search").value;
    var divContainer = document.getElementById("username-container");
    if(buttonText!="" && !usernameSet.has(buttonText)) {
        usernameSet.add(buttonText);
        divContainer.insertAdjacentHTML('beforeend', '<button class="btn bg-secondary btn-sm m-1"><font class="text-white">' + buttonText + '</font><font class="font-weight-bold text-primary cross" id="username-' + buttonText + '-cross" onmouseover="crossDanger(this.id)" onmouseout="crossPrimary(this.id)" onclick="deleteKey(this.id)"> X</font></button>');
    }
    sendFilter();
}
function addKeywords() {
    var buttonText = document.getElementById("keyword-search").value;
    var divContainer = document.getElementById("keyword-container");
    if(buttonText!="" && !keywordSet.has(buttonText)) {
        keywordSet.add(buttonText);
        divContainer.insertAdjacentHTML('beforeend', '<button class="btn bg-secondary btn-sm m-1"><font class="text-white">' + buttonText + '</font><font class="font-weight-bold text-primary cross" id="keyword-' + buttonText + '-cross" onmouseover="crossDanger(this.id)" onmouseout="crossPrimary(this.id)" onclick="deleteKey(this.id)"> X</font></button>');
    }
    sendFilter();
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
    sendFilter();
}