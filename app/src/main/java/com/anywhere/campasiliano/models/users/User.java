package com.anywhere.campasiliano.models.users;

public class User {

    private String user_id;
    private String username;
    private String name;
    private String last_name;
    private String first_name;
    private String phone;
    private String imageUrl;
    private String image_cover;
    private String mail;
    private String dateOfBirthday;
    private String sex;
    private String status;
    private String bio;
    private String vacation;
    private String address;
    private String commune_id;
    private String establishment_id;
    private String promotion_id;
    private String orientation_id;

    public User() {
    }

    public User(String user_id, String username, String name, String last_name, String first_name, String phone, String imageUrl, String image_cover, String mail, String dateOfBirthday, String sex, String status, String bio, String vacation, String address, String commune_id, String establishment_id, String promotion_id, String orientation_id) {
        this.user_id = user_id;
        this.username = username;
        this.name = name;
        this.last_name = last_name;
        this.first_name = first_name;
        this.phone = phone;
        this.imageUrl = imageUrl;
        this.image_cover = image_cover;
        this.mail = mail;
        this.dateOfBirthday = dateOfBirthday;
        this.sex = sex;
        this.status = status;
        this.bio = bio;
        this.vacation = vacation;
        this.address = address;
        this.commune_id = commune_id;
        this.establishment_id = establishment_id;
        this.promotion_id = promotion_id;
        this.orientation_id = orientation_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImage_cover() {
        return image_cover;
    }

    public void setImage_cover(String image_cover) {
        this.image_cover = image_cover;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getDateOfBirthday() {
        return dateOfBirthday;
    }

    public void setDateOfBirthday(String dateOfBirthday) {
        this.dateOfBirthday = dateOfBirthday;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getVacation() {
        return vacation;
    }

    public void setVacation(String vacation) {
        this.vacation = vacation;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCommune_id() {
        return commune_id;
    }

    public void setCommune_id(String commune_id) {
        this.commune_id = commune_id;
    }

    public String getEstablishment_id() {
        return establishment_id;
    }

    public void setEstablishment_id(String establishment_id) {
        this.establishment_id = establishment_id;
    }

    public String getPromotion_id() {
        return promotion_id;
    }

    public void setPromotion_id(String promotion_id) {
        this.promotion_id = promotion_id;
    }

    public String getOrientation_id() {
        return orientation_id;
    }

    public void setOrientation_id(String orientation_id) {
        this.orientation_id = orientation_id;
    }
}
