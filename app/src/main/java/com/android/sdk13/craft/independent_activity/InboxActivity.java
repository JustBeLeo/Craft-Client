package com.android.sdk13.craft.independent_activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.sdk13.craft.R;
import com.android.sdk13.craft.entity.Chat;
import com.android.sdk13.craft.entity.UserTemp;
import com.android.sdk13.craft.utils.NetConfig;
import com.android.sdk13.craft.utils.UIUtils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InboxActivity extends AppCompatActivity {

    @BindView(R.id.iv_inbox_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_offline_title)
    TextView tvTitle;
    @BindView(R.id.rv_inbox)
    RecyclerView rvInbox;
    @BindView( R.id.ed_inbox_text )
    EditText edText;

    ArrayList<Chat> list = new ArrayList<>();
    ChatAdapter adapter;
    String avatarUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_inbox );
        ButterKnife.bind( this );
        String name = getIntent().getStringExtra( "name" );
        avatarUrl = getIntent().getStringExtra( "avatar" );
        tvTitle.setText( name );
        Glide.with( this )
                .load( avatarUrl )
                .into( ivAvatar );
        adapter = new ChatAdapter( list,this,rvInbox );
        UIUtils.setVerticalRecycleView( this,rvInbox,adapter );
    }

    public void back(View view) {
        finish();
    }

    // 发送消息
    public void go(View view) {
        Chat chat = new Chat(NetConfig.URL + UserTemp.user.getAvatar_url(),edText.getText().toString(),true );
        edText.setText( "" );
        list.add( chat );
        adapter.notifyDataSetChanged();
        new Handler( ).postDelayed( () -> {
            Chat chat1 = new Chat( avatarUrl,"已收到你的来信",false );
            list.add( chat1 );
            adapter.notifyDataSetChanged();
        },2000 );
    }
}
