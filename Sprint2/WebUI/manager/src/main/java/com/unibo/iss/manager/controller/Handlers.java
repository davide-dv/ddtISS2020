package com.unibo.iss.manager.controller;

import com.unibo.iss.manager.beans.SimpleStringMessage;
import org.eclipse.californium.core.CoapResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.function.BiFunction;

@Controller
public class Handlers {

    //@Autowired
    private SimpMessagingTemplate template;
    public Handlers(SimpMessagingTemplate template) {
	this.template = template;
    }

    private BiFunction<String, CoapResponse, String> send = (ch, msg) -> {
        String log = "SEND TO MANAGER:\n\tSOURCE: " + ch + "\n\tMSG: " + msg.getResponseText();
        this.template.convertAndSend(ch, new SimpleStringMessage(msg.getResponseText()));
        return log;
    };

    public interface oneParameter {
        void apply(CoapResponse response) throws Exception;
    }

    public void testSend(final String channel, String message) {
        this.template.convertAndSend(channel, new SimpleStringMessage(message));
    }

    public oneParameter smartbell = response -> {
        System.out.println(send.apply("/topic/smartbell", response));
    };

    public oneParameter waiter = response -> {
        System.out.println(send.apply("/topic/waiter", response));
    };

    public oneParameter barman = response -> {
        System.out.println(send.apply("/topic/barman", response));
    };

}
