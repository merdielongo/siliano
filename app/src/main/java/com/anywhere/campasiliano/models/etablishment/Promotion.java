package com.anywhere.campasiliano.models.etablishment;

import java.util.ArrayList;
import java.util.List;

public class Promotion {

    private String name;
    private String vacation;
    private String separator;
    private String option;

    public Promotion() {
    }

    public Promotion(String name, String vacation, String separator, String option) {
        this.name = name;
        this.vacation = vacation;
        this.separator = separator;
        this.option = option;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVacation() {
        return vacation;
    }

    public void setVacation(String vacation) {
        this.vacation = vacation;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    @Override
    public String toString() {
        return "Promotion{" +
                "name='" + name + '\'' +
                ", vacation=" + vacation +
                ", separator=" + separator +
                ", option=" + option +
                '}';
    }
}
