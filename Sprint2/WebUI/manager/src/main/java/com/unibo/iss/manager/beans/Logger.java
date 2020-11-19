package com.unibo.iss.manager.beans;

import java.util.List;

public interface Logger<T> {

    int getMaximumMsgs();

    List<Message<T>> getNLastMsgs(int numberOfMsgs);

    void addMsg(Message<T> msg);

}
