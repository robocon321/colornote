package com.example.colornote.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;

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
import java.util.List;

public class ColorFragment extends Fragment {
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_color_option, container, false);
        addItemColorToGridLayout(getActivity(), view.findViewById(R.id.glColor), false, false);
        return view;
    }
    public void addItemColorToGridLayout(Context context, GridLayout glColor, boolean isEdit, boolean isShowAmount){
        glColor.removeAllViews();
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
            edtColor.setText(color.getContent());
            edtColor.setFocusable(isEdit);
            edtAmount.setFocusable(isEdit);

            if(isShowAmount){
                edtAmount.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
                edtColor.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 2f));
                edtAmount.setBackgroundColor(android.graphics.Color.parseColor(color.getColorMain()));
                edtAmount.setText(ColorDAO.getInstance().countTask(color.getId())+"");
            }else {
                edtAmount.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0f));
                edtColor.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 3f));
            }

            edtColor.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    rename(color);
                    return true;
                }
            });

            edtAmount.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    rename(color);
                    return true;
                }
            });

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

    public void rename(Color color){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Rename");
        final EditText input = new EditText(getActivity());
        input.setText(color.getContent());
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                color.setContent(input.getText().toString());
                ColorDAO.getInstance().update(color);
                HomeFragment.dialogSortFragment.dismiss();
                HomeFragment.dialogSortFragment.show(getActivity().getSupportFragmentManager(),"Show");
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

}
