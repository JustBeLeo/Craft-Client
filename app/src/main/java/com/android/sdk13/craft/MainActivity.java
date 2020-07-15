package com.android.sdk13.craft;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.sdk13.craft.article.ArticleFragment;
import com.android.sdk13.craft.communication.CommunicationFragment;
import com.android.sdk13.craft.entity.User;
import com.android.sdk13.craft.game.GameActivity;
import com.android.sdk13.craft.home.HomeFragment;
import com.android.sdk13.craft.profile.FocusActivity;
import com.android.sdk13.craft.publish.PublishActivity_Activity;
import com.android.sdk13.craft.publish.PublishArticleActivity;
import com.android.sdk13.craft.publish.PublishVideoActivity;
import com.android.sdk13.craft.learn.LearnFragment;
import com.android.sdk13.craft.login_reg.LoginActivity;
import com.android.sdk13.craft.profile.IndexActivity;
import com.android.sdk13.craft.entity.UserTemp;
import com.android.sdk13.craft.utils.MyApplication;
import com.android.sdk13.craft.utils.NetConfig;
import com.android.sdk13.craft.utils.PermissionUtils;
import com.android.sdk13.craft.utils.LoginUtils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private PermissionUtils permissionUtils = null;
    private static List<String> sNeedPermissions = new ArrayList<>();

    //静态块中初始化所需要的权限
    static {
        sNeedPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        sNeedPermissions.add(Manifest.permission.ACCESS_NETWORK_STATE);
        sNeedPermissions.add(Manifest.permission.INTERNET);
        sNeedPermissions.add(Manifest.permission.READ_PHONE_STATE);
        sNeedPermissions.add(Manifest.permission.READ_SYNC_SETTINGS);
        sNeedPermissions.add(Manifest.permission.ACCESS_WIFI_STATE);
        sNeedPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        sNeedPermissions.add(Manifest.permission.CAMERA);
        sNeedPermissions.add(Manifest.permission.CHANGE_WIFI_STATE);
    }

    // View对象
    @BindView(R.id.fl_main)
    FrameLayout flMain;
    @BindView(R.id.nav_view)
    BottomNavigationView navView;
    @BindView(R.id.dl_main)
    DrawerLayout dlMain;
    @BindView(R.id.nav_view_left)
    NavigationView navigationView;
    @BindView(R.id.cl_main)
    ConstraintLayout clMain;
    @BindView(R.id.toolbar_main)
    Toolbar toolbarMain;
    // 抽屉里的
    View header;
    ImageView ivAvatar;
    TextView tvUsername;
    TextView tvSignature;
    TextView tvPost;
    TextView tvFocus;
    TextView tvFocused;
    LinearLayout llPost;
    LinearLayout llFocus;
    LinearLayout llFocused;

    // 页面对象
    HomeFragment homeFragment;
    ArticleFragment entryFragment;
    LearnFragment learnFragment;
    CommunicationFragment communicationFragment;

    //工具对象
    FragmentManager fm;
    FragmentTransaction ft;

    //状态判断
    int position = 1;                     // 指定显示的fragment
    boolean isExited = false;             // 主页面点击两下退出

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExited = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        navView.setOnNavigationItemSelectedListener(this);
        initPermission();
        initToolbar();
        initFragment();
        initDrawer();
    }

    /*
     * @Description 初始化权限
     * @Date 17:23 2020/6/26
     **/
    private void initPermission() {
        permissionUtils = new PermissionUtils(this);
        permissionUtils.request(sNeedPermissions, 100, new PermissionUtils.CallBack() {
            @Override
            public void grantAll() {
            }

            @Override
            public void denied() {
                fileList();
            }
        });
    }

    /*
     * @Description 请求权限的回调函数
     * @Date 14:01 2020/6/25
     **/
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /*
     * @Description 初始化滑动侧边栏，所有按钮的点击事件
     * 以及进入用户主页（需要用户数据）
     * @Date 14:01 2020/6/25
     **/
    private void initDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, dlMain, toolbarMain, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        dlMain.addDrawerListener(toggle);
        toggle.syncState();
        dlMain.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                //设置主布局随菜单滑动而滑动
                int drawerViewWidth = drawerView.getWidth();
                clMain.setTranslationX(drawerViewWidth * slideOffset);
                Log.e("TAG", "onDrawerSlide: 测试GitHub提交情况" );
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        // 通过inflate 新建一个view 找到组件添加监听
        if (header == null) {
            header = View.inflate(this, R.layout.nav_header_drawer, null);
            navigationView.addHeaderView(header);
        }
        getUserInfo();
        // 设置头像点击事件
    }

    /*
     * @Description 每一次重新启动都尝试获取用户信息
     * @Date 14:54 2020/6/31
     **/
    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void getUserInfo() {

        tvSignature = header.findViewById(R.id.tv_drawer_signature);
        ivAvatar = header.findViewById(R.id.iv_drawer_avatar);
        tvUsername = header.findViewById(R.id.tv_drawer_username);
        tvPost = findViewById(R.id.tv_drawer_post);
        tvFocus = findViewById(R.id.tv_drawer_focus);
        tvFocused = findViewById(R.id.tv_drawer_focused);

        llFocus = findViewById(R.id.ll_drawer_focus);
        llPost = findViewById(R.id.ll_drawer_post);
        llFocused = findViewById(R.id.ll_drawer_focused);

        llFocus.setOnClickListener(view -> {
            Intent intent = new Intent(this, FocusActivity.class);
            intent.putExtra("type",1);
            startActivity(intent);
        });

        llFocused.setOnClickListener(view -> {
            Intent intent = new Intent(this, FocusActivity.class);
            intent.putExtra("type",2);
            startActivity(intent);
        });

        if (!UserTemp.isLogin) {
            tvSignature.setText(" ");
            tvFocus.setText("0");
            tvFocused.setText("0");
            tvPost.setText("0");
            ivAvatar.setOnClickListener(v -> {
                Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            });
            tvUsername.setText("请先登录");
            tvSignature.setVisibility(View.GONE);
            return;
        }

        User user = UserTemp.user;
        tvFocus.setText(String.format("%d", user.getFocus_count()));
        tvFocused.setText(user.getFocused_count() + "");
        tvPost.setText(user.getFavor_count() + "");

        ivAvatar.setOnClickListener(v -> {
            Intent intent;
            intent = new Intent(this, IndexActivity.class);
            intent.putExtra("id", UserTemp.user.getId());
            startActivity(intent);
        });

        // 如果有头像就加载
        Glide.with(this)
                .load(NetConfig.URL + user.getAvatar_url())
                .placeholder(R.mipmap.avatar_non_login)
                .circleCrop()
                .into(ivAvatar);
        tvUsername.setText(user.getUsername());
        if (user.getMotto() != null)
            tvSignature.setText(user.getMotto());
        else
            tvSignature.setText("快去设置你的个性签名吧！");

    }

    /*
     * @Description 顶部状态栏导航按钮点击事件
     * @Date 14:02 2020/6/25
     **/
    private void initToolbar() {
        setSupportActionBar(toolbarMain);
        MyApplication.toolbar = toolbarMain;
        toolbarMain.setNavigationOnClickListener(v -> {
            if (dlMain.isDrawerOpen(GravityCompat.START)) {
                dlMain.closeDrawer(GravityCompat.START);
            } else {
                dlMain.openDrawer(GravityCompat.START);
            }
        });
    }

    /*
     * @Description 初始化各个Fragment页面,并开启事务装载进入FrameLayout中
     * @Date 14:03 2020/6/25
     **/
    private void initFragment() {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        if (homeFragment == null){
            homeFragment = new HomeFragment();
            ft.add(R.id.fl_main, homeFragment);
        }
        if (entryFragment == null){
            entryFragment = new ArticleFragment();
            ft.add(R.id.fl_main, entryFragment);
        }
        if (learnFragment == null){
            learnFragment = new LearnFragment();
            ft.add(R.id.fl_main, learnFragment);
        }
        if (communicationFragment == null){
            communicationFragment = new CommunicationFragment();
            ft.add(R.id.fl_main, communicationFragment);
        }
        ft.show(homeFragment);
        ft.hide(entryFragment);
        ft.hide(learnFragment);
        ft.hide(communicationFragment);
        ft.commit();
    }

    /*
     * @Description 监听底部导航栏按钮点击事件
     * @Date 14:12 2020/6/25
     **/
    public boolean onNavigationItemSelected(MenuItem item) {
        // 配置导航栏点击事件
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            position = 1;
        } else if (id == R.id.nav_show) {
            position = 2;
        } else if (id == R.id.nav_learn) {
            position = 3;
        } else if (id == R.id.nav_chat) {
            if (UserTemp.isLogin) {
                position = 4;
            } else {
                Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
                return false;
            }
        }
        switchFragment();
        return true;
    }

    /*
     * @Description 用于各个Fragment的切换
     * @Date 14:12 2020/6/25
     **/
    private void switchFragment() {
        ft = fm.beginTransaction();
        switch (position) {
            case 1:
                ft.show(homeFragment);
                ft.hide(entryFragment);
                ft.hide(learnFragment);
                ft.hide(communicationFragment);
                break;
            case 2:
                ft.show(entryFragment);
                ft.hide(homeFragment);
                ft.hide(learnFragment);
                ft.hide(communicationFragment);
                break;
            case 3:
                ft.show(learnFragment);
                ft.hide(entryFragment);
                ft.hide(homeFragment);
                ft.hide(communicationFragment);
                break;
            case 4:
                ft.show(communicationFragment);
                ft.hide(entryFragment);
                ft.hide(homeFragment);
                ft.hide(learnFragment);
                break;
        }
        ft.commit();
    }

    public void uploadVideo(View view) {
        startActivity(new Intent(this, PublishVideoActivity.class));
    }

    public void game(View view) {
        startActivity(new Intent(this, GameActivity.class));
    }

    public void uploadArticle(View view) {
        Intent intent = new Intent(MainActivity.this, PublishArticleActivity.class);
        startActivity(intent);
    }

    public void activity(View view) {
        Intent intent = new Intent(MainActivity.this, PublishActivity_Activity.class);
        startActivity(intent);
    }

    /*
     * @Description 监听返回键的回调方法
     * @Date 14:17 2020/6/25
     **/
    public void onBackPressed() {
        if (dlMain.isDrawerOpen(GravityCompat.START)) {
            dlMain.closeDrawer(GravityCompat.START);
        } else {
            if (!isExited) {
                isExited = true;
                Toast.makeText(this, "请再按一下返回键退出", Toast.LENGTH_SHORT).show();
                handler.sendEmptyMessageDelayed(0, 2000);
            } else {
                finish();
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initDrawer();
        getUserInfo();
    }

    public void exit(View view) {
        new AlertDialog.Builder(this)
                .setMessage("您真的要退出吗？")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", (dialog, which) -> {
                    LoginUtils.logout();
                    initDrawer();
                })
                .show();
    }


}
