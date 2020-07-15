package com.android.sdk13.craft.communication;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;

import com.android.sdk13.craft.R;
import com.android.sdk13.craft.base.BaseFragment;
import com.android.sdk13.craft.entity.InboxBean;
import com.android.sdk13.craft.independent_activity.InboxActivity;
import com.android.sdk13.craft.utils.NetConfig;
import com.android.sdk13.craft.utils.UIUtils;

import java.util.ArrayList;

import butterknife.BindView;

/*
 * @Author sdk13
 * @Description 私聊的列表Pager
 * @Date 16:44 2020/6/26
 **/
public class InboxPager extends BaseFragment {

    @BindView(R.id.rv_pager_common)
    RecyclerView rv;

    ArrayList<InboxBean> list;

    @Override
    protected void initData() {
        InboxBean inbox1 = new InboxBean(NetConfig.URL + "/static/upload/avatar/head1.png", "天工官方账号", "欢迎加入天工大家庭，享受传统手工艺带来的乐趣", "8-18");
        InboxBean inbox2 = new InboxBean(NetConfig.URL + "/static/upload/avatar/head2.png", "普通的Leo", "嘿你好啊，我们能交个朋友吗？", "8-19");
        InboxBean inbox3 = new InboxBean(NetConfig.URL + "/static/upload/avatar/head3.png", "一个小孩", "宝鼎茶闲烟尚绿，幽窗棋罢指犹凉", "8-19");
        InboxBean inbox4 = new InboxBean(NetConfig.URL + "/static/upload/avatar/head4.png", "一期一会", "想要什么功能可以私聊我哟！", "8-20");
        list = new ArrayList<>();
        list.add(inbox1);
        list.add(inbox2);
        list.add(inbox3);
        list.add(inbox4);
    }

    @Override
    protected void initView() {
        InboxAdapter adapter = new InboxAdapter(list, getContext(), rv);
        adapter.setOnClickListener(i -> {
            InboxBean inbox = list.get(i);
            Intent intent = new Intent(getContext(), InboxActivity.class);
            intent.putExtra("name", inbox.getUser());
            intent.putExtra("avatar", inbox.getAvatar());
            startActivity(intent);
        });
        UIUtils.setVerticalRecycleView(getContext(), rv, adapter);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.pager_common;
    }
}
