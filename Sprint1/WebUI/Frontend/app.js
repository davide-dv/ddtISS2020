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
        stompClient.subscribe('/topic/echo', function (greeting) {
            showMsg(JSON.parse(greeting.body).content);
        });
        stompClient.subscribe('/topic/notify', function (greeting) {
            showMsg(JSON.parse(greeting.body).content);
        });
        stompClient.subscribe('/topic/order', function (greeting) {
            showMsg(JSON.parse(greeting.body).content);
        });
        stompClient.subscribe('/topic/pay', function (greeting) {
            showMsg(JSON.parse(greeting.body).content);
        });
        stompClient.subscribe('/topic/leave', function (greeting) {
            showMsg(JSON.parse(greeting.body).content);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendMsg() {
    stompClient.send("/app/test", {}, JSON.stringify({'content': $("#cnt").val()}));
}

function notify() {
    stompClient.send("/app/notify", {}, JSON.stringify({'content': $("#cnt").val()}));
}

function order() {
    stompClient.send("/app/order", {}, JSON.stringify({'content': $("#cnt").val()}));
}

function pay() {
    stompClient.send("/app/pay", {}, JSON.stringify({'content': $("#cnt").val()}));
}

function leave() {
    stompClient.send("/app/leave", {}, JSON.stringify({'content': $("#cnt").val()}));
}

function showMsg(message) {
    $("#test").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendMsg(); });
    $( "#notify" ).click(function() { notify(); });
    $( "#order" ).click(function() { order(); });
    $( "#pay" ).click(function() { pay(); });
    $( "#leave" ).click(function() { leave(); });
});

