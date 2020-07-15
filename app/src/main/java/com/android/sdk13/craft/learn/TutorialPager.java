package com.android.sdk13.craft.learn;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.sdk13.craft.R;
import com.android.sdk13.craft.base.BaseFragment;
import com.android.sdk13.craft.entity.Course;
import com.android.sdk13.craft.custom.OnLoadMoreListener;
import com.android.sdk13.craft.independent_activity.TutorialActivity;
import com.android.sdk13.craft.learn.adapter.TutorRecycleViewAdapter;
import com.android.sdk13.craft.utils.UIUtils;

import java.util.ArrayList;

import butterknife.BindView;

public class TutorialPager extends BaseFragment {

    @BindView(R.id.rv_pager_common)
    RecyclerView rvCommon;

    TutorRecycleViewAdapter adapter;

    ArrayList<Course> list;

    @Override
    protected void initData() {
        list = new ArrayList<>();
        list.add(new Course());
        list.add(new Course());
        list.add(new Course());
        list.add(new Course());
    }

    void getMoreData(){
        list.add( new Course() );
        list.add( new Course() );
        list.add( new Course() );
    }

    @Override
    protected void initView() {
        initRecycleView();
    }

    private void initRecycleView() {
        adapter = new TutorRecycleViewAdapter( list,getContext(),rvCommon );
        adapter.setmOnClickListener( i -> {
            Intent intent = new Intent( getContext(), TutorialActivity.class );
            startActivity( intent );
        } );
        UIUtils.setVerticalRecycleView( getContext(),rvCommon, adapter );

        rvCommon.addOnScrollListener( new OnLoadMoreListener() {
            @Override
            protected void onLoading(int countItem, int lastItem) {
                new Handler().postDelayed( ()->{
                    getMoreData();
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getContext(),"更新成功",Toast.LENGTH_SHORT).show();
                    },1000 );
            }
        } );
    }

    @Override
    protected int getLayoutID() {
        return R.layout.pager_common;
    }
}
