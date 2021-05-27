package com.example.colornote.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.colornote.R;
import com.example.colornote.adapter.ViewDetailsAdapter;
import com.example.colornote.adapter.ViewGridAdapter;
import com.example.colornote.adapter.ViewLargeGridAdapter;
import com.example.colornote.adapter.ViewListAdapter;
import com.example.colornote.dao.CheckListDAO;
import com.example.colornote.dao.TextDAO;
import com.example.colornote.mapper.CheckListMapper;
import com.example.colornote.mapper.TextMapper;
import com.example.colornote.model.Task;
import com.example.colornote.model.Text;
import com.example.colornote.util.Settings;

import java.util.ArrayList;
import java.util.Collections;

public class HomeFragment extends Fragment {
    Toolbar toolbar;
    Button btnSort;
    static GridView gvTask;
    static BaseAdapter adapter;
    static ArrayList<Task> tasks;
    static DialogSortFragment dialogSortFragment;
    CheckListDAO checkListDAO = CheckListDAO.getInstance();
    TextDAO textDAO = TextDAO.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home, container, false);
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
                    builder.setView(R.layout.layout_view_option);
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
                Dialog dialogColor = new Dialog(getActivity());
                dialogColor.setContentView(R.layout.layout_color_edit);
                dialogColor.show();
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

}
