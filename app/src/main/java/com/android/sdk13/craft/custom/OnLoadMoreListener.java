package com.android.sdk13.craft.custom;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_SETTLING;

public abstract class OnLoadMoreListener extends RecyclerView.OnScrollListener {
    private int countItem;
    private int lastItem;
    private boolean isScrolled = false;//是否可以滑动
    private RecyclerView.LayoutManager layoutManager;

    private static final int THRESHOLD = 10;//滑动的距离
    private int distance = 0;
    private boolean visible = true;//是否可见

    protected abstract void onLoading(int countItem, int lastItem);

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        //拖拽或者惯性滑动时isScolled设置为true
        isScrolled = newState == SCROLL_STATE_DRAGGING || newState == SCROLL_STATE_SETTLING;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            layoutManager = recyclerView.getLayoutManager();
            countItem = layoutManager.getItemCount();
            lastItem = ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
        }

        if (isScrolled && countItem != lastItem && lastItem == countItem - 1) {
            onLoading(countItem, lastItem);
        }

        if (distance > THRESHOLD && visible) {
            //隐藏
            visible = false;
            //onFABHide();
            distance = 0;
        } else if (distance < -20 && !visible) {
            //显示
            visible = true;
            //onFABShow();
            distance = 0;
        }
        if (visible && dy > 0 || (!visible && dy < 0)) {
            distance += dy;
        }
    }
}

