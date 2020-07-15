package com.android.sdk13.craft.login_reg;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.sdk13.craft.R;
import com.android.sdk13.craft.entity.User;
import com.android.sdk13.craft.entity.UserTemp;
import com.android.sdk13.craft.custom.ui.BackgroundVideoView;
import com.android.sdk13.craft.utils.NetConfig;
import com.android.sdk13.craft.utils.LoginUtils;
import com.android.sdk13.craft.utils.UIUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.bvv_login)
    BackgroundVideoView bvvLogin;
    @BindView(R.id.toolbar_reg)
    Toolbar toolbarLogin;
    @BindView(R.id.iv_reg)
    ImageView ivLogin;
    @BindView(R.id.et_login_phone)
    EditText etLoginPhone;
    @BindView(R.id.et_login_password)
    EditText etLoginPassword;
    @BindView(R.id.tv_login_86)
    TextView tvLogin86;
    @BindView(R.id.tv_login_change)
    TextView tvLoginChange;

    int loginMethod = 1;                // 1 - 手机密码登录 2 - 用户名密码登录
    String account;                     // 用户名或手机号
    String password;                    // 密码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏
        initView();
    }

    private void initView() {
        // 配置圆角logo
        ivLogin = findViewById(R.id.iv_reg);
        Glide.with(this)
                .load(R.mipmap.logo)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(UIUtils.dip2px(30))))//圆角半径
                .into(ivLogin);
        // 配置背景视频
        initVideo();

        //配置ToolBar
        toolbarLogin = findViewById(R.id.toolbar_reg);
        toolbarLogin.setNavigationOnClickListener(v -> {
            finish();
        });
    }

    private void initVideo() {
        bvvLogin.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.background_login));
        bvvLogin.start();
        bvvLogin.setOnCompletionListener((mp) -> {
            bvvLogin.start();
        });
    }

    //返回重启加载
    @Override
    protected void onRestart() {
        initVideo();
        super.onRestart();
    }

    //防止锁屏或者切出的时候，音乐在播放
    @Override
    protected void onStop() {
        bvvLogin.stopPlayback();
        super.onStop();
    }

    //忘记密码
    public void forgotPassword(View view) {
        Toast.makeText(this, "那只能怪你蠢了", Toast.LENGTH_SHORT).show();
    }

    //点击注册
    public void register(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    public void login(View view) {
        account = etLoginPhone.getText().toString();
        password = etLoginPassword.getText().toString();
        if (account.equals("") || password.equals("")) {
            Toast.makeText(this, ((loginMethod == 0) ? "手机号" : "用户名") + "和密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        HttpParams params = new HttpParams();
        if (loginMethod == LoginUtils.PHONE){
            params.put("phone",account);
        }else {
            params.put("email",account);
        }
        OkGo.<String>post(NetConfig.USER_LOGIN)
                .params(params)
                .params("password", password)
                .params("type",loginMethod)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        switch (NetConfig.getCode(response.body())) {
                            case 200:
                                Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                                Log.e("TAG", response.body());
                                JSONObject data = JSON.parseObject(response.body()).getJSONObject("data").getJSONObject("data");
                                User user = data.toJavaObject(User.class);
                                UserTemp.isLogin = true;
                                UserTemp.user = user;
                                LoginUtils.saveLogin(account, password,loginMethod);
                                finish();
                                break;
                            case 404:
                                Toast.makeText(LoginActivity.this, "用户名或密码错误！", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
    }

    public void changeMethod(View view) {
        switch (loginMethod) {
            case 0:
                //说明是手机密码登录方式
                tvLogin86.setVisibility(View.GONE);
                etLoginPhone.setHint("请输入注册邮箱");
                etLoginPhone.setInputType(InputType.TYPE_CLASS_TEXT);
                tvLoginChange.setText("手机号登录");
                loginMethod = 1;
                break;
            case 1:
                tvLogin86.setVisibility(View.VISIBLE);
                etLoginPhone.setHint("请输入手机号");
                etLoginPhone.setInputType(InputType.TYPE_CLASS_PHONE);
                tvLoginChange.setText("账户密码登录");
                loginMethod = 2;
                break;
        }
    }
}
