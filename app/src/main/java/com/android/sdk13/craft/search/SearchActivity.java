package com.android.sdk13.craft.search;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.sdk13.craft.R;
import com.android.sdk13.craft.article.ArticleActivity;
import com.android.sdk13.craft.article.adapter.ArticleAdapter;
import com.android.sdk13.craft.entity.Article;
import com.android.sdk13.craft.entity.UserTemp;
import com.android.sdk13.craft.utils.NetConfig;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
 * @Author sdk13
 * @Description 用于搜索的一个页面
 * @Date 11:31 2020/6/26
 **/
public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.rv_search_history)
    RecyclerView rvHistory;
    @BindView(R.id.ll_search_history)
    LinearLayout llHistory;
    @BindView(R.id.rv_search_result)
    RecyclerView rvResult;
    @BindView(R.id.ed_search_text)
    EditText edText;

    SharedPreferences sp;            // 存历史记录
    SharedPreferences.Editor editor;
    Map<String, ?> historyRecord;    // 历史记录Map

    int amount = 0;

    int currentPage = 1;
    int pageCount = 1;
    int pageSize = 8;
    List<Article> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        amount = 0;
        sp = getSharedPreferences("History", MODE_PRIVATE);
        historyRecord = sp.getAll();
        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> keys = new ArrayList<>();
        for (Map.Entry<String, ?> entry : historyRecord.entrySet()) {
            amount++;
            values.add(entry.getValue().toString());
            keys.add(entry.getKey());
        }
        SearchHistoryAdapter adapter = new SearchHistoryAdapter(values, this, rvHistory);
        adapter.setOnDeleteClick(i -> {
            editor = sp.edit();
            editor.remove(keys.get(i));
            editor.apply();
            initData();
        });
        adapter.setOnTextClick(i -> {
            edText.setText(values.get(i));
        });
        rvHistory.setAdapter(adapter);
        rvHistory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        adapter.notifyDataSetChanged();
    }

    /*
     * @Description 清除历史记录
     * @Date 11:31 2020/6/26
     **/
    public void clearHistory(View view) {
        editor = sp.edit();
        editor.clear();
        editor.apply();
        initData();
    }

    /*
     * @Description 搜索按钮监听
     * @Date 11:37 2020/6/26
     **/
    public void search(View view) {
        llHistory.setVisibility(View.INVISIBLE);
        rvResult.setVisibility(View.VISIBLE);
        String text = edText.getText().toString();
        editor = sp.edit();
        editor.putString(amount + "", text);
        editor.apply();
        OkGo.<String>get(NetConfig.ARTICLE_LIST)
                .params("user_id", UserTemp.user.getId())
                .params("title", text)
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
                        initData();
                    }
                });
    }

    private void initRecycleView() {
        ArticleAdapter adapter = new ArticleAdapter(list, this, rvResult);
        rvResult.setAdapter(adapter);
        rvResult.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter.setOnClickListener(i -> {
            startActivity(new Intent(this, ArticleActivity.class).putExtra("article_id", list.get(i).getId()));
        });
    }

    /*
     * @Description 返回上级界面
     * @Date 11:38 2020/6/26
     **/
    public void back(View view) {
        finish();
    }
}
