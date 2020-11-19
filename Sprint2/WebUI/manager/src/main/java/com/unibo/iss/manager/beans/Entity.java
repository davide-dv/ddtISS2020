package com.unibo.iss.manager.beans;

public class Entity implements EntityAbstraction {

    private String content;

    public Entity(final String content) {
        this.content = content;
    }

    @Override
    public String getContent() {
        return this.content;
    }
}
