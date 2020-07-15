package com.android.sdk13.craft.independent_activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.sdk13.craft.R;
import com.android.sdk13.craft.base.BaseRecycleAdapter;
import com.android.sdk13.craft.base.BaseRecycleViewHolder;
import com.android.sdk13.craft.entity.Step;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import cn.jzvd.JzvdStd;

public class StepAdapter extends BaseRecycleAdapter<Step> {


    public StepAdapter(ArrayList<Step> list, Context mContext, RecyclerView recyclerView) {
        super( list, mContext, recyclerView );
    }

    @Override
    protected BaseRecycleViewHolder setHolder(View view, int ItemType) {
        return new Holder( view );
    }

    @Override
    protected int getLayoutID(int ItemType) {
        return R.layout.item_tutorial_video;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull BaseRecycleViewHolder holder, int i) {
        if(holder instanceof Holder){
            Step step = list.get( i );
            ((Holder) holder).tvStep.setText( String.format( "第%d步", step.getStep() ) );
            ((Holder) holder).tvLearned.setText( String.format( "%d人表示看懂了", step.getLearned() ) );
            ((Holder) holder).tvComments.setText( String.format( "%d条评论", step.getComments() ) );
            ((Holder) holder).tvStepText.setText( step.getOperate() );
            Glide.with( mContext )
                    .load( step.getVideoUrl() )
                    .into( ((Holder) holder).jz.thumbImageView );
            if(onClickListener!=null){
                ((Holder) holder).tvComments.setOnClickListener( v -> onClickListener.onClick( i ) );
            }
        }
    }

    class Holder extends BaseRecycleViewHolder {
        @BindView(R.id.tv_tv_step)
        TextView tvStep;
        @BindView(R.id.tv_tv_step_text)
        TextView tvStepText;
        @BindView(R.id.jz_tv)
        JzvdStd jz;
        @BindView(R.id.tv_tv_comments)
        TextView tvComments;
        @BindView(R.id.tv_tv_learned)
        TextView tvLearned;
        Holder(View itemView) {
            super( itemView );
        }
    }
}
