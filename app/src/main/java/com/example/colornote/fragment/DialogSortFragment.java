package com.example.colornote.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.example.colornote.R;
import com.example.colornote.adapter.SortPagerAdapter;
import com.example.colornote.customview.CustomViewPager;
import com.google.android.material.tabs.TabLayout;

public class DialogSortFragment extends DialogFragment {
    CustomViewPager viewPagerSort;
    TabLayout tabLayoutSort;
    SortPagerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_sort, container);
        init(view);
        return view;
    }

    public void init(View view){
        viewPagerSort = view.findViewById(R.id.viewPagerSort);
        tabLayoutSort = view.findViewById(R.id.tabLayoutSort);

        FragmentManager fragmentManager = getChildFragmentManager();
        adapter = new SortPagerAdapter(fragmentManager);
        viewPagerSort.setAdapter(adapter);
        tabLayoutSort.setupWithViewPager(viewPagerSort);
        viewPagerSort.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayoutSort));
        tabLayoutSort.setTabsFromPagerAdapter(adapter);
        tabLayoutSort.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(getResources().getColor(R.color.tab_selected), android.graphics.PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(getResources().getColor(R.color.tab_non_selected), android.graphics.PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tabLayoutSort.getTabAt(0).setIcon(R.drawable.ic_by_color);
        tabLayoutSort.getTabAt(1).setIcon(R.drawable.ic_clock);
        tabLayoutSort.getTabAt(2).setIcon(R.drawable.ic_view);

    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

}
