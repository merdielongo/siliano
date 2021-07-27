package com.anywhere.campasiliano.models.posts;


public class Enclosure
{
    public String link;
    public String type;
    public String thumbnail;

    public Enclosure() {
    }

    public Enclosure(String link, String type, String thumbnail) {
        this.link = link;
        this.type = type;
        this.thumbnail = thumbnail;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
