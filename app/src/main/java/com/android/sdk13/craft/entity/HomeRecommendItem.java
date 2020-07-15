package com.android.sdk13.craft.entity;

public class HomeRecommendItem {
    private String coverUrl;    //item封面图片地址
    private String kind;        //推荐种类
    private String title;       //标题
    private String detail;      //细节
    private int watch;          //浏览数
    private int collect;        //收藏数

    public HomeRecommendItem() {
    }

    public HomeRecommendItem(String coverUrl, String kind, String title, String detail, int watch, int collect) {
        this.coverUrl = coverUrl;
        this.kind = kind;
        this.title = title;
        this.detail = detail;
        this.watch = watch;
        this.collect = collect;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getWatch() {
        return watch;
    }

    public void setWatch(int watch) {
        this.watch = watch;
    }

    public int getCollect() {
        return collect;
    }

    public void setCollect(int collect) {
        this.collect = collect;
    }

    @Override
    public String toString() {
        return "HomeRecommendItem{" +
                "coverUrl='" + coverUrl + '\'' +
                ", kind='" + kind + '\'' +
                ", title='" + title + '\'' +
                ", watch=" + watch +
                ", collect=" + collect +
                '}';
    }
}
