package com.android.sdk13.craft.login_reg;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.sdk13.craft.R;
import com.android.sdk13.craft.utils.NetConfig;
import com.android.sdk13.craft.utils.StringUtils;
import com.android.sdk13.craft.utils.UIUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
 * @Author sdk13
 * @Description 用于注册的Activity
 * @Date 14:22 2020/6/24
 **/
public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_reg)
    Toolbar toolbarReg;
    @BindView(R.id.iv_reg)
    ImageView ivReg;
    @BindView(R.id.et_reg_phone)
    EditText etRegPhone;
    @BindView(R.id.et_reg_email)
    EditText etRegEmail;
    @BindView(R.id.et_reg_username)
    EditText etRegUsername;
    @BindView(R.id.et_reg_password)
    EditText etRegPassword;
    @BindView(R.id.checkbox_reg)
    CheckBox checkboxReg;

    String phone;
    String email;
    String username;
    String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏
        initView();
    }

    private void initView() {
        // 配置圆角logo
        ivReg = findViewById(R.id.iv_reg);
        Glide.with(this)
                .load(R.mipmap.logo)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(UIUtils.dip2px(30))))//圆角半径
                .into(ivReg);

        //配置ToolBar
        toolbarReg = findViewById(R.id.toolbar_reg);
        toolbarReg.setNavigationOnClickListener(v -> {
            finish();
        });
    }

    public void register(View view) {
        phone = etRegPhone.getText().toString();
        email = etRegEmail.getText().toString();
        username = etRegUsername.getText().toString();
        password = etRegPassword.getText().toString();

        // 判断输入是否正确
        if (StringUtils.isBlank(phone)) {
            Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isBlank(email)) {
            Toast.makeText(this, "邮箱不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isBlank(username)) {
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isBlank(password)) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!checkboxReg.isChecked()) {
            Toast.makeText(this, "请同意用户手册", Toast.LENGTH_SHORT).show();
            return;
        }

        OkGo.<String>post(NetConfig.USER_REG)
                .params("username", username)
                .params("email", email)
                .params("phone", phone)
                .params("password", password)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        int responseCode = NetConfig.getCode(response.body());
                        Log.e("TAG", "onSuccess: " + responseCode);
                        switch (responseCode) {
                            case 200:
                                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                finish();
                                break;
                            case 1:
                                Toast.makeText(RegisterActivity.this, "该手机号已被注册", Toast.LENGTH_SHORT).show();
                                etRegPassword.setText("");
                                etRegPhone.setText("");
                                break;
                            case 2:
                                Toast.makeText(RegisterActivity.this, "该邮箱已被注册", Toast.LENGTH_SHORT).show();
                                etRegPassword.setText("");
                                etRegUsername.setText("");
                                break;
                        }
                    }
                });

    }
}
