package com.android.sdk13.craft.communication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.android.sdk13.craft.R;
import com.android.sdk13.craft.base.GlideImageLoader;
import com.android.sdk13.craft.entity.Activity;
import com.android.sdk13.craft.entity.UserTemp;
import com.android.sdk13.craft.utils.NetConfig;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OffLineActivity extends AppCompatActivity {

    @BindView(R.id.tv_offline_title)
    TextView tvTitle;
    @BindView(R.id.banner_offline)
    Banner banner;
    @BindView(R.id.tv_offline)
    TextView tvOffline;
    @BindView(R.id.tv_offline_time)
    TextView tvTime;
    @BindView(R.id.tv_offline_site)
    TextView tvSite;
    @BindView(R.id.tv_offline_member_count)
    TextView tvMemberCount;
    @BindView(R.id.tv_offline_join)
    TextView tvJoin;

    Long activity_id;
    Activity activity;

    boolean isJoin = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);
        ButterKnife.bind(this);
        activity_id = getIntent().getLongExtra("activity_id", -1);
        initData();

    }

    private void initData() {
        // 加载文章数据
        OkGo.<String>get(NetConfig.ACTIVITY_DETAIL)
                .params("activity_id", activity_id)
                .params("user_id", UserTemp.user.getId())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (NetConfig.judgeCode(response)) {
                            activity = JSONObject
                                    .parseObject(response.body())
                                    .getJSONObject("data")
                                    .getObject("activity", Activity.class);
                            initView();
                            initJoin();
                        }
                    }
                });
    }

    public void initJoin() {
        OkGo.<String>get(NetConfig.ACTIVITY_IS_JOIN)
                .params("activity_id", activity_id)
                .params("user_id", UserTemp.user.getId())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (NetConfig.getCode(response.body()) == 200 ||
                                NetConfig.getCode(response.body()) == 404) {
                            isJoin = JSONObject
                                    .parseObject(response.body())
                                    .getJSONObject("data")
                                    .getBoolean("isJoin");
                            if (!isJoin) {
                                tvJoin.setText("立即参与");
                            } else {
                                tvJoin.setText("退出活动");
                            }
                        }
                    }
                });
    }

    @SuppressLint({"DefaultLocale", "SimpleDateFormat"})
    private void initView() {
        // 标题
        tvTitle.setText(activity.getName());

        // 初始化头图
        ArrayList<String> images = new ArrayList<>();
        images.add(NetConfig.URL + activity.getCover_url());
        banner.setImageLoader(new GlideImageLoader());
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        banner.setBannerAnimation(Transformer.ZoomOutSlide);
        banner.setImages(images);
        banner.setDelayTime(6000);
        banner.start();

        // 加载时间
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        tvTime.setText(String.format("%s 至 %s", sdf1.format(activity.getStart_time()), sdf1.format(activity.getEnd_time())));
        // 加载地点
        tvSite.setText(activity.getSite());
        // 加载参与人数
        tvMemberCount.setText(String.format("%d", activity.getMember_count()));
        // 加载活动内容
        tvOffline.setText(activity.getContent());


    }

    public void back(View view) {
        finish();
    }

    public void join(View view) {
        String url;
        if (isJoin) {
            url = NetConfig.ACTIVITY_EXIT;
        } else {
            url = NetConfig.ACTIVITY_JOIN;
        }
        OkGo.<String>get(url)
                .params("activity_id", activity_id)
                .params("user_id", UserTemp.user.getId())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (NetConfig.judgeCode(response)) {
                            initData();
                        }
                    }
                });
    }
}
