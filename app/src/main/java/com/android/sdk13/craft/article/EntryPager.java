package com.android.sdk13.craft.article;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.sdk13.craft.R;
import com.android.sdk13.craft.article.adapter.ArticleAdapter;
import com.android.sdk13.craft.base.BaseFragment;
import com.android.sdk13.craft.entity.Article;
import com.android.sdk13.craft.entity.Entry;
import com.android.sdk13.craft.custom.OnLoadMoreListener;

import java.util.ArrayList;

import butterknife.BindView;

@SuppressLint("ValidFragment")
public class EntryPager extends BaseFragment {

    @BindView(R.id.rv_pager_common)
    RecyclerView rvNew;
    FloatingActionButton fab = EntryTemp.fab;

    ArticleAdapter adapter;

    int pageType = Entry.TYPE_NEW;

    ArrayList<Article> list;     // 词条数组


    public EntryPager(int Type) {
        pageType = Type;
    }

    @Override   // 根据类型初始化数据
    protected void initData() {
        switch (pageType) {
            case Entry.TYPE_FOLLOW:
                break;
            case Entry.TYPE_NEW:
                break;
            case Entry.TYPE_RECOMMEND:
                break;
        }

        list = new ArrayList<>();

    }

    // 获取更多数据
    protected void getMoreData() {

        adapter.notifyDataSetChanged();
        Toast.makeText( getContext(), "更新成功", Toast.LENGTH_SHORT ).show();
    }

    @Override
    protected void initView() {
        initRecycleView();
    }

    private void initRecycleView() {
        adapter = new ArticleAdapter( list, getContext(), rvNew );
        adapter.setOnClickListener( position -> {
            startActivity( new Intent( getContext(), ArticleActivity.class ) );
        } );
        rvNew.setAdapter( adapter );
        rvNew.setLayoutManager( new LinearLayoutManager( getContext(), LinearLayoutManager.VERTICAL, false ) );
        rvNew.addItemDecoration( new DividerItemDecoration( getContext(), DividerItemDecoration.VERTICAL ) );
        // 新建一个滑动监听
        OnLoadMoreListener loadMoreListener = new OnLoadMoreListener() {
            @Override
            protected void onLoading(int countItem, int lastItem) {
                new Handler().postDelayed( () -> {
                    getMoreData();
                }, 2000 );
            }
        };
        rvNew.addOnScrollListener( loadMoreListener );
    }

    @Override
    protected int getLayoutID() {
        return R.layout.pager_common;
    }
}
