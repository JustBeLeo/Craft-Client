package com.android.sdk13.craft.profile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.sdk13.craft.R;
import com.android.sdk13.craft.base.BaseRecycleAdapter;
import com.android.sdk13.craft.base.BaseRecycleViewHolder;
import com.android.sdk13.craft.entity.Video;

import java.util.ArrayList;

public class PostAdapter extends BaseRecycleAdapter<Object> {

    public static final int TYPE_VIDEO = 0;
    public static final int TYPE_ARTICLE = 1;

    public PostAdapter(ArrayList<Object> list, Context mContext, RecyclerView recyclerView) {
        super( list, mContext, recyclerView );

    }

    @Override
    protected BaseRecycleViewHolder setHolder(View view, int ItemType) {
        if(ItemType == TYPE_VIDEO)
            return new VideoHolder( view );
        return new ArticleHolder( view );
    }

    @Override
    protected int getLayoutID(int ItemType) {
        if(ItemType == TYPE_VIDEO)
            return R.layout.item_post_video;
        return R.layout.item_post_article;
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position) instanceof Video){
            return TYPE_VIDEO;
        }
        return TYPE_ARTICLE;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseRecycleViewHolder baseRecycleViewHolder, int i) {

    }

    class VideoHolder extends BaseRecycleViewHolder{

        VideoHolder(View itemView) {
            super( itemView );
        }
    }

    class ArticleHolder extends BaseRecycleViewHolder{

        ArticleHolder(View itemView) {
            super( itemView );
        }
    }

}
