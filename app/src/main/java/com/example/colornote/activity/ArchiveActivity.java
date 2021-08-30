package com.example.colornote.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.colornote.R;
import com.example.colornote.adapter.ViewAdapter;
import com.example.colornote.adapter.ViewListAdapter;
import com.example.colornote.dao.CheckListDAO;
import com.example.colornote.dao.ColorDAO;
import com.example.colornote.dao.TextDAO;
import com.example.colornote.fragment.HomeFragment;
import com.example.colornote.fragment.MoreFragment;
import com.example.colornote.mapper.CheckListMapper;
import com.example.colornote.mapper.ColorMapper;
import com.example.colornote.mapper.TextMapper;
import com.example.colornote.model.Color;
import com.example.colornote.model.Task;
import com.example.colornote.util.ISeletectedObserver;
import com.example.colornote.util.SelectedObserverService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArchiveActivity extends AppCompatActivity implements ISeletectedObserver {
    ImageButton btnBackArchive, btnNoteArchive, btnColorArchive;
    AlertDialog.Builder builder;
    Spinner spnLeft_Archive,spnRight_Archive;
    Button btnSort_Archive;
    public static ViewAdapter adapter;
    static GridView gvListArchive;
    public static ArrayList<Task> tasks;
    CheckListDAO checkListDAO = CheckListDAO.getInstance();
    TextDAO textDAO = TextDAO.getInstance();
    Dialog dialogEditColor;
    ImageView imgEdit,imgNumber;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);
        addControls();
        addEvents(this);
    }

    private void addEvents(Activity activity) {
        btnBackArchive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnNoteArchive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               builder = new AlertDialog.Builder(activity);
                LayoutInflater inflater = activity.getLayoutInflater();
                View view =inflater.inflate(R.layout.dialog_type_note,null);
                builder.setView(view).setTitle("Note Type");
                AlertDialog dialog = builder.create();
                dialog.show();
                view.findViewById(R.id.btn_All_note_archive).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btnNoteArchive.setImageResource(getResources().getIdentifier("@drawable/ic_square",null,getPackageName()));
                        tasks.clear();
                        tasks.addAll(textDAO.getAll(new TextMapper()));
                        tasks.addAll(checkListDAO.getByStatus(1));
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
                view.findViewById(R.id.btn_text_archive).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btnNoteArchive.setImageResource(getResources().getIdentifier("@drawable/ic_text",null,getPackageName()));
                        tasks.clear();
                        tasks.addAll(textDAO.getAll(new TextMapper()));
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
                view.findViewById(R.id.btn_check_list_archive).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btnNoteArchive.setImageResource(getResources().getIdentifier("@drawable/ic_check_list",null,getPackageName()));
                        tasks.clear();
                        tasks.addAll(checkListDAO.getByStatus(1));
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
            }
        });

        //spinner
        spnLeft_Archive.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(parent.getContext(), parent.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spnRight_Archive.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(parent.getContext(), parent.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //button sort
        btnSort_Archive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ArchiveActivity.this);
                LayoutInflater inflater = ArchiveActivity.this.getLayoutInflater();
                View view =inflater.inflate(R.layout.dialog_sort_archive,null);
                builder.setView(view).setTitle("Sort");
                AlertDialog dialog = builder.create();
                dialog.show();

                //sort by modified
                view.findViewById(R.id.btnSortModified_Archive).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Collections.sort(tasks, Task.compareByModifiedTime);
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
                //sort by created
                view.findViewById(R.id.btnSortCreated_Archive).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
//sort by A-B
                view.findViewById(R.id.btnSortAlphabeta_Archive).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Collections.sort(tasks, Task.compareByTitle);
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
//sort by color
                view.findViewById(R.id.btnSortColor_Archive).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Collections.sort(tasks, Task.compareByColor);
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
            }
        });
        //
        btnColorArchive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogEditColor();
            }
        });
    }

    public void addControls() {
        imgEdit = (ImageView) findViewById(R.id.imgEdit);
        imgNumber = (ImageView) findViewById(R.id.imgNumber);

        btnBackArchive = (ImageButton) findViewById(R.id.btnBackArchive);
        btnNoteArchive = (ImageButton) findViewById(R.id.btnNoteArchive);
        btnColorArchive = (ImageButton) findViewById(R.id.btnColorArchive);
        spnLeft_Archive = (Spinner) findViewById(R.id.spnLeft_Archive);
        spnRight_Archive = (Spinner) findViewById(R.id.spnRight_Archive);
        btnSort_Archive = (Button) findViewById(R.id.btnSort_Archive);

        gvListArchive = (GridView) findViewById(R.id.gvListArchive);
        tasks = new ArrayList<>();
        tasks.addAll(textDAO.getAll(new TextMapper()));
        tasks.addAll(checkListDAO.getByStatus(1));
        adapter = new ViewListAdapter(tasks, ArchiveActivity.this);
        Collections.sort(tasks, Task.compareByTitle);
        adapter.notifyDataSetChanged();
        gvListArchive.setAdapter(adapter);

        SelectedObserverService.getInstance().addObserver(this);

    }
    public void showDialogEditColor(){
        dialogEditColor = new Dialog(this);
        dialogEditColor.setContentView(R.layout.layout_color_edit);
        imgEdit= dialogEditColor.findViewById(R.id.imgEdit);
        imgNumber = dialogEditColor.findViewById(R.id.imgNumber);
        GridLayout glColor = dialogEditColor.findViewById(R.id.glColor);
        addItemColorToGridLayout(dialogEditColor, this, glColor, false, false);
        dialogEditColor.show();

        imgNumber.setOnClickListener(new View.OnClickListener() {
            boolean isShowAmount = false;
            @Override
            public void onClick(View v) {
                isShowAmount = !isShowAmount;

                addItemColorToGridLayout(dialogEditColor, ArchiveActivity.this, glColor, false, isShowAmount);
            }
        });

        imgEdit.setOnClickListener(new View.OnClickListener() {
            boolean isEdit = false;
            @Override
            public void onClick(View v) {
                isEdit = !isEdit;
                addItemColorToGridLayout(dialogEditColor, ArchiveActivity.this, glColor, isEdit, false);
            }
        });

    }
    @Override
    public void update(SelectedObserverService s) {

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
                        tasks.clear();
                        tasks.addAll(CheckListDAO.getInstance().getAll(new CheckListMapper()));
                        tasks.addAll(TextDAO.getInstance().getAll(new TextMapper()));
                        if(color.getId() != 1){
                            tasks.removeIf(task -> task.getColorId() != color.getId());
                        }
                        adapter.notifyDataSetChanged();
                        dialogEditColor.dismiss();
                        HomeFragment.btnSort.setBackgroundColor(android.graphics.Color.parseColor(color.getColorMain() == null ? "#F6F6F6" : color.getColorMain()));
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
    public void rename(Color color){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Rename");
        final EditText input = new EditText(this);
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