package com.example.colornote.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.colornote.R;
import com.example.colornote.dao.CheckListDAO;
import com.example.colornote.dao.ColorDAO;
import com.example.colornote.dao.TextDAO;
import com.example.colornote.mapper.CheckListMapper;
import com.example.colornote.mapper.ColorMapper;
import com.example.colornote.mapper.TextMapper;
import com.example.colornote.model.Color;
import com.example.colornote.model.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ColorFragment extends Fragment {
    List<Color> colors;
    GridLayout glColor;
    ArrayList<Task> tasks = HomeFragment.tasks;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.layout_color_option, container, false);
        colors = new ArrayList<Color>();
        ColorDAO dao = ColorDAO.getInstance();
        colors = dao.getAll(new ColorMapper());
        for(Color color : colors){
            addButton(color, view);
        }
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void addButton(Color color, View view){
        Button btnColor = new Button(getActivity());

        btnColor.setBackgroundColor(android.graphics.Color.parseColor(color.getColorMain()));
        btnColor.setGravity(Gravity.FILL);
        btnColor.setTextSize(18);

        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.setMargins(10,10,10,10);
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED,GridLayout.FILL,1f);
        params.rowSpec = params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED,GridLayout.FILL,1f);
        btnColor.setLayoutParams(params);

        btnColor.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                tasks.clear();
                tasks.addAll(CheckListDAO.getInstance().getAll(new CheckListMapper()));
                tasks.addAll(TextDAO.getInstance().getAll(new TextMapper()));
                tasks.removeIf(task -> task.getColorId() != color.getId());
                Toast.makeText(getActivity(), tasks.size()+"", Toast.LENGTH_SHORT).show();
                HomeFragment.adapter.notifyDataSetChanged();
                HomeFragment.dialogSortFragment.dismiss();
            }
        });

        glColor = view.findViewById(R.id.glColor);
        glColor.addView(btnColor);
    }
}
