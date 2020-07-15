package com.android.sdk13.craft.utils;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.Toolbar;

/*
 * @Author sdk13
 * @Description 全局变量缓存
 * @Date 14:52 2020/6/30
 **/
public class MyApplication extends Application {

    //在整个应用执行过程中，需要提供的变量

    public static Handler handler;//需要使用的handler
    public static Context context;//需要使用的handler
    public static Thread mainThread;//提供主线程对象
    public static int mainThreadId;//提供主线程对象的id
    public static Toolbar toolbar = null;

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        context = this.getApplicationContext();
        mainThread = Thread.currentThread();//实例化当前Application的线程即为主线程
        mainThreadId = android.os.Process.myTid();//获取当前线程的id
    }

}
