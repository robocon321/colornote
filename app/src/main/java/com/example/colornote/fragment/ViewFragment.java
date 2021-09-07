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
import com.example.colornote.adapter.ViewDetailsAdapter;
import com.example.colornote.adapter.ViewGridAdapter;
import com.example.colornote.adapter.ViewLargeGridAdapter;
import com.example.colornote.adapter.ViewListAdapter;
import com.example.colornote.model.Task;
import com.example.colornote.util.Constant;

import java.util.ArrayList;

public class ViewFragment extends Fragment {
    ArrayList<Task> tasks = HomeFragment.tasks;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_view_option, container, false);
        view.findViewById(R.id.btnSortList).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                HomeFragment.viewType = Constant.VIEW.LIST;
                HomeFragment.filter();
                HomeFragment.dialogSortFragment.dismiss();
            }
        });

        view.findViewById(R.id.btnSortDetail).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                HomeFragment.viewType = Constant.VIEW.DETAILS;
                HomeFragment.filter();
                HomeFragment.dialogSortFragment.dismiss();
            }
        });

        view.findViewById(R.id.btnSortGrid).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                HomeFragment.viewType = Constant.VIEW.GRID;
                HomeFragment.filter();
                HomeFragment.dialogSortFragment.dismiss();
            }
        });

        view.findViewById(R.id.btnSortLargeGrid).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                HomeFragment.viewType = Constant.VIEW.LARGE_GRID;
                HomeFragment.filter();
                HomeFragment.dialogSortFragment.dismiss();
            }
        });
        return view;
    }
}
