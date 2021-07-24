package com.anywhere.campasiliano.models.etablishment;

public class Orientation {

    private String name;
    private Promotion promotion;

    public Orientation() {
    }

    public Orientation(String name, Promotion promotion) {
        this.name = name;
        this.promotion = promotion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }
}
