package com.android.sdk13.craft.article;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.sdk13.craft.R;
import com.android.sdk13.craft.article.adapter.ArticleAdapter;
import com.android.sdk13.craft.base.BaseFragment;
import com.android.sdk13.craft.entity.Article;
import com.android.sdk13.craft.custom.OnLoadMoreListener;
import com.android.sdk13.craft.publish.PublishTutorialActivity;
import com.android.sdk13.craft.search.SearchActivity;
import com.android.sdk13.craft.utils.MyApplication;
import com.android.sdk13.craft.utils.NetConfig;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ArticleFragment extends BaseFragment {

    @BindView(R.id.rv_entry)
    RecyclerView rvEntry;
    @BindView(R.id.srl_entry)
    SwipeRefreshLayout srlEntry;

    ArticleAdapter adapter;
    List<Article> list;     // 文章数组

    int currentPage = 1;
    int pageCount = 1;
    int pageSize = 8;

    Unbinder unbinder;

    @Override
    protected void initData() {
        OkGo.<String>get(NetConfig.ARTICLE_LIST)
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

    /*
     * @Description 下拉加载更多数据
     * @Date 10:21 2020/6/30
     **/
    protected void getMoreData() {
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
                            list.addAll(JSONArray.parseArray(array.toJSONString(), Article.class));
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
        MyApplication.toolbar.setTitle("鬼斧神工");
        setHasOptionsMenu(true);
        srlEntry.setOnRefreshListener(() -> new Handler().post(() -> {
            currentPage = 1;
            initData();
            Toast.makeText(getContext(), "刷新成功!", Toast.LENGTH_SHORT).show();
            srlEntry.setRefreshing(false);
        }));
    }

    private void initRecycleView() {
        adapter = new ArticleAdapter(list, getContext(), rvEntry);
        adapter.setOnClickListener(position -> {
            startActivity(new Intent(getContext(), ArticleActivity.class).putExtra("article_id", list.get(position).getId()));
        });
        rvEntry.setAdapter(adapter);
        rvEntry.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvEntry.setScrollIndicators(View.SCROLL_INDICATOR_END);
        // 新建一个滑动监听
        OnLoadMoreListener loadMoreListener = new OnLoadMoreListener() {
            @Override
            protected void onLoading(int countItem, int lastItem) {
                new Handler().post(() -> getMoreData());
            }
        };
        rvEntry.addOnScrollListener(loadMoreListener);
    }

    @Override   //添加布局
    protected int getLayoutID() {
        return R.layout.fragment_entry;
    }

    @Override   //添加查询按钮
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.search_menu, menu);
    }

    @Override   //查询按钮监听
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(getContext(), SearchActivity.class);
        startActivity(intent);
        return true;
    }

    @Override   //切换隐藏状态的监听
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            MyApplication.toolbar.setTitle("鬼斧神工");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
