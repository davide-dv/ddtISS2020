package com.unibo.iss.manager.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.unibo.iss.manager.beans.SimpleStringMessage;
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

    private BiFunction<String, String, String> send = (ch, msg) -> {
        String log = "SEND TO MANAGER:\n\tSOURCE: " + ch + "\n\tMSG: " + msg;
	System.out.println(log);
        this.template.convertAndSend(ch, new SimpleStringMessage(msg));
        return log;
    };

    public interface oneParameter {
        void apply(String msg) throws Exception;
    }

    public void testSend(final String channel, String message) {
        this.template.convertAndSend(channel, new SimpleStringMessage(message));
    }

    public oneParameter smartbell = msg -> {
        System.out.println(send.apply("/topic/smartbell", msg));
    };

    public oneParameter waiter = msg -> {
        System.out.println(send.apply("/topic/waiter", msg));
    };

    public oneParameter barman = msg -> {
        System.out.println(send.apply("/topic/barman", msg));
    };

    public oneParameter knowledgebase = msg -> {
        try {
            spliterator(msg);
        } catch (Exception e) {
            System.out.println("SPLITERATOR ERROR:\n\t" + e.getMessage());		
        }
    };

    public void spliterator(final String msgStatus) throws Exception {
        String adaptedJson = jsonAdapter(msgStatus);
        Gson gson = new Gson();
        Gson gsonB = new GsonBuilder().create();
        SystemStatus systemStatus = gson.fromJson(adaptedJson, SystemStatus.class);
        smartbell.apply(gsonB.toJson(systemStatus.getSmartbell()));
        waiter.apply(gsonB.toJson(systemStatus.getWaiter()));
        barman.apply(gsonB.toJson(systemStatus.getBarman()));
    }

    private static String jsonAdapter(final String json) {
        return  json.replaceAll("\\s","")
                .replace("\\\"", "\"");
    }

}
