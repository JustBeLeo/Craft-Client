package com.android.sdk13.craft.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.annotations.NonNull;

public abstract class BaseFragment extends Fragment {
    Unbinder unbinder;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = LayoutInflater.from(getContext()).inflate(getLayoutID(),null);
        View view = View.inflate( getContext(),getLayoutID(),null );
        unbinder = ButterKnife.bind( this,view );
        initData();
        initView();
        return view;
    }

    protected abstract void initData();

    protected abstract void initView();

    protected abstract int getLayoutID();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
