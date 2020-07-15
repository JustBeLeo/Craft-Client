package com.android.sdk13.craft.entity;

import java.util.ArrayList;

public class OffLine {
    int id;
    String name;
    String des;
    String date;
    int count;          // 参与人数
    String locate;      // 地点
    ArrayList<String> images;    // 图片序列
    ArrayList<Integer> ids;          // 参与者id

    public OffLine() {
        images = new ArrayList<>();
        ids = new ArrayList<>();
    }

    public OffLine(String name, String des, String date, String locate, ArrayList<String> images,int count) {
        this.name = name;
        this.des = des;
        this.date = date;
        this.locate = locate;
        this.images = images;
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getLocate() {
        return locate;
    }

    public void setLocate(String locate) {
        this.locate = locate;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages( ArrayList<String> images) {
        this.images = images;
    }

    public ArrayList<Integer> getIds() {
        return ids;
    }

    public void setIds(ArrayList<Integer> ids) {
        this.ids = ids;
    }

    @Override
    public String toString() {
        return "OffLine{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", des='" + des + '\'' +
                ", date='" + date + '\'' +
                ", count=" + count +
                ", locate='" + locate + '\'' +
                ", images=" +images +
                ", ids=" + ids +
                '}';
    }
}
