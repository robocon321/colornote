package com.example.colornote.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.colornote.fragment.FragmentColor;
import com.example.colornote.fragment.FragmentSortBy;
import com.example.colornote.fragment.FragmentView;

import java.util.ArrayList;

public class SortPagerAdapter extends FragmentStatePagerAdapter {
    ArrayList<Fragment> fragments;
    public SortPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
        fragments =  new ArrayList<>();
        fragments.add(new FragmentColor());
        fragments.add(new FragmentSortBy());
        fragments.add(new FragmentView());
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String result = "";
        switch (position){
            case 0:
                result = "Color";
                break;
            case 1:
                result = "Sort";
                break;
            default:
                result = "View";
                break;
        }
        return result;
    }


}
