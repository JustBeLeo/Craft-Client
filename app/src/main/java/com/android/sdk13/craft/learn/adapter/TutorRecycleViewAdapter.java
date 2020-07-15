package com.android.sdk13.craft.learn.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.sdk13.craft.R;
import com.android.sdk13.craft.base.BaseRecycleAdapter;
import com.android.sdk13.craft.base.BaseRecycleViewHolder;
import com.android.sdk13.craft.entity.Course;

import java.util.ArrayList;

public class TutorRecycleViewAdapter extends BaseRecycleAdapter<Course> {

    public TutorRecycleViewAdapter(ArrayList<Course> list, Context mContext, RecyclerView recyclerView) {
        super( list, mContext, recyclerView );
    }

    @Override
    protected BaseRecycleViewHolder setHolder(View view, int ItemType) {
        return new Holder( view );
    }

    @Override
    protected int getLayoutID(int ItemType) {
        return R.layout.item_tutorial;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseRecycleViewHolder holder, int i) {
        if(holder instanceof Holder){
            if (mOnClickListener!=null){
                holder.itemView.setOnClickListener( v -> {
                    mOnClickListener.onClick( i );
                } );
            }
        }
    }

    public interface onClickListener{
        void onClick(int i);
    }

    private onClickListener mOnClickListener = null;

    public void setmOnClickListener(onClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
    }

    class Holder extends BaseRecycleViewHolder{

        Holder(View itemView) {
            super( itemView );
        }
    }
}
