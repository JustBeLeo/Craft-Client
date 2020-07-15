package com.android.sdk13.craft.communication;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.android.sdk13.craft.R;
import com.android.sdk13.craft.base.BaseFragment;
import com.android.sdk13.craft.base.MyPagerAdapter;
import com.android.sdk13.craft.profile.MineVideoPager;
import com.android.sdk13.craft.utils.MyApplication;

import java.util.ArrayList;

import butterknife.BindView;

public class CommunicationFragment extends BaseFragment {

    @BindView(R.id.tl_comm)
    TabLayout tlComm;
    @BindView(R.id.vp_comm)
    ViewPager vpComm;

    InboxPager inboxPager;
    MineVideoPager mineVideoPager;
    ActivityPager activityPager;

    ArrayList<Fragment> pagers = new ArrayList<>();
    ArrayList<String> titles = new ArrayList<>();

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        MyApplication.toolbar.setTitle( "清茶淡话" );
        setHasOptionsMenu( true );
        initPager();
        initViewPager();
        initTabLayout();
    }
    /*
     * @Description 初始化交流分页
     * @Date 17:27 2020/6/26
     **/
    private void initPager() {
        inboxPager = new InboxPager();
        mineVideoPager = new MineVideoPager();
        activityPager = new ActivityPager();
        pagers.add( inboxPager );
        pagers.add(mineVideoPager);
        pagers.add(activityPager);
        titles.add( "私信" );
        titles.add( "动态" );
        titles.add( "线下活动" );
    }

    //初始化ViewPager
    private void initViewPager() {
        MyPagerAdapter adapter = new MyPagerAdapter( getChildFragmentManager(),pagers,titles );
        vpComm.setAdapter( adapter );
    }

    private void initTabLayout() {
        tlComm.setupWithViewPager( vpComm );
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged( hidden );
        if(!hidden){
            MyApplication.toolbar.setTitle( "清茶淡话" );
        }
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_communication;
    }

}
