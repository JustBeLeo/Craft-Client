package com.android.sdk13.craft.entity;

public class InboxBean {
    String avatar;
    String user;
    String text;
    String date;

    public InboxBean(String avatar, String user, String text, String date) {
        this.avatar = avatar;
        this.user = user;
        this.text = text;
        this.date = date;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
