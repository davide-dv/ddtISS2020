var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#test").html("");
}

function connect() {
    var socket = new SockJS('http://localhost:8080/manager');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/smartbell', function (msg) {
            var cnt = check(msg);
            $( "#smartbell" ).html(cnt);
            showMsg(cnt);
        });
        stompClient.subscribe('/topic/waiter', function (msg) {
            var cnt = check(msg);
            $( "#waiter" ).html(cnt);
            showMsg(cnt);
        });
        stompClient.subscribe('/topic/barman', function (msg) {
            var cnt = check(msg);
            $( "#barman" ).html(cnt);
            showMsg(cnt);
        });
    });
}

function check(msg) {
    var str = msg.body;
    while(str.length > 0 && str.slice(-1) != '}') str = str(0, -1);
    return beautify(handler(JSON.parse(str).content));
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function showMsg(message) {
    $("#test").append("<tr><td>" + message + "</td></tr>");
}

function handler(message) {
    var json = JSON.parse(message);
    switch (json['state']) {
        case "tableCheck":
            checkTable(json)
            break;
	case "convoyClient":
	    checkTable(json)
	    break;
	case "setTableClean":
	    if (Math.floor(json['table']) == 0) {
		$( "#table0state" ).html('clean');
	    } else {
		$( "#table1state" ).html('clean');
	    }
	    break;
        default:
            console.log("Message not handled!")
            break;
    }
    return json;
}

function checkTable(json) {
    $( "#table0state" ).html(json['table0']);
    $( "#table1state" ).html(json['table1']);
}

function beautify(json) {
    var str = JSON.stringify(json);
    str = str.replaceAll('{','').replaceAll('}','').replaceAll('\"','').replaceAll('\\','').replaceAll(',','\n\t');
    return str;
}


$(function () {
    connect();
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });

    $( "#add-client" ).click(function() { addClient(); });
    $( "#send" ).click(function() { sendMsg(); });
    $( "#leave" ).click(function() { leave(); });
});
