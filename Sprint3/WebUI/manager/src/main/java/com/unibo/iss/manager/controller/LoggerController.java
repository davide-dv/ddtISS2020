package com.unibo.iss.manager.controller;

import com.unibo.iss.manager.beans.CommonLogger;
import com.unibo.iss.manager.beans.Logger;
import com.unibo.iss.manager.beans.Message;
import com.unibo.iss.manager.beans.SimpleStringMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Optional;

@RestController
public class LoggerController {

    private final CommonLogger<String> clientLogger = new CommonLogger<>(97);
    private final CommonLogger<String> waiterogger = new CommonLogger<>(97);
    private final CommonLogger<String> tableLogger = new CommonLogger<>(97);
    private final CommonLogger<String> barmanLogger = new CommonLogger<>(97);
    private final CommonLogger<String> smartbellLogger = new CommonLogger<>(97);

    private Optional<Logger<String>> getLoggerByOwner(final String owner) {
        Optional<Logger<String>> logger;
        switch (owner) {
            case "client":
                logger = Optional.of(clientLogger);
                break;
            case "waiter":
                logger = Optional.of(waiterogger);
                break;
            case "table":
                logger = Optional.of(tableLogger);
                break;
            case "barman":
                logger = Optional.of(barmanLogger);
                break;
            case "smart":
                logger = Optional.of(smartbellLogger);
                break;
            default:
                logger = Optional.empty();
        }
        return logger;
    }

    @GetMapping("/api/{owner}/get-last-n-msgs")
    synchronized public List<Message<String>> getLastNMsgs(@PathVariable String owner, @RequestParam(value = "nom", defaultValue = "1") int nom) {
        return this.getLoggerByOwner(owner).orElseGet(() -> new CommonLogger<>(1)).getNLastMsgs(nom);
    }

    @GetMapping("/api/{owner}/add-msg")
    synchronized public void addMsgToOwner(@PathVariable String owner, @RequestParam(value = "msg", defaultValue = "none") String msg) {
        this.getLoggerByOwner(owner).orElseGet(() -> new CommonLogger<>(1)).addMsg(new SimpleStringMessage(msg));
    }


}