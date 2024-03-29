package com.example.colornote.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.colornote.R;
import com.example.colornote.adapter.ViewAdapter;
import com.example.colornote.adapter.ViewListAdapter;
import com.example.colornote.dao.CheckListDAO;
import com.example.colornote.dao.ItemCheckListDAO;
import com.example.colornote.dao.TextDAO;
import com.example.colornote.fragment.HomeFragment;
import com.example.colornote.mapper.TextMapper;
import com.example.colornote.model.Task;
import com.example.colornote.model.Text;
import com.example.colornote.util.Constant;
import com.example.colornote.util.ISeletectedObserver;
import com.example.colornote.util.SelectedObserverService;

import java.util.ArrayList;
import java.util.Collections;

public class TrashCanActivity extends AppCompatActivity implements ISeletectedObserver {
    ImageButton btnBackTrashCan, btnTrashCanTrashCan;
    Button btnSort_TrashCan, btnSortModified_TrashCan, btnSortCreated_TrashCan, btnSortAlphabeta_TrasCan, btnSortColor_TrashCan;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    LinearLayout btnRestore,btnDeletePermanently;
GridLayout barBottomTrashCanHidden;
RelativeLayout barTopTrashCanHidden;
Toolbar toolbar_TrashCan;
ImageView imgRangeTrashCanHidden,imgCloseTrashCanHidden;
TextView txtCountTrashCanHidden;

    ImageView imgItemTask;
    public static ViewAdapter adapter;
    static GridView gvListRemove;
    public static ArrayList<Task> tasks;
    CheckListDAO checkListDAO = CheckListDAO.getInstance();
    TextDAO textDAO = TextDAO.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash_can);
        addControls();
        addEvents(this);
        checkIconTrashCan();
    }
public void checkIconTrashCan(){
    if(tasks.size()==0){
        btnTrashCanTrashCan.setVisibility(View.INVISIBLE);
    }
}
    private void addEvents(Activity activity) {

        btnBackTrashCan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        builder = new AlertDialog.Builder(activity);
        btnTrashCanTrashCan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                builder.setTitle("Delete").setMessage("Are you sure you want to delete all notes in the trash can?").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                deleteAllTask();
                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();

                            }
                        });
                dialog = builder.create();
                dialog.show();
            }
        });
        btnSort_TrashCan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new AlertDialog.Builder(TrashCanActivity.this);
                LayoutInflater inflater = TrashCanActivity.this.getLayoutInflater();
                View view = inflater.inflate(R.layout.dialog_sort_trashcan, null);
                builder.setView(view).setTitle("Sort");
                dialog = builder.create();
                dialog.show();
                //sort by modified
                view.findViewById(R.id.btnSortModified_TrashCan).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Collections.sort(tasks, Task.compareByModifiedTime);
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
                //sort by created
                view.findViewById(R.id.btnSortCreated_TrashCan).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
//sort by A-B
                view.findViewById(R.id.btnSortAlphabeta_TrasCan).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Collections.sort(tasks, Task.compareByTitle);
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
//sort by color
                view.findViewById(R.id.btnSortColor_TrashCan).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Collections.sort(tasks, Task.compareByColor);
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
            }
        });

