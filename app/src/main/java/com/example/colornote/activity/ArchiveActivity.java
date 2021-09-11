package com.example.colornote.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.colornote.R;
import com.example.colornote.adapter.ViewAdapter;
import com.example.colornote.adapter.ViewListAdapter;
import com.example.colornote.dao.CheckListDAO;
import com.example.colornote.dao.ColorDAO;
import com.example.colornote.dao.ItemCheckListDAO;
import com.example.colornote.dao.TextDAO;
import com.example.colornote.fragment.HomeFragment;
import com.example.colornote.mapper.CheckListMapper;
import com.example.colornote.mapper.ColorMapper;
import com.example.colornote.mapper.TextMapper;
import com.example.colornote.model.CheckList;
import com.example.colornote.model.Color;
import com.example.colornote.model.Task;
import com.example.colornote.model.Text;
import com.example.colornote.util.Constant;
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

    RelativeLayout barTopArchiveHidden;
    LinearLayout btn_change_color_archive,btn_delete_archive,btn_unarchive,bottom_bar_archive;
    ImageView imgRangeArchiveHidden, imgCloseArchiveHidden;

    TextView txtCountArchiveHidden;
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
        loadAllTask();
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
                        loadAllTask();
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
                view.findViewById(R.id.btn_text_archive).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btnNoteArchive.setImageResource(getResources().getIdentifier("@drawable/ic_text",null,getPackageName()));
                       loadTextTask();
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
                view.findViewById(R.id.btn_check_list_archive).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btnNoteArchive.setImageResource(getResources().getIdentifier("@drawable/ic_check_list",null,getPackageName()));
                        loadChecklistTask();
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
                String n =(String)parent.getItemAtPosition(position);
                if(n.equals("All")){
loadAllTask();
                }else if(n.equals("Notes")){
loadNotes();
                }else{
loadChecklistTask();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spnRight_Archive.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

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
        imgCloseArchiveHidden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectedObserverService.getInstance().reset();
                adapter.updateBorderView();
            }
        });

        imgRangeArchiveHidden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectedObserverService.getInstance().selectRange();
                adapter.updateBorderView();
            }
        });
        btn_unarchive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unarchiveTask();
            }
        });
        btn_delete_archive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTask();
            }
        });
        btn_change_color_archive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeColorTask();
            }
        });
    }

    public void addControls() {

        barTopArchiveHidden = findViewById(R.id.barTopArchiveHidden);

        imgRangeArchiveHidden =  findViewById(R.id.imgRangeArchiveHidden);
        imgCloseArchiveHidden =  findViewById(R.id.imgCloseArchiveHidden);
        txtCountArchiveHidden =  findViewById(R.id.txtCountArchiveHidden);

        imgEdit =  findViewById(R.id.imgEdit);
        imgNumber =  findViewById(R.id.imgNumber);

        btnBackArchive =  findViewById(R.id.btnBackArchive);
        btnNoteArchive =  findViewById(R.id.btnNoteArchive);
        btnColorArchive =  findViewById(R.id.btnColorArchive);
        spnLeft_Archive =  findViewById(R.id.spnLeft_Archive);
        spnRight_Archive =  findViewById(R.id.spnRight_Archive);
        btnSort_Archive =  findViewById(R.id.btnSort_Archive);
        bottom_bar_archive = findViewById(R.id.bottom_bar_archive);
        btn_change_color_archive = findViewById(R.id.btn_change_color_archive);
        btn_delete_archive =  findViewById(R.id.btn_delete_archive);
        btn_unarchive = findViewById(R.id.btn_unarchive);
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
        if(barTopArchiveHidden.getVisibility() == View.VISIBLE && !s.hasSelected()){

            barTopArchiveHidden.setVisibility(View.INVISIBLE);
            bottom_bar_archive.setVisibility(View.INVISIBLE);
        }
        if(barTopArchiveHidden.getVisibility() == View.INVISIBLE && s.hasSelected()){

            barTopArchiveHidden.setVisibility(View.VISIBLE);
            bottom_bar_archive.setVisibility(View.VISIBLE);
        }
        if(s.hasRange()){
            imgRangeArchiveHidden.setImageResource(R.drawable.ic_range_active);
            imgRangeArchiveHidden.setEnabled(true);
        }else{
            imgRangeArchiveHidden.setImageResource(R.drawable.ic_range_nonactive);
            imgRangeArchiveHidden.setEnabled(false);
        }
        txtCountArchiveHidden.setText(s.getRatio());
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
    public void unarchiveTask(){
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
        loadAllTask();
        adapter.notifyDataSetChanged();

    }
    public void deleteTask() {

        boolean[] isSelected = SelectedObserverService.getInstance().getIsSelected();
        for(int i = 0; i < isSelected.length ; i ++) {
            if(isSelected[i]) {
                Task task = tasks.get(i);
                if(task.getClass().equals(Text.class)) TextDAO.getInstance().changeStatus(task.getId(),Constant.STATUS.RECYCLE_BIN);
                else {
                    ItemCheckListDAO.getInstance().changeStatus(task.getId(),Constant.STATUS.RECYCLE_BIN);
                    CheckListDAO.getInstance().changeStatus(task.getId(),Constant.STATUS.RECYCLE_BIN);
                }
            }
        }
        loadAllTask();
        adapter.notifyDataSetChanged();

    }
    public void changeColorTask() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.layout_choose_color);
        dialog.show();
        GridLayout gvColor = dialog.findViewById(R.id.glColor);
        List<com.example.colornote.model.Color> colors = ColorDAO.getInstance().getAll(new ColorMapper());
        colors.remove(0);
        for(com.example.colornote.model.Color c: colors) {
            Button btn = new Button(this);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
                params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            }
            btn.setLayoutParams(params);
            btn.setBackgroundColor(android.graphics.Color.parseColor(c.getColorMain()));
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean[] isSelected = SelectedObserverService.getInstance().getIsSelected();
                    for(int i = 0; i < isSelected.length ; i ++) {
                        if(isSelected[i]) {
                            Task task = tasks.get(i);
                            task.setColorId(c.getId());
                            if(task.getClass().equals(Text.class)) TextDAO.getInstance().update((Text) task);
                            else {
                                CheckListDAO.getInstance().update((CheckList) task);
                            }
                        }
                    }
                    loadAllTask();
                    adapter.notifyDataSetChanged();
                    dialog.cancel();
                }
            });
            gvColor.addView(btn);
        }
    }
    public static void loadAllTask(){
        tasks.clear();
        tasks.addAll(TextDAO.getInstance().getByStatus(Constant.STATUS.ARCHIVE));
        tasks.addAll(CheckListDAO.getInstance().getByStatus(Constant.STATUS.ARCHIVE));
    }
    public static void loadTextTask(){
        tasks.clear();
        tasks.addAll(TextDAO.getInstance().getByStatus(Constant.STATUS.ARCHIVE));

    }
    public static void loadChecklistTask(){
        tasks.clear();
        tasks.addAll(CheckListDAO.getInstance().getByStatus(Constant.STATUS.ARCHIVE));
    }public  void loadCalendar(){
        tasks.clear();
        tasks.addAll(CheckListDAO.getInstance().getCalendarCheckList());
        tasks.addAll(TextDAO.getInstance().getCalendarText());
    }
    public  void loadNotes(){
        tasks.clear();
        tasks.addAll(CheckListDAO.getInstance().getNoteCheckList());
        tasks.addAll(TextDAO.getInstance().getNoteText());
    }
}