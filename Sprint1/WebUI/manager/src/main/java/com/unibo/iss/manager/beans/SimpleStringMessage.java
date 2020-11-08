package com.unibo.iss.manager.beans;

public class SimpleStringMessage implements Message<String>{

    private String content;

    public SimpleStringMessage() {}

    public SimpleStringMessage(final String content) {
        this.content = content;
    }

    @Override
    public String getContent() {
        return this.content;
    }

    @Override
    public void setContent(final String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "SimpleStringMessage{" +
                "content='" + content + '\'' +
                '}';
    }
}
