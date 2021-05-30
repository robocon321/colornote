package com.example.colornote.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.colornote.R;
import com.example.colornote.adapter.ViewDetailsAdapter;
import com.example.colornote.adapter.ViewGridAdapter;
import com.example.colornote.adapter.ViewLargeGridAdapter;
import com.example.colornote.adapter.ViewListAdapter;
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

public class HomeFragment extends Fragment {
    Toolbar toolbar;
    Button btnSort;
    static GridView gvTask;
    static BaseAdapter adapter;
    static ArrayList<Task> tasks;
    static DialogSortFragment dialogSortFragment;
    CheckListDAO checkListDAO = CheckListDAO.getInstance();
    TextDAO textDAO = TextDAO.getInstance();
    Dialog dialogEditColor;
    ImageView imgEdit, imgNumber;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);
        init(view);
        setEvents();
        return view;
    }

    public void init(View view){
        toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        btnSort = view.findViewById(R.id.btnSort);
        gvTask = view.findViewById(R.id.gvTask);

        tasks = new ArrayList<>();
        tasks.addAll(textDAO.getAll(new TextMapper()));
        tasks.addAll(checkListDAO.getAll(new CheckListMapper()));
        adapter = new ViewListAdapter(tasks, getActivity());

        Collections.sort(tasks, Task.compareByTitle);
        adapter.notifyDataSetChanged();
        gvTask.setAdapter(adapter);
    }

    public void setEvents(){
        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSortFragment =  new DialogSortFragment();
                dialogSortFragment.show(getActivity().getSupportFragmentManager(),"Show");
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.top_home_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.mnView:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder.setView(R.layout.fragment_view_option);
                }
                builder.setTitle("View");
                AlertDialog dialog = builder.create();

                dialog.show();
                Button btnSortList = dialog.findViewById(R.id.btnSortList);
                Button btnSortDetail = dialog.findViewById(R.id.btnSortDetail);
                Button btnSortGrid = dialog.findViewById(R.id.btnSortGrid);
                Button btnSortLargeGrid = dialog.findViewById(R.id.btnSortLargeGrid);

                btnSortList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeAdapter(new ViewListAdapter(tasks, getActivity()), 1);
                        dialog.cancel();
                    }
                });

                btnSortDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeAdapter(new ViewDetailsAdapter(tasks, getActivity()), 1);
                        dialog.cancel();
                    }
                });

                btnSortGrid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            changeAdapter(new ViewGridAdapter(tasks, getActivity()), 3);
                        dialog.cancel();
                    }
                });

                btnSortLargeGrid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeAdapter(new ViewLargeGridAdapter(tasks, getActivity()), 2);
                        dialog.cancel();
                    }
                });

                break;
            case R.id.mnBackup:
                break;
            case R.id.mnColorOption:
                showDialogEditColor();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void changeAdapter(BaseAdapter newAdapter, int numCol){
        adapter = newAdapter;
        adapter.notifyDataSetChanged();
        gvTask.setNumColumns(numCol);
        gvTask.setAdapter(adapter);
    }

    public void addItemColorToGridLayout(Dialog dialog, Context context, GridLayout glColor, boolean isEdit, boolean isShowAmount){
        glColor.removeAllViews();
        List<Color> colors = new ArrayList<Color>();
        ColorDAO dao = ColorDAO.getInstance();
        colors = dao.getAll(new ColorMapper());

        for(Color color : colors){
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.item_color, glColor,false);
            EditText edtColor = view.findViewById(R.id.edtTitle);
            EditText edtAmount = view.findViewById(R.id.edtAmount);
            view.setBackgroundColor(android.graphics.Color.parseColor(color.getColorMain()));

            if(isShowAmount){
                edtAmount.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
                edtColor.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 2f));
                edtAmount.setBackgroundColor(android.graphics.Color.parseColor(color.getColorMain()));
                edtAmount.setText(ColorDAO.getInstance().countTask(color.getId())+"");
            }else {
                edtAmount.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0f));
                edtColor.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 3f));
            }

            edtAmount.setFocusable(false);
            edtColor.setFocusable(isEdit);
            edtColor.setText(color.getContent());
            if(!isEdit){
                imgNumber.setVisibility(View.VISIBLE);
                imgEdit.setImageResource(R.drawable.ic_edit);
                edtColor.setBackground(null);
                edtColor.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(View v) {
                        tasks.clear();
                        tasks.addAll(CheckListDAO.getInstance().getAll(new CheckListMapper()));
                        tasks.addAll(TextDAO.getInstance().getAll(new TextMapper()));
                        tasks.removeIf(task -> task.getColorId() != color.getId());
                        adapter.notifyDataSetChanged();
                        dialogEditColor.dismiss();
                    }
                });
            }else{
                imgNumber.setVisibility(View.INVISIBLE);
                imgEdit.setImageResource(R.drawable.ic_check);

                List<Color> finalColors = colors;
                imgEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for(int i = 0; i < finalColors.size(); i++){
                            String str = ((EditText) ((LinearLayout) glColor.getChildAt(i)).getChildAt(2)).getText().toString();
                            Color color = finalColors.get(i);
                            color.setContent(str);
                            ColorDAO.getInstance().update(color);
                        }
                        dialogEditColor.dismiss();
                        showDialogEditColor();
                    }
                });
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

            glColor.addView(view);
        }
    }

    public void showDialogEditColor(){
        dialogEditColor = new Dialog(getActivity());
        dialogEditColor.setContentView(R.layout.layout_color_edit);
        imgEdit= dialogEditColor.findViewById(R.id.imgEdit);
        imgNumber = dialogEditColor.findViewById(R.id.imgNumber);
        GridLayout glColor = dialogEditColor.findViewById(R.id.glColor);
        addItemColorToGridLayout(dialogEditColor, getActivity(), glColor, false, false);
        dialogEditColor.show();

        imgNumber.setOnClickListener(new View.OnClickListener() {
            boolean isShowAmount = false;
            @Override
            public void onClick(View v) {
                isShowAmount = !isShowAmount;
                addItemColorToGridLayout(dialogEditColor, getActivity(), glColor, false, isShowAmount);
            }
        });

        imgEdit.setOnClickListener(new View.OnClickListener() {
            boolean isEdit = false;
            @Override
            public void onClick(View v) {
                isEdit = !isEdit;
                addItemColorToGridLayout(dialogEditColor, getActivity(), glColor, isEdit, false);
            }
        });

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
                dialogEditColor.dismiss();
                showDialogEditColor();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
