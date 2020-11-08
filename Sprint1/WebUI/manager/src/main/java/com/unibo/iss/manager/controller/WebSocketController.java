package com.unibo.iss.manager.controller;

import com.unibo.iss.manager.beans.SimpleStringMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.Random;

@Controller
public class WebSocketController {

    private TCPClientController tcpClientController = new TCPClientController();

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
        this.tcpClientController.sendNotify();
        int delay = 500;
        Thread.sleep(delay); // simulated delay
        return new SimpleStringMessage("Notified! Your id is: " + "1");
    }

    @MessageMapping("/order")
    @SendTo("/topic/order")
    public SimpleStringMessage order(SimpleStringMessage message) throws Exception {
        this.tcpClientController.sendOrder(message.getContent());
        int delay = 500;
        Thread.sleep(delay); // simulated delay
        return new SimpleStringMessage("Ordered by " + message.getContent());
    }

    @MessageMapping("/pay")
    @SendTo("/topic/pay")
    public SimpleStringMessage pay(SimpleStringMessage message) throws Exception {
        this.tcpClientController.sendPay();
        int delay = 500;
        Thread.sleep(delay); // simulated delay
        return new SimpleStringMessage("Paid!");
    }

    @MessageMapping("/leave")
    @SendTo("/topic/leave")
    public SimpleStringMessage leave(SimpleStringMessage message) throws Exception {
        int delay = 500;
        Thread.sleep(delay); // simulated delay
        return new SimpleStringMessage("Leaved!");
    }
}
