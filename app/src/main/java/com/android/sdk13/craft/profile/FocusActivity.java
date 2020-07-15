package com.android.sdk13.craft.profile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.android.sdk13.craft.R;
import com.android.sdk13.craft.entity.Focus;
import com.android.sdk13.craft.entity.UserTemp;
import com.android.sdk13.craft.utils.NetConfig;
import com.android.sdk13.craft.utils.UIUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FocusActivity extends AppCompatActivity {


    @BindView(R.id.rv_focus)
    RecyclerView rvFocus;
    @BindView(R.id.tv_focus_no_data)
    TextView tvFocusNoData;

    int type;
    List<Focus> focusList;
    @BindView(R.id.toolbar_focus)
    Toolbar toolbarFocus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focus);
        ButterKnife.bind(this);
        type = getIntent().getIntExtra("type", -1);
        toolbarFocus.setNavigationOnClickListener((v) -> {
            finish();
        });
        initData();
    }

    private void initData() {
        String url;
        if (type == 1) {
            url = NetConfig.FOCUS_LIST;
            toolbarFocus.setTitle("关注列表");
        } else {
            url = NetConfig.FOCUSED_LIST;
            toolbarFocus.setTitle("粉丝列表");
        }

        OkGo.<String>get(url)
                .params("user_id", UserTemp.user.getId())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (NetConfig.getCode(response.body()) == 200) {
                            JSONArray array = JSON.parseObject(response.body())
                                    .getJSONObject("data")
                                    .getJSONArray("list");
                            if (array.size() == 0) {
                                tvFocusNoData.setVisibility(View.VISIBLE);
                            } else {
                                focusList = JSONArray.parseArray(array.toJSONString(), Focus.class);
                                initRecycleView();
                            }
                        }
                        if (NetConfig.getCode(response.body()) == 404) {
                            tvFocusNoData.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    private void initRecycleView() {
        FocusAdapter adapter = new FocusAdapter(focusList, this, rvFocus,type);
        rvFocus.setAdapter(adapter);
        UIUtils.setVerticalRecycleView(this, rvFocus, adapter);
    }


}
