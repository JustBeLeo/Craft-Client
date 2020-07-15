package com.android.sdk13.craft.entity;

import java.util.Date;

public class Announce extends BaseEntity {
    // 发送者id
    private Long sender_id;
    // 标题
    private String title;
    // 内容
    private String content;
    // 发布时间
    private Date post_time;
    // 是否被删除
    private boolean delete;
    // 点击次数
    private int click_count;

    public Long getSender_id() {
        return sender_id;
    }

    public void setSender_id(Long sender_id) {
        this.sender_id = sender_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPost_time() {
        return post_time;
    }

    public void setPost_time(Date post_time) {
        this.post_time = post_time;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public int getClick_count() {
        return click_count;
    }

    public void setClick_count(int click_count) {
        this.click_count = click_count;
    }
}
