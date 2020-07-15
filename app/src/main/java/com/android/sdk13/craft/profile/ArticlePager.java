package com.android.sdk13.craft.profile;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.sdk13.craft.R;
import com.android.sdk13.craft.article.ArticleActivity;
import com.android.sdk13.craft.article.adapter.ArticleAdapter;
import com.android.sdk13.craft.base.BaseFragment;
import com.android.sdk13.craft.entity.Article;
import com.android.sdk13.craft.entity.UserTemp;
import com.android.sdk13.craft.utils.NetConfig;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ArticlePager extends BaseFragment {

    @BindView( R.id.rv_pager_common )
    RecyclerView rvPost;

    int currentPage = 1;
    int pageCount = 1;
    int pageSize = 8;

    List<Article> list = new ArrayList<>();

    @Override
    protected void initData() {
        OkGo.<String>get(NetConfig.ARTICLE_USER_LIST)
                .params("user_id", UserTemp.user.getId())
                .params("pageNum", currentPage)
                .params("pageSize", pageSize)
                .params("sort", "")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        JSONObject object = JSONObject.parseObject(response.body());
                        JSONArray array = object.getJSONArray("records");
                        list = JSONArray.parseArray(array.toJSONString(), Article.class);
                        currentPage = object.getInteger("currentPage");
                        pageCount = object.getInteger("pageCount");
                        initRecycleView();
                    }
                });
    }

    private void initRecycleView() {
        ArticleAdapter adapter = new ArticleAdapter( list,getContext(),rvPost );
        rvPost.setAdapter( adapter );
        rvPost.setLayoutManager( new LinearLayoutManager( getContext(),LinearLayoutManager.VERTICAL,false ) );
        adapter.setOnClickListener(i -> {
            startActivity(new Intent(getContext(), ArticleActivity.class).putExtra("article_id", list.get(i).getId()));
        });
    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutID() {
        return R.layout.pager_common;
    }
}
