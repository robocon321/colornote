package com.example.colornote.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.example.colornote.model.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ColorFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.layout_color_option, container, false);
        addItemColorToGridLayout(getActivity(), view.findViewById(R.id.glColor), false, false);
        return view;
    }

    public static void addItemColorToGridLayout(Context context, GridLayout glColor, boolean isEdit, boolean isShowAmount){
        ArrayList<Task> tasks = HomeFragment.tasks;

        List<Color> colors = new ArrayList<Color>();
        ColorDAO dao = ColorDAO.getInstance();
        colors = dao.getAll(new ColorMapper());

        for(Color color : colors){
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.item_color, glColor,false);
            EditText edtColor = view.findViewById(R.id.edtTitle);
            EditText edtAmount = view.findViewById(R.id.edtAmount);

            edtColor.setBackgroundColor(android.graphics.Color.parseColor(color.getColorMain()));
            edtColor.setFocusable(isEdit);

            if(isShowAmount){
                edtAmount.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
                edtColor.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 2f));
                edtAmount.setBackgroundColor(android.graphics.Color.parseColor(color.getColorMain()));
            }else {
                edtAmount.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0f));
                edtColor.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 3f));
            }

            edtColor.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View v) {
                    tasks.clear();
                    tasks.addAll(CheckListDAO.getInstance().getAll(new CheckListMapper()));
                    tasks.addAll(TextDAO.getInstance().getAll(new TextMapper()));
                    tasks.removeIf(task -> task.getColorId() != color.getId());
                    HomeFragment.adapter.notifyDataSetChanged();
                    HomeFragment.dialogSortFragment.dismiss();
                }
            });

            glColor.addView(view);
        }
    }
}
