package com.android.sdk13.craft.entity;

import java.util.Date;

/*
 * @Author sdk13
 * @Description 文章类
 * @Date 16:37 2020/6/28
 **/
public class Article {
    Long id;
    // 用户
    User user;
    // 封面url
    String cover_url;
    // 文章内容
    String text;
    // 内容图片url
    String content_url;
    // 文章标题
    String title;
    // 工艺类型
    Type type;
    // 收费状态
    int status;
    // 发布时间
    Date post_time;
    // 喜爱数量
    int like_count;
    // 收藏数量
    int favor_count;
    // 评论数量
    int comments_count;
    // 点击数量
    int click_count;
    // 分享数量
    int share_count;
    // 是否删除
    boolean delete;
    // 工艺品id
    Long craft_id;
    // 是否通过审核
    boolean pass;
    // 是否检查
    boolean check;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public String getCover_url() {
        return cover_url;
    }

    public void setCover_url(String cover_url) {
        this.cover_url = cover_url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getContent_url() {
        return content_url;
    }

    public void setContent_url(String content_url) {
        this.content_url = content_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getPost_time() {
        return post_time;
    }

    public void setPost_time(Date post_time) {
        this.post_time = post_time;
    }

    public int getLike_count() {
        return like_count;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }

    public int getFavor_count() {
        return favor_count;
    }

    public void setFavor_count(int favor_count) {
        this.favor_count = favor_count;
    }

    public int getComments_count() {
        return comments_count;
    }

    public void setComments_count(int comments_count) {
        this.comments_count = comments_count;
    }

    public int getClick_count() {
        return click_count;
    }

    public void setClick_count(int click_count) {
        this.click_count = click_count;
    }

    public int getShare_count() {
        return share_count;
    }

    public void setShare_count(int share_count) {
        this.share_count = share_count;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public Long getCraft_id() {
        return craft_id;
    }

    public void setCraft_id(Long craft_id) {
        this.craft_id = craft_id;
    }

    public boolean isPass() {
        return pass;
    }

    public void setPass(boolean pass) {
        this.pass = pass;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
