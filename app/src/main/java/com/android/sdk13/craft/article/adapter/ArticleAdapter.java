package com.android.sdk13.craft.article.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.sdk13.craft.R;
import com.android.sdk13.craft.base.BaseRecycleAdapter;
import com.android.sdk13.craft.base.BaseRecycleViewHolder;
import com.android.sdk13.craft.entity.Article;
import com.android.sdk13.craft.utils.NetConfig;
import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleAdapter extends BaseRecycleAdapter<Article> {

    private final static int TYPE_CONTENT = 0;//正常内容
    private final static int TYPE_FOOTER = 1; //下拉刷新

    String[] types = {"丝绸织染",
            "雕塑",
            "造纸与印刷",
            "金银细金工艺",
            "绘画",
            "剪纸",
            "景泰蓝",
            "陶瓷",
            "漆艺",
            "中药炮制"};

    public ArticleAdapter(List<Article> list, Context mContext, RecyclerView recyclerView) {
        super(list, mContext, recyclerView);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == list.size())
            return TYPE_FOOTER;
        return TYPE_CONTENT;
    }

    @Override
    protected BaseRecycleViewHolder setHolder(View view, int ItemType) {
//        if (ItemType == TYPE_FOOTER)
//            return new mHolder2(view);
        return new mHolder(view);
    }

    @Override
    protected int getLayoutID(int ItemType) {
//        if (ItemType == TYPE_FOOTER)
//            return R.layout.content_recycleview_progressbar;
        return R.layout.item_article;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseRecycleViewHolder holder, int position) {
        holder.itemView.setOnClickListener(v -> onClickListener.onClick(position));
        if (holder instanceof mHolder) {
            Article article = list.get(position);
            ((mHolder) holder).tvClick.setText(article.getClick_count() + "");
            ((mHolder) holder).tvText.setText(article.getText());
            ((mHolder) holder).tvTitle.setText(article.getTitle());
            ((mHolder) holder).tvUser.setText(article.getUser().getUsername());
            ((mHolder) holder).tvType.setText(article.getType().getName());
            Glide.with(mContext)
                    .load(NetConfig.URL + article.getCover_url())
                    .into(((mHolder) holder).ivCover);
        }
    }


    class mHolder extends BaseRecycleViewHolder {
        @BindView(R.id.iv_item_article_cover)
        ImageView ivCover;
        @BindView(R.id.tv_item_article_type)
        TextView tvType;
        @BindView(R.id.tv_item_article_title)
        TextView tvTitle;
        @BindView(R.id.tv_item_article_text)
        TextView tvText;
        @BindView(R.id.tv_item_article_user)
        TextView tvUser;
        @BindView(R.id.tv_item_article_click)
        TextView tvClick;

        public mHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    // 用于加载进度条
    class mHolder2 extends BaseRecycleViewHolder {
        public mHolder2(View itemView) {
            super(itemView);
        }
    }

}
