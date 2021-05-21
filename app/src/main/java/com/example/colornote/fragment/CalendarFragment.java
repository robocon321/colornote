package com.example.colornote.fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.colornote.R;

import static com.example.colornote.R.menu.top_cal_menu;

public class CalendarFragment extends Fragment {
    View view;
    Toolbar toolbarCal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_calendar, container, false);
        init(view);
        return view;
    }

    public void init(View view) {
        toolbarCal = view.findViewById(R.id.toolbarCal);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbarCal);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.top_cal_menu, menu);
//        Nhật sửa hộ t cái menu với, nó chỉ nhận cái khai báo trong mainactivity thôi à
    }

}
