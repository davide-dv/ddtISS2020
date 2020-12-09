package com.unibo.iss.manager.controller;

import java.util.Observable;
import java.util.Observer;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class TCPController {

    @Autowired
    private SimpMessagingTemplate template;
    private Handlers handler;

    private CoapClient smartbell = new CoapClient();
    private String uriStr = "coap://127.0.0.1:8050/ctxtearoom/smartbell";
    private CoapClient waiter = new CoapClient();
    private String uriWaiter = "coap://127.0.0.1:8050/ctxtearoom/waiter";
    private CoapClient barman = new CoapClient();
    private String uriBarman = "coap://127.0.0.1:8050/ctxtearoom/barman";
    private CoapClient knowledgebase = new CoapClient();
    private String uriKnowledgebase = "coap://127.0.0.1:8050/ctxtearoom/knowledgebase";
    private String notify = "msg(notify, dispatch, web, smartbell, notify(X), 1)";
    private BiFunction<String, String, String> order = (tea, id) -> "msg(order, dispatch, web, waiter, order(" + tea + ", " + id + "), 1)";
    private Function<String, String> pay = (table) -> "msg(payment, dispatch, web, waiter, payment(" + table +  "), 1)";

    private CoapHandler coapHandler(Handlers.oneParameter callback) {
        return new CoapHandler() {
            @Override
            public void onLoad(CoapResponse response) {
                try {
                    callback.apply(response.getResponseText());
                } catch (Exception e) {
                    System.out.println("CoapHandler exception: " + e.getMessage());
                }
            }

            @Override
            public void onError() {

            }
        };
    }

    public TCPController(final SimpMessagingTemplate template) {
	this.template = template;
        this.handler = new Handlers(this.template);
        knowledgebase.setURI(this.uriKnowledgebase);
       	this.knowledgebase.observe(coapHandler(this.handler.knowledgebase));
	this.smartbell.setURI(this.uriStr);
	this.waiter.setURI(this.uriWaiter);
    }

    public void sendNotify() {
        this.smartbell.put(notify, MediaTypeRegistry.TEXT_PLAIN);
        System.out.println("NOTIFY");
    }

    public void sendOrder(final String content) {
        String[] particles = content.trim().split(",");
        String msg = "";
        try {
            msg = order.apply(particles[0], particles[1]);
            this.waiter.put(msg, MediaTypeRegistry.TEXT_PLAIN);
        } catch (Exception e) {
            System.out.println("ERROR IN PARAMETERS");
        }
    }

    public void sendPay(final String table) {
        this.waiter.put(pay.apply(table), MediaTypeRegistry.TEXT_PLAIN);
    }

}