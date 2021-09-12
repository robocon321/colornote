package com.example.colornote.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.colornote.R;
import com.example.colornote.activity.BackupActivity;
import com.example.colornote.adapter.ViewAdapter;
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
import com.example.colornote.util.Constant;
import com.example.colornote.util.ISeletectedObserver;
import com.example.colornote.util.SelectedObserverService;
import com.example.colornote.util.SyncFirebase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment implements ISeletectedObserver {
    Toolbar toolbar;
    public static Button btnSort;
    static GridView gvTask;
    public static ViewAdapter adapter;
    public static ArrayList<Task> tasks;
    public static DialogSortFragment dialogSortFragment;
    Dialog dialogEditColor;
    TextView txtCount;
    ImageView imgEdit, imgNumber, imgRange, imgClose;
    View toolbarHidden;
    String accountId = "";

    public static int colorType = 1;
    public static int sortType = Constant.SORT_BY.NO_SORT;
    public static int viewType = Constant.VIEW.LIST;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void filter() {
        tasks.clear();
        tasks.addAll(TextDAO.getInstance().getByStatus(Constant.STATUS.NORMAL));
        tasks.addAll(CheckListDAO.getInstance().getByStatus(Constant.STATUS.NORMAL));

        if(colorType != 1) {
            tasks.removeIf(task -> task.getColorId() != colorType);
            Color color = ColorDAO.getInstance().get(new ColorMapper(), colorType);
            btnSort.setBackgroundColor(android.graphics.Color.parseColor(color.getColorMain() == null ? Constant.MAIN_COLOR : color.getColorMain()));
        }

        switch (sortType){
            case Constant.SORT_BY.MODIFIED_TIME:
                Collections.sort(tasks, Task.compareByModifiedTime);
                btnSort.setText("Sort by motified time");
                break;
            case Constant.SORT_BY.ALPHABECALLY:
                Collections.sort(tasks, Task.compareByTitle);
                btnSort.setText("Sort by alphabetically");
                break;
            case Constant.SORT_BY.COLOR:
                Collections.sort(tasks, Task.compareByColor);
                btnSort.setText("Sort by color");
                break;
            case Constant.SORT_BY.REMINDER:
                Collections.sort(tasks, Task.compareByReminderTime);
                btnSort.setText("Sort by reminder");
                break;
            default:
                break;
        }

        switch (viewType) {
            case Constant.VIEW.LIST:
                adapter = new ViewListAdapter(tasks, btnSort.getContext());
                gvTask.setNumColumns(1);
                break;
            case Constant.VIEW.DETAILS:
                adapter = new ViewDetailsAdapter(tasks, btnSort.getContext());
                gvTask.setNumColumns(1);
                break;
            case Constant.VIEW.GRID:
                adapter = new ViewGridAdapter(tasks, btnSort.getContext());
                gvTask.setNumColumns(3);
                break;
            case Constant.VIEW.LARGE_GRID:
                adapter = new ViewLargeGridAdapter(tasks, btnSort.getContext());
                gvTask.setNumColumns(2);
                break;
        }
        adapter.notifyDataSetChanged();
        gvTask.setAdapter(adapter);
    }
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
        toolbarHidden = view.findViewById(R.id.toolbarHidden);
        txtCount = view.findViewById(R.id.txtCount);
        imgRange = view.findViewById(R.id.imgRange);
        imgClose = view.findViewById(R.id.imgClose);

        tasks = new ArrayList<>();
        adapter = new ViewDetailsAdapter(tasks, getActivity());
        loadTask();

        Collections.sort(tasks, Task.compareByTitle);
        adapter.notifyDataSetChanged();
        gvTask.setAdapter(adapter);

        SelectedObserverService.getInstance().addObserver(this);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("account", Context.MODE_PRIVATE);
        accountId = sharedPreferences.getString("account_id", "");
    }

    public void setEvents(){
        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSortFragment =  new DialogSortFragment();
                dialogSortFragment.show(getActivity().getSupportFragmentManager(),"Show");
            }
        });

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectedObserverService.getInstance().reset();
                adapter.updateBorderView();
            }
        });

        imgRange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectedObserverService.getInstance().selectRange();
                adapter.updateBorderView();
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.top_home_menu, menu);
        if(accountId.length() > 0) {
            menu.findItem(R.id.mnBackup).setTitle("Sync");
            menu.findItem(R.id.mnBackup).setIcon(R.drawable.black_sync);
        } else {
            menu.findItem(R.id.mnBackup).setTitle("Backup");
            menu.findItem(R.id.mnBackup).setIcon(R.drawable.ic_backup);
        }
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
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(View v) {
                        viewType = Constant.VIEW.LIST;
                        filter();
                        dialog.cancel();
                    }
                });

                btnSortDetail.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(View v) {
                        viewType = Constant.VIEW.DETAILS;
                        filter();
                        dialog.cancel();
                    }
                });

                btnSortGrid.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(View v) {
                        viewType = Constant.VIEW.GRID;
                        filter();
                        dialog.cancel();
                    }
                });

                btnSortLargeGrid.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(View v) {
                        viewType = Constant.VIEW.LARGE_GRID;
                        filter();
                        dialog.cancel();
                    }
                });
                break;
            case R.id.mnBackup:
                if(accountId.length() > 0) {
                    SyncFirebase.getInstance().sync(accountId);
                } else {
                    Intent intent = new Intent(getActivity(), BackupActivity.class);
                    getActivity().startActivity(intent);
                }
                break;
            case R.id.mnColorOption:
                showDialogEditColor();
                break;
        }
        return super.onOptionsItemSelected(item);
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
            view.setBackgroundColor(android.graphics.Color.parseColor(color.getColorMain() == null ? "#ffffff" : color.getColorMain()));

            if(isShowAmount){
                edtAmount.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
                edtColor.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 2f));
                edtAmount.setBackgroundColor(android.graphics.Color.parseColor(color.getColorMain()  == null ? "#ffffff" : color.getColorMain()));

//                 edtAmount.setBackgroundColor(android.graphics.Color.parseColor(color.getColorMain()));


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
                        colorType = color.getId();
                        filter();
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

    @Override
    public void update(SelectedObserverService s) {
        if(toolbarHidden.getVisibility() == View.VISIBLE && !s.hasSelected()){
            btnSort.setClickable(true);
            toolbar.setVisibility(View.VISIBLE);
            toolbarHidden.setVisibility(View.INVISIBLE);

        }
        if(toolbarHidden.getVisibility() == View.INVISIBLE && s.hasSelected()){
            btnSort.setClickable(false);
            toolbar.setVisibility(View.INVISIBLE);
            toolbarHidden.setVisibility(View.VISIBLE);
        }

        if(s.hasRange()){
            imgRange.setImageResource(R.drawable.ic_range_active);
            imgRange.setEnabled(true);
        }else{
            imgRange.setImageResource(R.drawable.ic_range_nonactive);
            imgRange.setEnabled(false);
        }

        txtCount.setText(s.getRatio());
    }

    public boolean hasSelected(boolean[] isSelected){
        for(int i=0;i<isSelected.length;i++){
            if(isSelected[i]) return true;
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SelectedObserverService.getInstance().removeObserver(this);
    }

    public static void loadTask(){
        tasks.clear();
        tasks.addAll(TextDAO.getInstance().getByStatus(Constant.STATUS.NORMAL));
        tasks.addAll(CheckListDAO.getInstance().getByStatus(Constant.STATUS.NORMAL));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadTask();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("account", Context.MODE_PRIVATE);
        accountId = sharedPreferences.getString("account_id", "");
    }
}
