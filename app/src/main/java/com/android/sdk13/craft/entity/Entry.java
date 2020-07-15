package com.android.sdk13.craft.entity;

public class Entry {
    public final static int TYPE_NEW = 0;           // 最新词条
    public final static int TYPE_RECOMMEND = 1;     // 推荐词条
    public final static int TYPE_FOLLOW = 2;        // 关注词条

    int entryType = 0;  //0-匠人 1-手工艺品 2-手艺
    String title;       //标题
    String text;        //介绍
    String username;    //发表用户名
    int heat;           //热度（浏览量）

}
