package com.android.sdk13.craft.entity;

import java.util.Date;

/**
 * @Package: Craft.com.java.leo.entity
 * @Author: Leo
 * @Date: 2020/7/7 15:58
 * @Description:
 */

public class Comment extends BaseEntity {
    // 发布用户
    User user;
    // 是否回复
    boolean reply;
    // 父评论id
    Long parent_id;
    // 喜爱数
    int like_count;
    // 发表时间
    Date post_time;
    // 是否被删除
    boolean delete;
    // 评论内容
    String content;
    // 评论类型 0-评论 1-跟评
    int type;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isReply() {
        return reply;
    }

    public void setReply(boolean reply) {
        this.reply = reply;
    }

    public Long getParent_id() {
        return parent_id;
    }

    public void setParent_id(Long parent_id) {
        this.parent_id = parent_id;
    }

    public int getLike_count() {
        return like_count;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
