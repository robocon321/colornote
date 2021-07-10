package com.example.colornote.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.colornote.R;
import com.example.colornote.adapter.ViewAdapter;
import com.example.colornote.adapter.ViewListAdapter;
import com.example.colornote.dao.CheckListDAO;
import com.example.colornote.dao.TextDAO;
import com.example.colornote.fragment.HomeFragment;
import com.example.colornote.mapper.TextMapper;
import com.example.colornote.model.Task;
import com.example.colornote.util.ISeletectedObserver;
import com.example.colornote.util.SelectedObserverService;

import java.util.ArrayList;
import java.util.Collections;

public class TrashCanActivity extends AppCompatActivity implements ISeletectedObserver {
    ImageButton btnBackTrashCan, btnTrashCanTrashCan;
    Button btnSort_TrashCan, btnSortModified_TrashCan, btnSortCreated_TrashCan, btnSortAlphabeta_TrasCan, btnSortColor_TrashCan;
    AlertDialog.Builder builder;
    AlertDialog dialog;
GridLayout barBottomTrashCanHidden;
RelativeLayout barTopTrashCanHidden;
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
                tasks.clear();
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


    }

    private void addControls() {
        barBottomTrashCanHidden = (GridLayout) findViewById(R.id.barBottomTrashCanHidden);
        barTopTrashCanHidden = (RelativeLayout) findViewById(R.id.barTopTrashCanHidden);

        btnBackTrashCan = (ImageButton) findViewById(R.id.btnBackTrashCan);
        btnTrashCanTrashCan = (ImageButton) findViewById(R.id.btnTrashCanTrashCan);
        btnSort_TrashCan = (Button) findViewById(R.id.btnSort_TrashCan);
        gvListRemove = (GridView) findViewById(R.id.gvListRemove);

//       imgItemTask=findViewById(R.id.imgCheck);

        tasks = new ArrayList<>();
        tasks.addAll(textDAO.getAll(new TextMapper()));
        tasks.addAll(checkListDAO.getByStatus(1));
        adapter = new ViewListAdapter(tasks, TrashCanActivity.this);
        Collections.sort(tasks, Task.compareByTitle);
        adapter.notifyDataSetChanged();
        gvListRemove.setAdapter(adapter);


    }

    @Override
    public void update(SelectedObserverService s) {

    }
}