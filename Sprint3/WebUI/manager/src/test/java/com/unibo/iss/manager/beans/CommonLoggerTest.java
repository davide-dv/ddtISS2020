package com.unibo.iss.manager.beans;

import org.junit.jupiter.api.Test;
import java.security.InvalidParameterException;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class CommonLoggerTest {

    private static final int maximumSavedMsgs = 97;

    @Test
    void invalidParameters() {
        try {
            CommonLogger<Integer> testerLogger = new CommonLogger<>(-1);
        } catch (InvalidParameterException e) {
            assertEquals(e.getMessage(), "maximumSavedMsg can't be less than 1");
        }
    }

    @Test
    void getMaximumMsgs() {
        CommonLogger<Integer> testerLogger = new CommonLogger<>(maximumSavedMsgs);
        assertEquals(testerLogger.getMaximumMsgs(), maximumSavedMsgs);
    }

    @Test
    void addMsg() {
        CommonLogger<String>testerLogger = new CommonLogger<>(maximumSavedMsgs);
        Message<String> msg = new SimpleStringMessage("test");
        testerLogger.addMsg(msg);
        assertEquals(msg.getContent(), Optional.of(testerLogger.getNLastMsgs(1).get(0).getContent()).orElse(""));
    }

    @Test
    void getNLastMsgs() {
        CommonLogger<String>testerLogger = new CommonLogger<>(maximumSavedMsgs);
        LinkedList<Message<String>> list = new LinkedList(new Random(100).ints( maximumSavedMsgs ).boxed().map(x -> new SimpleStringMessage("" + x)).collect( Collectors.toList()));
        list.forEach(testerLogger::addMsg);
        LinkedList<Message<String>> list2 = new LinkedList(testerLogger.getNLastMsgs(maximumSavedMsgs));
        list.forEach(x -> {
            if(!x.getContent().equals(list2.removeLast().getContent())) fail();
        });
    }
}