package com.unibo.iss.manager.beans;

import java.security.InvalidParameterException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class CommonLogger<T> implements Logger<T>{

    private final int maximumSavedMsgs;
    private final LinkedList<Message<T>> queue;

    public CommonLogger(final int maximumSavedMsgs) {
        if(maximumSavedMsgs <= 0) throw new InvalidParameterException("maximumSavedMsg can't be less than 1");
        this.maximumSavedMsgs = maximumSavedMsgs;
        this.queue = new LinkedList<>();
    }

    @Override
    public int getMaximumMsgs() {
        return this.maximumSavedMsgs;
    }

    @Override
    public List<Message<T>> getNLastMsgs(final int numberOfMsgs) {
        int availableMsgs = Math.min(numberOfMsgs, this.maximumSavedMsgs);
        return this.queue.stream().limit(availableMsgs).collect(Collectors.toList());
    }

    @Override
    public void addMsg(final Message<T> msg) {
        if(this.queue.size() == maximumSavedMsgs) this.queue.removeLast();
        this.queue.addFirst(msg);
    }

}
