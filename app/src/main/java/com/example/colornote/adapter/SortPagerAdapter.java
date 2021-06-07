package com.example.colornote.adapter;

import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.colornote.fragment.ColorFragment;
import com.example.colornote.fragment.SortByFragment;
import com.example.colornote.fragment.ViewFragment;

import java.util.ArrayList;

public class SortPagerAdapter extends FragmentStatePagerAdapter {
    ArrayList<Fragment> fragments;
    public SortPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
        fragments =  new ArrayList<>();
        fragments.add(new ColorFragment());
        fragments.add(new SortByFragment());
        fragments.add(new ViewFragment());
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Nullable
    @Override
    public Parcelable saveState() {
        return null;
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
