package com.android.sdk13.craft.profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.sdk13.craft.R;
import com.android.sdk13.craft.base.MyPagerAdapter;
import com.android.sdk13.craft.entity.User;
import com.android.sdk13.craft.entity.UserTemp;
import com.android.sdk13.craft.utils.NetConfig;
import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
 * @Author sdk13
 * @Description 个人主页的Activity
 * @Date 14:17 2020/6/25
 **/
public class IndexActivity extends AppCompatActivity {

    @BindView(R.id.iv_index_avatar)
    ImageView ivAvatar;
    @BindView(R.id.toolbar_index)
    Toolbar toolbar;
    @BindView(R.id.ctl_index)
    CollapsingToolbarLayout ctl_index;
    @BindView(R.id.iv_index_gender)
    ImageView ivGender;                 // 性别
    @BindView(R.id.tv_index_focus)
    TextView tvFocus;                   // 关注数
    @BindView(R.id.tv_index_focused)
    TextView tvFocused;                 // 被关注数
    @BindView(R.id.tv_index_username)
    TextView tvUsername;                // 用户名
    @BindView(R.id.tv_index_region)
    TextView tvRegion;                  // 地区
    @BindView(R.id.tv_index_mtto)
    TextView tvMtto;                    // 个性签名


    @BindView(R.id.tl_index)
    TabLayout tlIndex;
    @BindView(R.id.vp_index)
    ViewPager vpIndex;

    FragmentManager fm;

    ArrayList<Fragment> pagers = new ArrayList<>();
    ArrayList<String> titles = new ArrayList<>();
    User user;
    Long id;

    MineVideoPager mineVideoPager;
    ArticlePager articlePager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        id = getIntent().getLongExtra("id", 1);
        getUserData();
        initToolbar();
        initPager();
        initViewPager();
        initTabLayout();

    }

    private void initToolbar() {
        ctl_index.setExpandedTitleColor(getColor(R.color.transparent));
        ctl_index.setCollapsedTitleTextColor(Color.WHITE);
        ctl_index.setCollapsedTitleTextAppearance(R.style.toolBarText_title);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void initPager() {
        mineVideoPager = new MineVideoPager();
        articlePager = new ArticlePager();
        MineVideoPager mineVideoPager3 = new MineVideoPager();
        pagers.add(mineVideoPager);
        titles.add("视频");
        pagers.add(articlePager);
        titles.add("文章");
        pagers.add(mineVideoPager3);
        titles.add("收藏");
    }

    private void initViewPager() {
        fm = getSupportFragmentManager();
        vpIndex.setAdapter(new MyPagerAdapter(fm, pagers, titles));
    }

    private void initTabLayout() {
        tlIndex.setupWithViewPager(vpIndex);
    }

    public void edit(View view) {
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onRestart() {
        getUserData();
        super.onRestart();
    }

    private void getUserData() {
        OkGo.<String>get(NetConfig.GET_USER_INFO)
                .params("id", UserTemp.user.getId())
                .execute(new StringCallback() {
                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onSuccess(Response<String> response) {
                        JSONObject data = JSON.parseObject(response.body()).getJSONObject("data").getJSONObject("user");
                        UserTemp.user = data.toJavaObject(User.class);
                        user = UserTemp.user;
                        // 加载性别图标
                        int gender;    // 1男0女
                        Boolean g = user.getGender();
                        if (g != null) {
                            if (user.getGender()) {
                                gender = R.drawable.ic_male;
                            } else {
                                gender = R.drawable.ic_female;
                            }
                            Glide.with(IndexActivity.this)
                                    .load(gender)
                                    .into(ivGender);
                        }
                        // 加载头像
                        Glide.with(IndexActivity.this)
                                .load(NetConfig.URL + user.getAvatar_url())
                                .placeholder(R.mipmap.avatar_non_login)
                                .circleCrop()
                                .into(ivAvatar);
                        ctl_index.setTitle(user.getUsername());
                        // 加载普通TextView
                        tvUsername.setText(user.getUsername());
                        tvFocus.setText(String.format("%d", user.getFocus_count()));
                        tvFocused.setText(String.format("%d", user.getFocused_count()));
                        if (user.getMotto() != null) {
                            tvMtto.setText(user.getMotto());
                        } else {
                            tvMtto.setText("还没有哦");
                        }
                    }
                });

    }
}