//select item
        imgCloseTrashCanHidden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectedObserverService.getInstance().reset();
                adapter.updateBorderView();
            }
        });

        imgRangeTrashCanHidden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectedObserverService.getInstance().selectRange();
                adapter.updateBorderView();
            }
        });

        btnDeletePermanently.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTask();
            }
        });
        btnRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restoreTask();
            }
        });

    }

    private void addControls() {
        barBottomTrashCanHidden = findViewById(R.id.barBottomTrashCanHidden);
        barTopTrashCanHidden = findViewById(R.id.barTopTrashCanHidden);
        toolbar_TrashCan =  findViewById(R.id.toolbar_TrashCan);
        imgRangeTrashCanHidden =  findViewById(R.id.imgRangeTrashCanHidden);
        imgCloseTrashCanHidden =  findViewById(R.id.imgCloseTrashCanHidden);
        txtCountTrashCanHidden =  findViewById(R.id.txtCountTrashCanHidden);
        btnRestore =  findViewById(R.id.btnRestore);
        btnDeletePermanently =  findViewById(R.id.btnDeletePermanently);



        btnBackTrashCan =  findViewById(R.id.btnBackTrashCan);
        btnTrashCanTrashCan =  findViewById(R.id.btnTrashCanTrashCan);
        btnSort_TrashCan =  findViewById(R.id.btnSort_TrashCan);
        gvListRemove =  findViewById(R.id.gvListRemove);

//       imgItemTask=findViewById(R.id.imgCheck);

        tasks = new ArrayList<>();
        loadTask();
        adapter = new ViewListAdapter(tasks, TrashCanActivity.this);
        Collections.sort(tasks, Task.compareByTitle);
        adapter.notifyDataSetChanged();
        gvListRemove.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        SelectedObserverService.getInstance().addObserver(this);

    }
    public void deleteAllTask() {

        for(int i = 0; i < tasks.size() ; i ++) {

                Task task = tasks.get(i);
                if(task.getClass().equals(Text.class)) TextDAO.getInstance().delete(task.getId());
                else {
                    ItemCheckListDAO.getInstance().deleteByIdCheckList(task.getId());
                    CheckListDAO.getInstance().delete(task.getId());
                }

        }
        loadTask();
        adapter.notifyDataSetChanged();
        checkIconTrashCan();
    }
    public void deleteTask() {

        boolean[] isSelected = SelectedObserverService.getInstance().getIsSelected();
        for(int i = 0; i < isSelected.length ; i ++) {
            if(isSelected[i]) {
                Task task = tasks.get(i);
                if(task.getClass().equals(Text.class)) TextDAO.getInstance().delete(task.getId());
                else {
                    ItemCheckListDAO.getInstance().deleteByIdCheckList(task.getId());
                    CheckListDAO.getInstance().delete(task.getId());
                }
            }
        }
        loadTask();
        adapter.notifyDataSetChanged();
        checkIconTrashCan();
    }
    public void restoreTask(){
        boolean[] isSelected = SelectedObserverService.getInstance().getIsSelected();
        for(int i = 0; i < isSelected.length ; i ++) {
            if(isSelected[i]) {
                Task task = tasks.get(i);
                if(task.getClass().equals(Text.class)) TextDAO.getInstance().changeStatus(task.getId(),Constant.STATUS.NORMAL);
                else {
                    ItemCheckListDAO.getInstance().changeStatus(task.getId(),Constant.STATUS.NORMAL);
                    CheckListDAO.getInstance().changeStatus(task.getId(),Constant.STATUS.NORMAL);
                }
            }
        }
        loadTask();
        adapter.notifyDataSetChanged();
        checkIconTrashCan();
    }
    public static void loadTask(){
        tasks.clear();
        tasks.addAll(TextDAO.getInstance().getByStatus(Constant.STATUS.RECYCLE_BIN));
        tasks.addAll(CheckListDAO.getInstance().getByStatus(Constant.STATUS.RECYCLE_BIN));
    }
    @Override
    public void update(SelectedObserverService s) {
        if(barTopTrashCanHidden.getVisibility() == View.VISIBLE && !s.hasSelected()){
            toolbar_TrashCan.setVisibility(View.VISIBLE);
            barTopTrashCanHidden.setVisibility(View.INVISIBLE);
barBottomTrashCanHidden.setVisibility(View.INVISIBLE);
        }
        if(barTopTrashCanHidden.getVisibility() == View.INVISIBLE && s.hasSelected()){
            toolbar_TrashCan.setVisibility(View.INVISIBLE);
            barTopTrashCanHidden.setVisibility(View.VISIBLE);
            barBottomTrashCanHidden.setVisibility(View.VISIBLE);
        }
        if(s.hasRange()){
            imgRangeTrashCanHidden.setImageResource(R.drawable.ic_range_active);
            imgRangeTrashCanHidden.setEnabled(true);
        }else{
            imgRangeTrashCanHidden.setImageResource(R.drawable.ic_range_nonactive);
            imgRangeTrashCanHidden.setEnabled(false);
        }
        txtCountTrashCanHidden.setText(s.getRatio());
    }


}