package com.anywhere.campasiliano.models.users;

public class Leader {

    private String student;
    private String function;
    private boolean status;

    public Leader() {
    }

    public Leader(String student, String function, boolean status) {
        this.student = student;
        this.function = function;
        this.status = status;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
