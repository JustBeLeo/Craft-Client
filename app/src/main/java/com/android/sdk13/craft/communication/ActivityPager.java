package com.android.sdk13.craft.communication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.sdk13.craft.R;
import com.android.sdk13.craft.base.BaseFragment;
import com.android.sdk13.craft.custom.OnLoadMoreListener;
import com.android.sdk13.craft.entity.Activity;
import com.android.sdk13.craft.utils.NetConfig;
import com.android.sdk13.craft.utils.UIUtils;
import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ActivityPager extends BaseFragment {

    @BindView(R.id.rv_pager_common)
    RecyclerView rv;
    @BindView(R.id.msv_common)
    SwipeRefreshLayout msvCommon;

    List<Activity> list;
    ActivityAdapter adapter;

    int currentPage = 1;
    int pageCount = 1;
    int pageSize = 8;

    @Override
    protected void initData() {
        OkGo.<String>get(NetConfig.ACTIVITY_LIST)
                .params("pageNum", currentPage)
                .params("pageSize", pageSize)
                .params("sort", "")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (response.code() == 200) {
                            JSONObject object = JSONObject.parseObject(response.body());
                            JSONArray array = object.getJSONArray("records");
                            list = JSONArray.parseArray(array.toJSONString(), Activity.class);
                            currentPage = object.getInteger("currentPage");
                            pageCount = object.getInteger("pageCount");
                            initRecycleView();
                        }
                    }
                });
    }

    private void initRecycleView() {
        adapter = new ActivityAdapter(list, getContext(), rv);
        adapter.setmOnClick(i -> {
            Activity activity = list.get(i);
            Intent intent = new Intent(getContext(), OffLineActivity.class);
            intent.putExtra("activity_id", activity.getId());
            startActivity(intent);
        });
        rv.setScrollIndicators(View.SCROLL_INDICATOR_END);
        // 新建一个滑动监听
        OnLoadMoreListener loadMoreListener = new OnLoadMoreListener() {
            @Override
            protected void onLoading(int countItem, int lastItem) {
                new Handler().post(() -> getMoreData());
            }
        };
        rv.addOnScrollListener(loadMoreListener);
        UIUtils.setVerticalRecycleView(getContext(), rv, adapter);
    }

    private void getMoreData() {
        if (currentPage < pageCount){
            OkGo.<String>get(NetConfig.ACTIVITY_LIST)
                    .params("pageNum", ++currentPage)
                    .params("pageSize", pageSize)
                    .params("sort", "")
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            if (response.code() == 200) {
                                JSONObject object = JSONObject.parseObject(response.body());
                                JSONArray array = object.getJSONArray("records");
                                list = JSONArray.parseArray(array.toJSONString(), Activity.class);
                                currentPage = object.getInteger("currentPage");
                                pageCount = object.getInteger("pageCount");
                                adapter.notifyDataSetChanged();
                                Toast.makeText(getContext(),"加载成功",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else {
            Toast.makeText(getContext(),"没有更多数据了",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void initView() {
        // 加载刷新
        msvCommon.setOnRefreshListener(() -> new Handler().post(() -> {
            currentPage = 1;
            initData();
            Toast.makeText(getContext(), "刷新成功!", Toast.LENGTH_SHORT).show();
            msvCommon.setRefreshing(false);
        }));
    }


    @Override
    protected int getLayoutID() {
        return R.layout.pager_common;
    }
}
