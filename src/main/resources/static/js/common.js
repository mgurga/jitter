
function convertTime(isodate) {
    var d = new Date()
    var utcdate = new Date(isodate.substring(0,10) + " " + isodate.substring(11, 16))
    var correctdate = new Date(utcdate.getTime() - (d.getTimezoneOffset()*60*1000));
    return correctdate.toLocaleString();
}

for(let post of document.getElementsByClassName("postdate")) {
    var noPostDate = post.innerHTML.replace("Post Date: ", "");
    var convertedTime = convertTime(noPostDate);
    post.innerHTML = "Posted: " + convertedTime;
}