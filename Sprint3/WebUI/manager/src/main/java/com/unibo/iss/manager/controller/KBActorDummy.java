package com.unibo.iss.manager.controller;

import java.util.Observable;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

public class KBActorDummy extends Observable implements Runnable {

    private String[] tempStatus = {"Temperature_OK", "Temperature_NO"};

    private Supplier<String> nextTemperatureStatus = () -> tempStatus[new Random().nextInt(2)];

    private Function<Integer, String> nextMsg = (id) ->  "{\\\"smartbell\\\": " +
                                                    "{\\\"state\\\":\\\"checkTemperature\\\"," +
                                                    "\\\"clientID\\\":" + id + "," +
                                                    "\\\"msg\\\":\\\"" + nextTemperatureStatus.get() + "\\\"}," +
                                                    "\\\"waiter\\\": {}, \\\"barman\\\":{} }";

    private volatile boolean isRunning = true;

    @Override
    public void run() {
        while (this.isRunning) {
            try {
                this.setChanged();
                this.notifyObservers(nextMsg.apply(new Random().nextInt(10)));
                Thread.sleep(new Random().nextInt(6500) + 500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void stop() {
        this.isRunning = false;
    }

    public synchronized boolean isRunning() {
        return this.isRunning;
    }

}
