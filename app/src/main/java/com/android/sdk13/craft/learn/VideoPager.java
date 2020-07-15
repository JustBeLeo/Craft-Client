package com.android.sdk13.craft.learn;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.sdk13.craft.R;
import com.android.sdk13.craft.base.BaseFragment;
import com.android.sdk13.craft.entity.Video;
import com.android.sdk13.craft.custom.OnLoadMoreListener;
import com.android.sdk13.craft.learn.adapter.VideoAdapter;
import com.android.sdk13.craft.entity.UserTemp;
import com.android.sdk13.craft.utils.NetConfig;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.List;

import butterknife.BindView;
import cn.jzvd.JzvdStd;

public class VideoPager extends BaseFragment {
    @BindView(R.id.rv_pager_common)
    RecyclerView rvCommon;

    @BindView(R.id.msv_common)
    SwipeRefreshLayout srlExplore;

    VideoAdapter adapter;

    List<Video> list;


    int currentPage = 1;
    int pageCount = 1;
    int pageSize = 8;

    @Override
    protected void initData() {

        OkGo.<String>get(NetConfig.VIDEO_LIST)
                .params("sort", "")
                .params("pageNum", currentPage)
                .params("pageSize", pageSize)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        JSONObject object = JSONObject.parseObject(response.body());
                        JSONArray array = object.getJSONArray("records");
                        list = JSONArray.parseArray(array.toJSONString(), Video.class);
                        currentPage = object.getInteger("currentPage");
                        pageCount = object.getInteger("pageCount");
                        initRecycleView();
                    }
                });
    }

    void getMoreData() {
        if (currentPage < pageCount) {
            OkGo.<String>get(NetConfig.ARTICLE_LIST)
                    .params("pageNum", ++currentPage)
                    .params("pageSize", pageSize)
                    .params("sort", "")
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            JSONObject object = JSONObject.parseObject(response.body());
                            JSONArray array = object.getJSONArray("records");
                            list.addAll(JSONArray.parseArray(array.toJSONString(), Video.class));
                            currentPage = object.getInteger("currentPage");
                            pageCount = object.getInteger("pageCount");
                            adapter.notifyDataSetChanged();
                            Toast.makeText(getContext(), "更新成功", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(getContext(), "没有更多数据啦", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void initView() {
        srlExplore.setOnRefreshListener(() -> new Handler().post(() -> {
            currentPage = 1;
            initData();
            Toast.makeText(getContext(), "刷新成功!", Toast.LENGTH_SHORT).show();
            srlExplore.setRefreshing(false);
        }));
    }

    private void initRecycleView() {
        adapter = new VideoAdapter(list, getContext(), rvCommon);
        adapter.setOnClickListener(i -> {
            Video video = list.get(i);
            OkGo.<String>get(NetConfig.VIDEO_DETAIL)
                    .params("Video_id",video.getId())
                    .params("user_id", UserTemp.user.getId())
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            JzvdStd.startFullscreenDirectly(getContext(), JzvdStd.class, NetConfig.URL + video.getVideo_url(), video.getName());
                        }
                    });
        });
        rvCommon.setAdapter(adapter);
        rvCommon.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvCommon.addOnScrollListener(new OnLoadMoreListener() {
            @Override
            protected void onLoading(int countItem, int lastItem) {
                new Handler().post(() -> getMoreData());
            }
        });

    }

    @Override
    protected int getLayoutID() {
        return R.layout.pager_common;
    }
}
