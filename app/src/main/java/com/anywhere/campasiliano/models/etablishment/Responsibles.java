package com.anywhere.campasiliano.models.etablishment;

public class Responsibles {

    private String name;
    private String last_name;
    private String first_name;
    private String mail;
    private String photo;
    private String function;

    public Responsibles() {
    }

    public Responsibles(String name, String last_name, String first_name, String mail, String photo, String function) {
        this.name = name;
        this.last_name = last_name;
        this.first_name = first_name;
        this.mail = mail;
        this.photo = photo;
        this.function = function;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }
}
