package com.anywhere.campasiliano.models.chats;

public class Group {

    private String user;
    private String establishment;
    private String promotion;
    private String name;
    private String logo;
    private String desc;

    public Group() {
    }

    public Group(String user, String establishment, String promotion, String name, String logo, String desc) {
        this.user = user;
        this.establishment = establishment;
        this.promotion = promotion;
        this.name = name;
        this.logo = logo;
        this.desc = desc;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getEstablishment() {
        return establishment;
    }

    public void setEstablishment(String establishment) {
        this.establishment = establishment;
    }

    public String getPromotion() {
        return promotion;
    }

    public void setPromotion(String promotion) {
        this.promotion = promotion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
