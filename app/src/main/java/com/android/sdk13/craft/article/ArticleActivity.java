package com.android.sdk13.craft.article;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.sdk13.craft.R;
import com.android.sdk13.craft.article.adapter.CommentAdapter;
import com.android.sdk13.craft.entity.Article;
import com.android.sdk13.craft.entity.Comment;
import com.android.sdk13.craft.entity.User;
import com.android.sdk13.craft.independent_activity.StlActivity;
import com.android.sdk13.craft.entity.UserTemp;
import com.android.sdk13.craft.utils.NetConfig;
import com.android.sdk13.craft.utils.StringUtils;
import com.android.sdk13.craft.utils.TestAdapter;
import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jzvd.Jzvd;

public class ArticleActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_article)
    Toolbar toolbarArticle;
    @BindView(R.id.tv_article_title)
    TextView tvArticleTitle;
    @BindView(R.id.rv_article_comments)
    RecyclerView rvComments;
    @BindView(R.id.rv_article_version)
    RecyclerView rvVersion;
    @BindView(R.id.tv_article_type)
    TextView tvArticleType;

    @BindView(R.id.iv_article_text)
    TextView tvArticleText;
    @BindView(R.id.iv_article_content)
    ImageView ivArticleContent;

    @BindView(R.id.iv_article_avatar)
    ImageView ivArticleAvatar;
    @BindView(R.id.tv_article_username)
    TextView tvArticleUsername;
    @BindView(R.id.tv_article_focused)
    TextView tvArticleFocused;
    @BindView(R.id.ed_article_comment_text)
    EditText edCommentText;
    @BindView(R.id.ib_article_focus)
    ImageButton ibFocus;


    // 文章id
    Long article_id;
    // 文章对象
    Article article;
    // 评论适配器
    CommentAdapter adapter;
    // 评论列表
    List<Comment> commentList = new ArrayList<>();
    // 关注情况
    boolean isFocus;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        ButterKnife.bind(this);
        article_id = getIntent().getLongExtra("article_id", -1);
        initView();
        initVersion();
    }

    private void initView() {
        // 设置返回退出
        toolbarArticle.setNavigationOnClickListener((v) -> {
            finish();
        });

        if (article_id == -1) {
            Toast.makeText(this, "获取文章详情失败", Toast.LENGTH_SHORT).show();
            return;
        }

        OkGo.<String>get(NetConfig.ARTICLE_DETAIL)
                .params("user_id", UserTemp.user.getId())
                .params("article_id", article_id)
                .execute(new StringCallback() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (response.code() == 500) {
                            Toast.makeText(ArticleActivity.this, "未找到文章", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        article = JSON.parseObject(response.body())
                                .getJSONObject("data")
                                .getObject("article", Article.class);

                        tvArticleTitle.setText(article.getTitle());
                        tvArticleType.setText(article.getType().getName());
                        tvArticleText.setText(article.getText());
                        Glide.with(ArticleActivity.this)
                                .load(NetConfig.URL + article.getContent_url())
                                .into(ivArticleContent);

                        // 发布者设置
                        User user = article.getUser();
                        Glide.with(ArticleActivity.this)
                                .load(NetConfig.URL + user.getAvatar_url())
                                .circleCrop()
                                .into(ivArticleAvatar);
                        tvArticleFocused.setText(user.getFocused_count() + "位关注者");
                        tvArticleUsername.setText(user.getUsername());
                        initComments();
                        initFocus();
                    }
                });



    }

    private void initFocus() {
        // 不停自启
        if (article == null){
            handler.postDelayed(this::initFocus,500);
        }
        // 有了以后开始检测
        // 设置关注情况
        OkGo.<String>post(NetConfig.FOCUS_JUDGE)
                .params("user_id",UserTemp.user.getId())
                .params("focus_id",article.getUser().getId())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (NetConfig.judgeCode(response)){
                            Boolean object = (Boolean) JSON.parseObject(response.body())
                                    .getJSONObject("data")
                                    .getBoolean("isFocus");
                            // 如果关注了
                            if (object!=null && object){
                                Glide.with(ArticleActivity.this)
                                        .load(R.drawable.ic_like_big_fill)
                                        .into(ibFocus);
                                isFocus = true;
                            }else {
                                Glide.with(ArticleActivity.this)
                                        .load(R.drawable.ic_like_big)
                                        .into(ibFocus);
                                isFocus = false;
                            }
                        }
                    }
                });

    }

    /*
     * Description 显示评论列表
     * Date 2020/7/8 11:18
     * Param
     **/
    private void initComments() {
        OkGo.<String>get(NetConfig.ARTICLE_GET_COMMENTS)
                .params("article_id", article_id)
                .params("pageNum", 1)
                .params("pageSize", 5)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        JSONArray array = JSON.parseObject(response.body()).getJSONObject("data").getJSONArray("list");
                        commentList = JSONArray.parseArray(array.toJSONString(), Comment.class);
                        initCommentsView();
                    }
                });

    }

    /*
     * Description 加载评论
     * Date 2020/7/8 13:29
     * Param
     **/
    private void initCommentsView() {
        adapter = new CommentAdapter(commentList, ArticleActivity.this);
        rvComments.setAdapter(adapter);
        rvComments.setLayoutManager(new LinearLayoutManager(ArticleActivity.this, LinearLayoutManager.VERTICAL, false));
    }

    private void initVersion() {
        rvVersion.setAdapter(new TestAdapter(this, rvVersion, R.layout.item_version, 6));
        rvVersion.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    public void go(View view) {
        startActivity(new Intent(this, StlActivity.class));
    }

    /*
     * Description 发布评论
     * Date 2020/7/8 13:29
     * Param
     **/
    public void sendComment(View view) {
        String content = edCommentText.getText().toString();
        if (StringUtils.isBlank(content)) {
            Toast.makeText(this, "评论不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        OkGo.<String>post(NetConfig.ARTICLE_SEND_COMMENT)
                .params("user_id", UserTemp.user.getId())
                .params("article_id", article_id)
                .params("content", content)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Toast.makeText(ArticleActivity.this, "评论成功！", Toast.LENGTH_SHORT).show();
                        edCommentText.setText("");
                        initComments();
                    }
                });
    }


    public void focus(View view) {
        String url = NetConfig.FOCUS_ADD;
        if (isFocus){
            url = NetConfig.FOCUS_CANCEL;
        }
        OkGo.<String>post(url)
                .params("user_id",UserTemp.user.getId())
                .params("focus_id",article.getUser().getId())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (NetConfig.judgeCode(response)){
                            Toast.makeText(ArticleActivity.this,"成功",Toast.LENGTH_SHORT).show();
                            initFocus();
                        }else if (NetConfig.getCode(response.body())==200){
                            String error = JSONObject.parseObject(response.body()).getString("msg");
                            Toast.makeText(ArticleActivity.this,error,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }


}
