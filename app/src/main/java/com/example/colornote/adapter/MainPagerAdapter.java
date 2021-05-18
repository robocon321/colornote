package com.example.colornote.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.colornote.fragment.CalendarFragment;
import com.example.colornote.fragment.HomeFragment;
import com.example.colornote.fragment.MoreFragment;
import com.example.colornote.fragment.SearchFragment;

import java.util.ArrayList;

public class MainPagerAdapter extends FragmentStatePagerAdapter {
    ArrayList<Fragment> fragments;

    public MainPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
        fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new CalendarFragment());
        fragments.add(new SearchFragment());
        fragments.add(new MoreFragment());
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
}
