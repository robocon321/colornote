package com.example.colornote.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.colornote.fragment.CloudFragment;
import com.example.colornote.fragment.DeviceFragment;

import java.util.ArrayList;

public class BackupPagerAdapter extends FragmentStatePagerAdapter {
    ArrayList<Fragment> fragments;

    public BackupPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
        fragments = new ArrayList<>();
        fragments.add(new CloudFragment());
        fragments.add(new DeviceFragment());
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
        return position == 1 ? "Device":"Cloud";
    }
}
