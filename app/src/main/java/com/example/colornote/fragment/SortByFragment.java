package com.example.colornote.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.colornote.R;
import com.example.colornote.model.Task;
import com.example.colornote.util.Constant;

import java.util.Collections;

public class SortByFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_sort_option, container, false);

        view.findViewById(R.id.btnSortModified).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                HomeFragment.sortType = Constant.SORT_BY.MODIFIED_TIME;
                HomeFragment.filter();
                HomeFragment.dialogSortFragment.dismiss();
            }
        });

        view.findViewById(R.id.btnSortAlpha).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                HomeFragment.sortType = Constant.SORT_BY.ALPHABECALLY;
                HomeFragment.filter();
                HomeFragment.dialogSortFragment.dismiss();
            }
        });

        view.findViewById(R.id.btnSortReminder).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                HomeFragment.sortType = Constant.SORT_BY.REMINDER;
                HomeFragment.filter();
                HomeFragment.dialogSortFragment.dismiss();
            }
        });

        view.findViewById(R.id.btnSortColor).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                HomeFragment.sortType = Constant.SORT_BY.COLOR;
                HomeFragment.filter();
                HomeFragment.dialogSortFragment.dismiss();
            }
        });
        return view;
    }
}
