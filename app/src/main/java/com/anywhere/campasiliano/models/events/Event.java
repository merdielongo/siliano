package com.anywhere.campasiliano.models.events;

public class Event {

    private int id;
    private String establishment;
    private String name;
    private String desc;
    private String type;
    private String cover;
    private String file;
    private String price;
    private String date_event;
    private String location;

    public Event() {
    }

    public Event(int id, String establishment, String name, String desc, String type, String cover, String file, String price, String date_event, String location) {
        this.id = id;
        this.establishment = establishment;
        this.name = name;
        this.desc = desc;
        this.type = type;
        this.cover = cover;
        this.file = file;
        this.price = price;
        this.date_event = date_event;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEstablishment() {
        return establishment;
    }

    public void setEstablishment(String establishment) {
        this.establishment = establishment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate_event() {
        return date_event;
    }

    public void setDate_event(String date_event) {
        this.date_event = date_event;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
