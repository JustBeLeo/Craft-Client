package com.android.sdk13.craft.learn.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.sdk13.craft.R;
import com.android.sdk13.craft.entity.Video;
import com.android.sdk13.craft.utils.AppScreenMgr;
import com.android.sdk13.craft.utils.NetConfig;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.Holder> {


    private List<Video> list;
    private Context mContext;
    private RecyclerView rv;

    public VideoAdapter(List<Video> list, Context mContext, RecyclerView rv) {
        this.list = list;
        this.mContext = mContext;
        this.rv = rv;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_post_video, rv, false);
        return new Holder(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull Holder holder, int index) {
        Video video = list.get(index);
        holder.tvPlayed.setText(String.format("%d次播放", video.getClick_count()));
        holder.tvTitle.setText(video.getName());
        holder.tvTime.setText(String.format("%s,%s", video.getUser().getUsername(), video.getType().getName()));
        Glide.with(mContext)
                .load(NetConfig.URL + video.getCover_url())
                .into(holder.ivCover);
        holder.ll.setOnClickListener(view -> onClickListener.onClick(index));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface onClickListener{
        void onClick(int i);
    }

    onClickListener onClickListener = null;

    public void setOnClickListener(onClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    static class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_item_post_video_time)
        TextView tvTime;
        @BindView(R.id.tv_item_post_video_played)
        TextView tvPlayed;
        @BindView(R.id.tv_item_post_video_title)
        TextView tvTitle;
        @BindView(R.id.iv_item_post_video_cover)
        ImageView ivCover;
        @BindView(R.id.ll_item_post_video)
        LinearLayout ll;
        Holder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
