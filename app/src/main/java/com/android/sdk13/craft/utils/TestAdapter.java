package com.android.sdk13.craft.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
/*
 * @Author sdk13
 * @Description 用于测试RecycleView Item形状的测试Adapter
 * @Date 16:09 2020/6/25
 **/
public class TestAdapter extends RecyclerView.Adapter<TestAdapter.Holder> {
    private Context mContext;
    private RecyclerView rv;
    private int id;
    private int count;

    public TestAdapter( Context mContext,RecyclerView rv, int id , int count) {
        this.count = count;
        this.id = id;
        this.rv = rv;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(id,rv,false);
        return new Holder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
    }

    @Override
    public int getItemCount() {
        return count;
    }

    class Holder extends RecyclerView.ViewHolder{

        public Holder(@NonNull View itemView) {
            super( itemView );
        }
    }
}
