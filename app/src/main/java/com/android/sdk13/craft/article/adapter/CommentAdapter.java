package com.android.sdk13.craft.article.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.sdk13.craft.R;
import com.android.sdk13.craft.entity.Comment;
import com.android.sdk13.craft.utils.NetConfig;
import com.bumptech.glide.Glide;


import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * ProjectName:    Craft
 * Package:        com.android.sdk13.craft.article.adapter
 * ClassName:      CommentAdapter
 * Description:
 * Author:         Leo
 * CreateDate:     2020/7/8 10:34
 * UpdateUser:     更新者
 * UpdateDate:     2020/7/8 10:34
 * UpdateRemark:   更新说明
 * Version:        1.0
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.Holder> {


    List<Comment> list;
    Context mContext;

    public CommentAdapter(List<Comment> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_comments, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        Comment comment = list.get(i);
        holder.tvContent.setText(comment.getContent());
        holder.tvLikes.setText(comment.getLike_count()+"");
        holder.tvUsername.setText(comment.getUser().getUsername());
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        holder.tvTime.setText(sdf.format(comment.getPost_time()));
        Glide.with(mContext)
                .load(NetConfig.URL + comment.getUser().getAvatar_url())
                .circleCrop()
                .into(holder.ivAvatar);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_item_comment_avatar)
        ImageView ivAvatar;
        @BindView(R.id.tv_item_comment_username)
        TextView tvUsername;
        @BindView(R.id.tv_item_comment_likes)
        TextView tvLikes;
        @BindView(R.id.tv_item_comment_content)
        TextView tvContent;
        @BindView(R.id.tv_item_comment_time)
        TextView tvTime;

        public Holder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
