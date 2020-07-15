package com.android.sdk13.craft.home;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.sdk13.craft.base.BaseFragment;
import com.android.sdk13.craft.base.GlideImageLoader;
import com.android.sdk13.craft.article.ArticleActivity;
import com.android.sdk13.craft.entity.Announce;
import com.android.sdk13.craft.entity.Article;
import com.android.sdk13.craft.R;
import com.android.sdk13.craft.entity.Video;
import com.android.sdk13.craft.entity.UserTemp;
import com.android.sdk13.craft.utils.MyApplication;
import com.android.sdk13.craft.utils.NetConfig;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.sunfusheng.marqueeview.MarqueeView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.jzvd.JzvdStd;

public class HomeFragment extends BaseFragment {

    @BindView(R.id.banner_home)
    Banner bannerHome;
    @BindView( R.id.marqueeView )
    MarqueeView marqueeView;
    @BindView( R.id.rv_home_article)
    RecyclerView rvArticle;
    @BindView( R.id.rv_home2 )
    RecyclerView rvVideo;

    List<Article> articleList;
    ArticleListAdapter articleAdapter;

    List<Video> videoList;
    VideoListAdapter videoAdapter;

    int currentPage = 1;
    int pageCount = 1;
    int pageSize = 4;

    @Override
    protected void initData() {
        articleList = new ArrayList<>();
        OkGo.<String>get(NetConfig.ARTICLE_LIST)
                .params("pageNum", currentPage)
                .params("pageSize", pageSize)
                .params("sort", "")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        JSONObject object = JSONObject.parseObject(response.body());
                        JSONArray array = object.getJSONArray("records");
                        articleList = JSONArray.parseArray(array.toJSONString(), Article.class);
                        currentPage = object.getInteger("currentPage");
                        pageCount = object.getInteger("pageCount");
                        initArticleList();
                    }
                });

        OkGo.<String>get(NetConfig.VIDEO_LIST)
                .params("sort", "")
                .params("pageNum", currentPage)
                .params("pageSize", pageSize)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        JSONObject object = JSONObject.parseObject(response.body());
                        JSONArray array = object.getJSONArray("records");
                        videoList = JSONArray.parseArray(array.toJSONString(), Video.class);
                        currentPage = object.getInteger("currentPage");
                        pageCount = object.getInteger("pageCount");
                        initVideoList();
                    }
                });

    }

    private void initArticleList() {
        articleAdapter = new ArticleListAdapter(articleList,getContext(), rvArticle);
        rvArticle.setAdapter( articleAdapter );
        rvArticle.setLayoutManager( new GridLayoutManager( getContext(),2 ) );
        articleAdapter.setmOnClickListener( i -> {
            // 取出对应数据
            Intent intent = new Intent( getContext(), ArticleActivity.class );
            intent.putExtra("article_id", articleList.get(i).getId());
            startActivity( intent );
        } );
    }

    private void initVideoList() {
        videoAdapter = new VideoListAdapter(videoList, getContext(), rvVideo);
        videoAdapter.setOnClickListener(i -> {
            Video video = videoList.get(i);
            OkGo.<String>get(NetConfig.VIDEO_DETAIL)
                    .params("Video_id",video.getId())
                    .params("user_id", UserTemp.user.getId())
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            JzvdStd.startFullscreenDirectly(getContext(), JzvdStd.class, NetConfig.URL + video.getVideo_url(), video.getName());
                        }
                    });
        });
        rvVideo.setAdapter(videoAdapter);
        rvVideo.setLayoutManager(new GridLayoutManager( getContext(),2 ));

    }

    @Override
    protected void initView() {
        MyApplication.toolbar.setTitle( "探寻世界" );
        initBanner();       // 初始化顶部轮播广告
        initMarquee();      // 初始化公告组件
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged( hidden );
        if(!hidden){
            MyApplication.toolbar.setTitle( "探寻世界" );
        }
    }

    private void initMarquee() {
        OkGo.<String>get( NetConfig.HOME_ANNOUNCE )
                .execute( new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        int code = NetConfig.getCode( response.body() );
                        if(code == 200){
                            JSONArray j1 = JSON.parseObject( response.body() ).getJSONObject( "data" ).getJSONArray( "announces" );
                            List<Announce> data = JSONArray.parseArray( j1.toJSONString(), Announce.class );
                            List<String> messages = new ArrayList<>();
                            for(Announce a : data){
                                messages.add( a.getTitle() );
                            }
                            marqueeView.startWithList(messages);
                            marqueeView.setOnItemClickListener((position, textView) -> {
                                new AlertDialog.Builder(getContext())
                                        .setTitle(data.get(position).getTitle())
                                        .setMessage(data.get(position).getContent())
                                        .setPositiveButton("我知道了",null)
                                        .show();
                            });
                        }
                    }
                } );
    }

    private void initBanner() {
        List<Integer> images = new ArrayList<>();
        images.add( R.mipmap.back8 );
        images.add( R.mipmap.back6 );
        images.add( R.mipmap.back7 );
        List<String> titles = new ArrayList<>();
        titles.add( "巧夺天工的传世技艺" );
        titles.add( "白庙主人的笑傲江湖" );
        titles.add( "文物医生的进阶之路：千里之行，始于足下" );
        bannerHome.setImageLoader( new GlideImageLoader() );
        bannerHome.setBannerStyle( BannerConfig.CIRCLE_INDICATOR_TITLE);
        bannerHome.setBannerAnimation( Transformer.ZoomOutSlide );
        bannerHome.setImages( images );
        bannerHome.setBannerTitles( titles );
        bannerHome.setDelayTime(6000);
        bannerHome.start();
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_home;
    }
}
