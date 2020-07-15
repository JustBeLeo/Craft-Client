package com.android.sdk13.craft.entity;


/*
* Description 工艺品类型
* Date 2020/7/8 0:10
* Param
**/
public class Type extends BaseEntity{

    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Type{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
