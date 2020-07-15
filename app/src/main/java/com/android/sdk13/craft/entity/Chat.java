package com.android.sdk13.craft.entity;

public class Chat {
    String avatarUrl;
    String text;
    boolean send;   // 1-发送 0-接收

    public Chat(String avatarUrl, String text, boolean send) {
        this.avatarUrl = avatarUrl;
        this.text = text;
        this.send = send;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isSend() {
        return send;
    }

    public void setSend(boolean send) {
        this.send = send;
    }
}
