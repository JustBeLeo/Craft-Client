package com.android.sdk13.craft.independent_activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.sdk13.craft.R;
import com.android.sdk13.craft.entity.Step;
import com.android.sdk13.craft.profile.IndexActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jzvd.JzvdStd;

public class TutorialActivity extends AppCompatActivity {

    @BindView(R.id.jz_entry)
    JzvdStd jzEntry;
    @BindView(R.id.toolbar_entry)
    Toolbar toolbarEntry;
    @BindView(R.id.tv_entry_title)
    TextView tvEntryTitle;
    @BindView(R.id.rv_tutorial)
    RecyclerView rvTutorial;
    @BindView(R.id.iv_tutorial_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_tutorial_username)
    TextView tvUsername;

    ArrayList<Step> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_tutorial );
        ButterKnife.bind( this );
        initData();
        initToolbar();
        initRecycleView();
    }

    private void initData() {
        Step step1 = new Step( 1,"文火熬制",15,63,"http://129.211.26.221:8080/tiangong/picture/step1.jpeg" );
        Step step2 = new Step( 2,"用手指做一个空心的形状",23,42,"http://129.211.26.221:8080/tiangong/picture/step2.jpeg" );
        Step step3 = new Step( 3,"在麦芽糖顶端拉出长长的一根丝",18,30,"http://129.211.26.221:8080/tiangong/picture/step3.jpeg" );
        Step step4 = new Step( 4,"边往糖人里吹气边捏形状",5,2,"http://129.211.26.221:8080/tiangong/picture/step4.jpeg" );
        Step step5 = new Step( 5,"完成",6,2,"http://129.211.26.221:8080/tiangong/picture/step5.jpeg" );
        list = new ArrayList<>();
        list.add( step1 );
        list.add( step2 );
        list.add( step3 );
        list.add( step4 );
        list.add( step5 );
    }

    private void initRecycleView() {
        StepAdapter adapter = new StepAdapter( list,this,rvTutorial );
        rvTutorial.setAdapter( adapter );
        // 进入评论页面
        adapter.setOnClickListener( i -> startActivity( new Intent( this,CommentActivity.class ) ) );
        rvTutorial.setLayoutManager( new LinearLayoutManager( this, LinearLayoutManager.VERTICAL, false ) );
        ivAvatar.setOnClickListener( v -> {
            Intent intent = new Intent( this, IndexActivity.class );
            intent.putExtra( "id",13 );
            startActivity( intent );
        } );
    }

    private void initToolbar() {
        toolbarEntry.setNavigationOnClickListener( (v) -> {
            finish();
        } );
    }

    public void back(View view) {
        finish();
    }
}
