package com.unibo.iss.manager.controller;

import java.util.function.Function;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

public class TCPClientController {

    private CoapClient smartbell = new CoapClient();
    private String uriStr = "coap://192.168.1.125:8050/ctxtearoom/smartbell";
    private CoapClient waiter = new CoapClient();
    private String uriWaiter = "coap://192.168.1.125:8050/ctxtearoom/waiter";
    private String notify = "msg(notify, dispatch, web, smartbell, notify(X), 1)";
    private Function<String,String> order = (id) -> "msg(order, dispatch, web, waiter, order(Twinings, " + id + "), 1)";
    private String pay = "msg(payment, dispatch, web, waiter, payment(1), 1)";

    public TCPClientController() {
        CoapHandler handler = new CoapHandler() {

            @Override
            public void onLoad(CoapResponse response) {
                // TODO Auto-generated method stub
                System.out.println("Response: " + response.getResponseText());

            }

            @Override
            public void onError() {
                // TODO Auto-generated method stub

            }
        };
        //Thread.sleep(1000);
        smartbell.setURI(this.uriStr);
        smartbell.observe(handler);
        waiter.setURI(this.uriWaiter);
    }

    public void sendNotify() {
        //this.waiter.put(notify, MediaTypeRegistry.TEXT_PLAIN);
    	this.smartbell.put(notify, MediaTypeRegistry.TEXT_PLAIN);
        System.out.println("NOTIFY");
    }

    public void sendOrder(final String id) {
    	System.out.println(order.apply(id));
        this.waiter.put(order.apply(id), MediaTypeRegistry.TEXT_PLAIN);
    }

    public void sendPay() {
        this.waiter.put(pay, MediaTypeRegistry.TEXT_PLAIN);
    }

}
