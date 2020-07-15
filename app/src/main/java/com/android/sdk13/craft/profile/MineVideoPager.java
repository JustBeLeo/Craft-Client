package com.android.sdk13.craft.profile;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.sdk13.craft.R;
import com.android.sdk13.craft.base.BaseFragment;
import com.android.sdk13.craft.entity.Video;
import com.android.sdk13.craft.learn.adapter.VideoAdapter;
import com.android.sdk13.craft.entity.UserTemp;
import com.android.sdk13.craft.utils.NetConfig;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.List;

import butterknife.BindView;
import cn.jzvd.JzvdStd;

public class MineVideoPager extends BaseFragment {

    @BindView( R.id.rv_pager_common )
    RecyclerView rv;

    List<Video> list;

    int currentPage = 1;
    int pageCount = 1;
    int pageSize = 8;
    VideoAdapter adapter;

    @Override
    protected void initData() {
        OkGo.<String>get(NetConfig.VIDEO_USER_LIST)
                .params("user_id",UserTemp.user.getId())
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

    private void initRecycleView() {
        adapter = new VideoAdapter(list, getContext(), rv);
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
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutID() {
        return R.layout.pager_common;
    }

}
