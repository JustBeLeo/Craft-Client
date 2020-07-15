package com.android.sdk13.craft.learn;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.sdk13.craft.R;
import com.android.sdk13.craft.base.BaseFragment;
import com.android.sdk13.craft.base.MyPagerAdapter;
import com.android.sdk13.craft.search.SearchActivity;
import com.android.sdk13.craft.utils.MyApplication;

import java.util.ArrayList;

import butterknife.BindView;

public class LearnFragment extends BaseFragment {

    @BindView(R.id.tl_learn)
    TabLayout tl;
    @BindView( R.id.vp_learn)
    ViewPager vp;

    ArrayList<Fragment> fragmentList = new ArrayList<>();;
    ArrayList<String> titleList = new ArrayList<>();;
    TutorialPager tutorialPager;
    VideoPager videoPager;

    @Override
    protected void initData() {
        titleList.add( "教程" );
        titleList.add( "探索" );
    }

    @Override
    protected void initView() {
        MyApplication.toolbar.setTitle( "天工造物" );
        setHasOptionsMenu( true );
        initPager();
        initViewPager();
        initTabLayout();
    }
    // 初始化分页
    private void initPager() {
        tutorialPager = new TutorialPager();
        videoPager = new VideoPager();
        fragmentList.add( tutorialPager );
        fragmentList.add(videoPager);
    }

    //初始化ViewPager
    private void initViewPager() {
        MyPagerAdapter adapter = new MyPagerAdapter( getChildFragmentManager(),fragmentList,titleList );
        vp.setAdapter( adapter );
    }

    private void initTabLayout() {
        tl.setupWithViewPager( vp );
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_learn;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged( hidden );
        if(!hidden){
            MyApplication.toolbar.setTitle( "天工造物" );
        }
    }

    @Override   //添加查询按钮
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate( R.menu.search_menu, menu );
    }

    @Override   //查询按钮监听
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent( getContext(), SearchActivity.class );
        startActivity( intent );
        return true;
    }
}
