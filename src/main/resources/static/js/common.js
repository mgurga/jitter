
function convertTime(isodate) {
    var d = new Date()
    var utcdate = new Date(isodate.substring(0,10) + " " + isodate.substring(11, 16))
    var correctdate = new Date(utcdate.getTime() - (d.getTimezoneOffset()*60*1000));
    return correctdate.toLocaleString();
}

for(let post of document.getElementsByClassName("postdate")) {
    var convertedTime = convertTime(post.innerHTML);
    post.innerHTML = convertedTime;
}

for(let num of document.getElementsByClassName("largenum")) {
    var compactNum = Intl.NumberFormat('en', { notation: 'compact' }).format(num.innerHTML);
    num.innerHTML = compactNum;
}