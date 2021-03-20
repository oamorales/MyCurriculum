package com.oamorales.myresume.models;

import io.realm.RealmObject;

public class PersonInfo extends RealmObject {

    private String name;
    private String birth;
    private String email;
    private String phone;
    private String address;
    private String facebook;
    private String twitter;
    private String instagram;
    private String skype;

    public PersonInfo() { }

    public PersonInfo(String name, String birth, String email, String phone, String address,
                      String facebook, String twitter, String instagram, String skype) {
        this.name = name;
        this.birth = birth;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.facebook = facebook;
        this.twitter = twitter;
        this.instagram = instagram;
        this.skype = skype;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getSkype() {
        return skype;
    }

    public void setSkype(String skype) {
        this.skype = skype;
    }
}
