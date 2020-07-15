package com.android.sdk13.craft.base;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import java.util.ArrayList;

public class MyPagerAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> list;
    ArrayList<String> titles;
    FragmentManager fm;

    public MyPagerAdapter(FragmentManager fm) {
        super( fm );
    }

    public MyPagerAdapter(FragmentManager fm, ArrayList<Fragment> list, ArrayList<String> titles) {
        super( fm );
        this.list = list;
        this.titles = titles;
        this.fm = fm;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Fragment getItem(int i) {
        return list.get( i );
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get( position );
    }

    public void setFragments(ArrayList<Fragment> fragments) {
        if (this.list != null) {
            FragmentTransaction ft = fm.beginTransaction();
            for (Fragment f : this.list) {
                ft.remove(f);
            }
            ft.commit();
            fm.executePendingTransactions();
        }
        if (fragments != null)
            this.list = fragments;
        notifyDataSetChanged();
    }

    public void clearFragment() {
        setFragments(null);
    }
}
