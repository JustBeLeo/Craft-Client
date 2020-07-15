package com.android.sdk13.craft;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

import com.android.sdk13.craft.entity.UserTemp;
import com.android.sdk13.craft.utils.NetConfig;

import static com.android.sdk13.craft.utils.LoginUtils.loadLogin;

public class SplashActivity extends AppCompatActivity {

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 去掉窗口标题
        requestWindowFeature( Window.FEATURE_NO_TITLE);
        // 隐藏顶部的状态栏
        getWindow().addFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_splash );
        String ip = NetConfig.getHostIP();
        Log.e("TAG", "本机ip地址:" + ip);
        UserTemp.isLogin = loadLogin();
        handler.postDelayed( this::startMainActivity, 1500 );
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        startMainActivity();
        return super.onTouchEvent( event );
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages( null );
        super.onDestroy();
    }

    public void startMainActivity() {
        startActivity( new Intent( SplashActivity.this,MainActivity.class ) );
        finish();
    }
}
