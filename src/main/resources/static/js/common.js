function convertTime(isodate) {
    var d = new Date()
    var utcdate = new Date(isodate.substring(0,10) + " " + isodate.substring(11, 16))
    var correctdate = new Date(utcdate.getTime() - (d.getTimezoneOffset()*60*1000));
    return correctdate.toLocaleString();
}

function addAccount() {
    var acc = prompt("Input account handle, leave blank to cancel");
    if(acc == "")
        return;
    var newCA;
    if(localStorage.getItem("customaccounts") == null)
        newCA = acc;
    else
        newCA = localStorage.getItem("customaccounts") + "," + acc;
    localStorage.setItem("customaccounts", newCA);
    createAccountButtons();
}

function removeAccount(acchandle) {
    var cas = localStorage.getItem("customaccounts").split(",");
    var out = "";
    for(let ca of cas)
        if(!(ca == acchandle))
            out += ca + ",";
    out = out.substring(0, out.length - 1);
    localStorage.setItem("customaccounts", out);
    createAccountButtons();
}

function createAccountButtons() {
    var cabar = document.getElementById("cabar");
    cabar.innerHTML = "";
    var cas = localStorage.getItem("customaccounts");
    if(cas == null)
        return;
    cas = cas.split(",");
    for(let ca of cas) {
        if(ca != "") {
            var opening = document.createElement("p");
            var ending = document.createElement("p");
            opening.innerHTML = "[";
            ending.innerHTML = "] ";
            cabar.append(opening);
            var acclink = document.createElement("a");
            acclink.className = "favoriteaccount";
            acclink.innerHTML = ca;
            acclink.href = "/" + ca;
            cabar.append(acclink);
            var deletebtn = document.createElement("button");
            deletebtn.innerHTML = "X";
            deletebtn.className = "cabarbtn";
            deletebtn.addEventListener("click", function() {removeAccount(ca)});
            cabar.append(deletebtn);
            cabar.append(ending);
        }
    }
}

for(let post of document.getElementsByClassName("postdate")) {
    var convertedTime = convertTime(post.innerHTML);
    post.innerHTML = convertedTime;
}

for(let num of document.getElementsByClassName("largenum")) {
    var compactNum = Intl.NumberFormat('en', { notation: 'compact' }).format(num.innerHTML);
    num.innerHTML = compactNum;
}

createAccountButtons();