package com.anywhere.campasiliano.models.etablishment;

public class Coordination {

    private String function;
    private String user;
    private boolean status;

    public Coordination() {
    }

    public Coordination(String function, String user, boolean status) {
        this.function = function;
        this.user = user;
        this.status = status;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
