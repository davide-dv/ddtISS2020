var stompClient = null;
var clientCounter = 0;
var article =   "<article id=sec" + clientCounter + " class=\"style1\">" +
                "<span class=\"image\">" +
                "<img src=\"images/pic01.jpg\" alt=\"\" />" +
                "</span>" +
                "<a>" +
                "<h2>CLIENT" +
                "</h2>" +
                "<div class=\"content\">" +
                "<div class=\"col-md-6\">" +
                "<div id=\"box-client\" class=\"form-inline\">" +
                "<div class=\"button-container\">" +
                "<button class=\"btn btn20 btn-default\" type=\"notify\" onclick=\"notify()\">NOTIFY" +
                "</button>" +
                "</div>" +
                "<div class=\"button-container\">" +
                "<button class=\"btn btn20 btn-default\" type=\"order\" onclick=\"order()\">ORDER" +
                "</button>" +
                "</div>" +
                "<div class=\"button-container\">" +
                "<button class=\"btn btn20 btn-default\" id=btn" + clientCounter +  " type=\"pay\" onclick=\"pay(this.id)\">PAY" +
                "</button>" +
                "</div>" +
                "</div>" +
                "</div>" +
                "</div>" +
                "</a>" +
                "</article>";

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
        stompClient.subscribe('/topic/echo', function (msg) {
            showMsg(JSON.parse(msg.body).content);
        });
        stompClient.subscribe('/topic/notify', function (msg) {
            showMsg(JSON.parse(msg.body).content);
        });
        stompClient.subscribe('/topic/order', function (msg) {
            showMsg(JSON.parse(msg.body).content);
        });
        stompClient.subscribe('/topic/pay', function (msg) {
            showMsg(JSON.parse(msg.body).content);
        });
        stompClient.subscribe('/topic/leave', function (msg) {
            showMsg(JSON.parse(msg.body).content);
        });
    });
}

function addClient() {
    clientCounter++;
    console.log("Client added");
    document.getElementById("msgLabel").style.visibility = "visible";
    $( "#clients" ).append(article);
    $( ".notify_btn" ).click(function() { notify(); });
    $( ".order_btn" ).click(function() { order(); });
    $( ".pay_btn" ).click(function() { pay(); });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendMsg() {
    stompClient.send("/app/test", {}, JSON.stringify({'content': $("#msgLabel").val()}));
}

function notify() {
    stompClient.send("/app/notify", {}, JSON.stringify({'content': $("#msgLabel").val()}));
}

function order() {
    stompClient.send("/app/order", {}, JSON.stringify({'content': $("#msgLabel").val()}));
}

function pay(id) {
    stompClient.send("/app/pay", {}, JSON.stringify({'content': $("#msgLabel").val()}));
    setTimeout(() => {
        leave(id);
    }, 3000);
}

function leave(id) {
    document.getElementById("sec" + id.substring(3)).remove();
    stompClient.send("/app/leave", {}, JSON.stringify({'content': $("#msgLabel").val()}));
}

function showMsg(message) {
    $("#test").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    connect();
    addClient();
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });

    $( "#add-client" ).click(function() { addClient(); });
    $( "#send" ).click(function() { sendMsg(); });
    $( "#leave" ).click(function() { leave(); });
});