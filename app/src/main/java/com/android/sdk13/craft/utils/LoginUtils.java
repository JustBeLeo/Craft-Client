package com.android.sdk13.craft.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.sdk13.craft.entity.User;
import com.android.sdk13.craft.entity.UserTemp;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;

public class LoginUtils {

    public static boolean isSuccess = false;
    public final static int PHONE = 1;
    public final static int EMAIL = 2;

    public static void saveLogin(String username, String password,int login_method) {
        SharedPreferences sp;
        sp = MyApplication.context.getSharedPreferences( "login", Context.MODE_PRIVATE );
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.putString( "username", username );
        editor.putString( "password", password );
        editor.putInt( "login_method", login_method );
        editor.apply();
    }

    public static void logout(){
        SharedPreferences sp;
        sp = MyApplication.context.getSharedPreferences( "login", Context.MODE_PRIVATE );
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
        UserTemp.user = new User();
        UserTemp.isLogin = false;
    }

    public static boolean loadLogin() {
        SharedPreferences sp;
        isSuccess = false;
        sp = MyApplication.context.getSharedPreferences( "login", Context.MODE_PRIVATE );
        SharedPreferences.Editor editor = sp.edit();
        String username = sp.getString( "username", "no" );
        String password = sp.getString( "password", "no" );
        int login_method = sp.getInt( "login_method", 1 );
        assert password != null;
        assert username != null;
        HttpParams params = new HttpParams();
        if (login_method == LoginUtils.PHONE){
            params.put("phone",username);
        }else {
            params.put("email",username);
        }
        if (!username.equals( "no" ) && !password.equals( "no" ))
            OkGo.<String>post( NetConfig.USER_LOGIN )
                    .params( params )
                    .params( "password", password )
                    .params("type",login_method)
                    .execute( new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            Log.e("TAG", "onSuccess: "+response.body());
                            switch (NetConfig.getCode( response.body() )) {
                                case 200:
                                    UserTemp.isLogin = true;
                                    JSONObject data = JSON.parseObject(response.body()).getJSONObject("data").getJSONObject("data");
                                    UserTemp.user = data.toJavaObject(User.class);
                                    isSuccess = true;
                                    Log.e("TAG", "SpUtils 后台登录成功");
                                    break;
                                case 404:
                                    isSuccess = false;
                                    break;
                            }
                        }
                    } );
        editor.apply();
        return isSuccess;
    }



}
