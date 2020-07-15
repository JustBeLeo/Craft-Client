package com.android.sdk13.craft.entity;

/**
 * @Package: Craft.com.java.leo.entity
 * @Author: Leo
 * @Date: 2020/7/8 13:33
 * @Description:
 */

public class Focus extends BaseEntity {

    User focus_user;

    User user;

    public User getFocus_user() {
        return focus_user;
    }

    public void setFocus_user(User focus_user) {
        this.focus_user = focus_user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
