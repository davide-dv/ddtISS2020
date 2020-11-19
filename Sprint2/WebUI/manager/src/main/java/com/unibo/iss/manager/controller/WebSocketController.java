package com.unibo.iss.manager.controller;

import com.unibo.iss.manager.beans.SimpleStringMessage;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.coap.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @Autowired
    private TCPController tcpController;

    @MessageMapping("/test")
    @SendTo("/topic/echo")
    public SimpleStringMessage echo(SimpleStringMessage message) throws Exception {
        int delay = 1000;
        Thread.sleep(delay); // simulated delay
        return new SimpleStringMessage("Echo - " + message.getContent() + " with " + delay + "ms");
    }

    @MessageMapping("/notify")
    @SendTo("/topic/notify")
    public SimpleStringMessage notify(SimpleStringMessage message) throws Exception {
        this.tcpController.sendNotify();
        return new SimpleStringMessage("Notified!");
    }

    @MessageMapping("/order")
    @SendTo("/topic/order")
    public SimpleStringMessage order(SimpleStringMessage message) throws Exception {
        this.tcpController.sendOrder(message.getContent());
        return new SimpleStringMessage("Ordered by " + message.getContent());
    }

    @MessageMapping("/pay")
    @SendTo("/topic/pay")
    public SimpleStringMessage pay(SimpleStringMessage message) throws Exception {
        this.tcpController.sendPay(message.getContent());
        return new SimpleStringMessage("Paid!");
    }

    @MessageMapping("/leave")
    @SendTo("/topic/leave")
    public SimpleStringMessage leave(SimpleStringMessage message) throws Exception {
        return new SimpleStringMessage("Leaved!");
    }

}
