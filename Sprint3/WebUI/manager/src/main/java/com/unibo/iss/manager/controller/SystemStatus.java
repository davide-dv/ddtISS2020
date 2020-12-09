package com.unibo.iss.manager.controller;

public class SystemStatus {

    private Object smartbell;
    private Object waiter;
    private Object barman;

    public Object getSmartbell() {
        return smartbell;
    }

    public void setSmartbell(Object smartbell) {
        this.smartbell = smartbell;
    }

    public Object getWaiter() {
        return waiter;
    }

    public void setWaiter(Object waiter) {
        this.waiter = waiter;
    }

    public Object getBarman() {
        return barman;
    }

    public void setBarman(Object barman) {
        this.barman = barman;
    }

    @Override
    public String toString() {
        return "SystemStatus{" +
                "smartbell='" + smartbell + '\'' +
                ", waiters='" + waiter + '\'' +
                ", barman='" + barman + '\'' +
                '}';
    }
}
