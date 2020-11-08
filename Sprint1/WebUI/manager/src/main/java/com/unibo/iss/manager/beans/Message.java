package com.unibo.iss.manager.beans;

public interface Message<T> {

    T getContent();

    void setContent(T content);

}
