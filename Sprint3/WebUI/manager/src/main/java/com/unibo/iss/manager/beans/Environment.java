package com.unibo.iss.manager.beans;

import java.util.HashMap;
import java.util.Map;

public class Environment {

    private static enum TableState {
        CLEAN,
        DIRTY
    }
    private static enum SmartbellState {
        OK,
        NO
    }
    private static enum WaiterState {
        WAIT_CLIENT,
        GO_TO_THE_DOOR,
        ACCOMPANIES_TO_THE_TABLE,
        BRING_THE_BILL,
        ACCOMPANIES_TO_THE_EXIT
    }
    private static enum BarmanState {
        WAITING,
        PREPARE,
        COMPLETED
    }
    private static class Table {

        private static TableState state;

        public void setState(TableState newState) {
            state = newState;
        }

        public TableState getState() {
            return state;
        }

    }
    private static Map<String, Table> tableMap = new HashMap<>();
    private static SmartbellState smartbellState;
    private static WaiterState waiterState;
    private static BarmanState barmanState;

    public static  Map<String, Table> getTableMap() {
        return tableMap;
    }

    public static  void setTableMap(final Map<String, Table> newTableMap) {
        tableMap = newTableMap;
    }

    public static  SmartbellState getSmartbellState() {
        return smartbellState;
    }

    public static  void setSmartbellState(final SmartbellState newSmartbellState) {
        smartbellState = newSmartbellState;
    }

    public static  WaiterState getWaiterState() {
        return waiterState;
    }

    public static  void setWaiterState(final WaiterState newWaiterState) {
        waiterState = newWaiterState;
    }

    public static  BarmanState getBarmanState() {
        return barmanState;
    }

    public static  void setBarmanState(final BarmanState newBarmanState) {
        barmanState = newBarmanState;
    }
}
