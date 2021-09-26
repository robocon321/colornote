package com.example.colornote.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.colornote.R;
import com.example.colornote.adapter.CheckListAdapter;
import com.example.colornote.dao.CheckListDAO;
import com.example.colornote.dao.ItemCheckListDAO;
import com.example.colornote.model.CheckList;
import com.example.colornote.model.ItemCheckList;
import com.example.colornote.util.Constant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Calendar_Checklist_Activity extends AppCompatActivity {

    private int colorid;
    EditText title_checklist;
    TextView date_checklist;
    Toolbar toolbar;
    boolean checkIcon = true;
    Button button_additem;
    RecyclerView recyclerView;
    ArrayList<String> list = new ArrayList<>();
    CheckListAdapter checkListAdapter;
    private long numEdit = 0;
    CheckList checkList = new CheckList();
    ItemCheckList itemCheckList = new ItemCheckList();
    int listItemSize,parentId;
    LinearLayout linearLayout;
    int color_black =1;
    int num_click = 0;

    SharedPreferences sharedPreferences;
    String themeName;
    private Date date;

    AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("Theme", Context.MODE_PRIVATE);
        themeName = sharedPreferences.getString("ThemeName", "Default");
        setContentView(R.layout.activity_check_list);

        toolbar = findViewById(R.id.toolbar_checklist);
        title_checklist = findViewById(R.id.title_checklist);
        date_checklist = findViewById(R.id.date_checklist);
        button_additem = findViewById(R.id.btn_additem);
        linearLayout = findViewById(R.id.layout_checkList);
        linearLayout.setBackgroundColor(Color.parseColor("#ffe77a"));
        button_additem.setBackgroundColor(Color.parseColor("#ffe77a"));
        Constant.num_edit = 1;
        title_checklist.setVisibility(View.VISIBLE);
        button_additem.setVisibility(View.VISIBLE);

        if(themeName.equalsIgnoreCase("Dark")){
            button_additem.setBackgroundColor(Color.parseColor("#000000"));
            linearLayout.setBackgroundColor(Color.parseColor("#000000"));
        }else{
            linearLayout.setBackgroundColor(Color.parseColor("#ffe77a"));
            button_additem.setBackgroundColor(Color.parseColor("#ffe77a"));
        }


        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
        String s = simpleDateFormat.format(date);
        date_checklist.setText(s);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_list) ;
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Calendar_Checklist_Activity.this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);

        checkListAdapter = new CheckListAdapter(list,Calendar_Checklist_Activity.this);
        recyclerView.setAdapter(checkListAdapter);
        checkListAdapter.notifyDataSetChanged();

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setTitle("Title CheckList");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_check_24);
//      edit item in home
        if(Constant.num_click==1) {
            if (getIntent().getExtras() != null) {
                num_click = 1;
                checkList = (CheckList) getIntent().getExtras().get("checkList");
                title_checklist.setText(checkList.getTitle());
                actionBar.setTitle(checkList.getTitle());
                colorid = checkList.getColorId();
                date_checklist.setText(simpleDateFormat.format(checkList.getModifiedDate()));
                String colorSub = getIntent().getStringExtra("colorSub");
                if(colorSub.equals("#000000")) {
//                    changeIconToolBar(1);
                }else{
//                    changeIconToolBar(0);
                }
//                Toast.makeText(Calendar_Checklist_Activity.this, colorSub, Toast.LENGTH_LONG).show();
//                Drawable colorDrawable = new ColorDrawable(Color.parseColor(colorSub));

                actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(colorSub)));

                if(themeName.equalsIgnoreCase("Dark")){
                    button_additem.setBackgroundColor(Color.parseColor("#000000"));
                    linearLayout.setBackgroundColor(Color.parseColor("#000000"));
                }else{
                    button_additem.setBackgroundColor(Color.parseColor(getIntent().getStringExtra("colorMain")));
                    linearLayout.setBackgroundColor(Color.parseColor(getIntent().getStringExtra("colorMain")));
                }

                //get item checklist
                ItemCheckListDAO itemCheckListDAO = ItemCheckListDAO.getInstance();
                List<ItemCheckList> listItem = new ArrayList<>();
                listItem = itemCheckListDAO.getByParentId(checkList.getId());
                for(int i = 0;i<listItem.size();i++){
                    list.add(listItem.get(i).getContent());
                }
                parentId = checkList.getId();
                Constant.num_click = 0;

                numEdit = 1;
                checkIcon = false;
                if (color_black == 0) {
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24_w);
                } else {
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
                }
                Constant.num_edit = 0;
                title_checklist.setVisibility(View.GONE);
                button_additem.setVisibility(View.GONE);
            } else {
                Toast.makeText(Calendar_Checklist_Activity.this, "null", Toast.LENGTH_LONG).show();
            }
        }

        title_checklist.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus &&!title_checklist.getText().toString().equals("")){
                    getSupportActionBar().setTitle(title_checklist.getText().toString());
                }else{
                    checkIcon = true;
                    if(color_black==0){
                        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_check_24_w);
                    }else{
                        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_check_24);
                    }
                }
            }
        });
        button_additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(color_black==0){
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_check_24_w);
                }else{
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_check_24);
                }

                getSupportActionBar().setTitle(title_checklist.getText().toString());
                title_checklist.clearFocus();
                Dialog dialog = new Dialog(Calendar_Checklist_Activity.this);
                dialog.setContentView(R.layout.dialog_additem_checklist);
                EditText editTextitem;
                Button button_ok,button_exit;
                editTextitem = (EditText) dialog.findViewById(R.id.edtext_item);
                button_ok = (Button) dialog.findViewById(R.id.btn_ok);
                // button_exit = (Button) dialog.findViewById(R.id.btn_exit);

                button_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String text = editTextitem.getText().toString();
                        if(!text.equals("")){
                            list.add(text);
//                            Toast.makeText(Calendar_Checklist_Activity.this,""+list.size(),Toast.LENGTH_LONG).show();
                            checkListAdapter = new CheckListAdapter(list,Calendar_Checklist_Activity.this);
                            recyclerView.setAdapter(checkListAdapter);
                            dialog.dismiss();
                        }else{
                            Toast.makeText(Calendar_Checklist_Activity.this,"Ban chua nhap noi dung",Toast.LENGTH_LONG).show();
                        }
                    }
                });
//                button_exit.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
                dialog.show();
                checkIcon=true;
                Constant.num_edit = 1;
                checkListAdapter.notifyDataSetChanged();
                title_checklist.setVisibility(View.VISIBLE);
                button_additem.setVisibility(View.VISIBLE);
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.text_checklist_menu,menu);

        MenuItem searchItem = menu.findItem(R.id.find);

        final androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(Calendar_Checklist_Activity.this,query,Toast.LENGTH_LONG).show();
                searchView.clearFocus();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("aaaaa",newText);
//                checkListAdapter = new CheckListAdapter(list,Calendar_Checklist_Activity.this);
                checkListAdapter.getFilter().filter(newText);
                if(newText.equals("")){
                    checkListAdapter = new CheckListAdapter(list,Calendar_Checklist_Activity.this);
                    recyclerView.setAdapter(checkListAdapter);
                }
                return false;
            }
        });
//         return super.onCreateOptionsMenu(menu);


        menu.findItem(R.id.mnCheck).setTitle(checkList.completeAll() ? "Uncheck" : "Check");
        menu.findItem(R.id.mnCheck).setIcon(checkList.completeAll() ? R.drawable.ic_square : R.drawable.ic_check);
        return super.onCreateOptionsMenu(menu);
//         return true;

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                if(checkIcon==false) {
                    onBackPressed();

                }
                else{
                    if(color_black==0){
                        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24_w);
                    }else{
                        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
                    }
                    if(numEdit==0) {
                        addItemCheckList();
                        addCheckList(colorid);
                    }else{
                        removeAllItemListDAO();
                        editItemChecklist();
                        editCheckList(colorid);
                    }
                    Toast.makeText(Calendar_Checklist_Activity.this,"Saved",Toast.LENGTH_LONG).show();
                    getSupportActionBar().setTitle(title_checklist.getText().toString());
                    title_checklist.clearFocus();
                    closekeyboard();
                    checkIcon =false;
                    Constant.num_edit = 0;
                    checkListAdapter.notifyDataSetChanged();
                    title_checklist.setVisibility(View.GONE);
                    button_additem.setVisibility(View.GONE);
                }
                return true;
            case R.id.edit:
                if(color_black==0){
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_check_24_w);
                }else{
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_check_24);
                }
                title_checklist.requestFocus();
                checkIcon = true;
                getSupportActionBar().setTitle(title_checklist.getText().toString());
                Constant.num_edit = 1;
                checkListAdapter.notifyDataSetChanged();
                title_checklist.setVisibility(View.VISIBLE);
                button_additem.setVisibility(View.VISIBLE);
                return  true;
            case R.id.color:
                Dialog dialog = new Dialog(Calendar_Checklist_Activity.this);
                dialog.setContentView(R.layout.dialog_color);
                Button button_red,button_orange,button_yellow,button_green,button_blue,
                        button_purple,button_black,button_gray,button_white_gray;
                button_red = dialog.findViewById(R.id.btn_yellow);
                button_black = dialog.findViewById(R.id.btn_blue_1);
                button_orange = dialog.findViewById(R.id.btn_orange);
                button_yellow = dialog.findViewById(R.id.btn_red);
                button_green = dialog.findViewById(R.id.btn_green);
                button_blue = dialog.findViewById(R.id.btn_blue);
                button_purple = dialog.findViewById(R.id.btn_purple);
                button_gray = dialog.findViewById(R.id.btn_gray);
                button_white_gray = dialog.findViewById(R.id.btn_white);

                changeColorActionbar(button_red,dialog,4,1,"#f7cad0");
                changeColorActionbar(button_black,dialog,8,0,"#adb5bd");
                changeColorActionbar(button_orange,dialog,3,1,"#FFEAD7");
                changeColorActionbar(button_yellow,dialog,2,1,"#fff2b2");
                changeColorActionbar(button_green,dialog,5,1,"#b7efc5");
                changeColorActionbar(button_purple,dialog,7,1,"#dec9e9");
                changeColorActionbar(button_gray,dialog,9,1,"#dee2e6");
                changeColorActionbar(button_blue,dialog,6,1,"#caf0f8");
                changeColorActionbar(button_white_gray,dialog,10,1,"#ffffff");

                dialog.show();
                Constant.num_edit = 1;
                checkListAdapter.notifyDataSetChanged();
                title_checklist.setVisibility(View.VISIBLE);
                button_additem.setVisibility(View.VISIBLE);
                if(color_black==0){
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_check_24_w);
                }else{
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_check_24);
                }
                title_checklist.requestFocus();
                checkIcon = true;
                return true;
            case R.id.mnCheck:
                checkCheckList(item);
                return true;
            case R.id.mnSend:
                sendTask();
                return true;
            case R.id.mnReminder:
                changeReminderActivitiy();
                return true;
            case R.id.mnArchive:
                archiveTask();
                return true;
            case R.id.mnDelete:
                deleteTask();
                return true;
            default:break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void deleteTask() {
        ItemCheckListDAO.getInstance().changeStatus(checkList.getId(), Constant.STATUS.RECYCLE_BIN);
        CheckListDAO.getInstance().changeStatus(checkList.getId(), Constant.STATUS.RECYCLE_BIN);
        onBackPressed();
    }

    public void archiveTask() {
        CheckListDAO.getInstance().changeStatus(checkList.getId(), Constant.STATUS.ARCHIVE);
        onBackPressed();
    }

    public void changeReminderActivitiy(){
        Intent intent = new Intent(this, ReminderActivity.class);
        intent.putExtra("task", checkList);
        startActivity(intent);
    }

    public void sendTask() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, checkList.getTitle() + "\n" + checkList.showContent());
        this.startActivity(Intent.createChooser(intent, "Share tasks"));
    }

    public void checkCheckList(MenuItem item) {
        boolean isCompleted = !checkList.completeAll();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(!checkList.completeAll()) {
            builder.setTitle("Check all items");
            builder.setMessage("Are you sure you want to check all items?");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int index) {
                    List<ItemCheckList> items = ItemCheckListDAO.getInstance().getByParentId(checkList.getId());
                    CheckListDAO.getInstance().changeCompleted(checkList.getId(), isCompleted);
                    for (ItemCheckList item : items) {
                        ItemCheckListDAO.getInstance().changeCompleted(item.getId(), isCompleted);
                        CheckListAdapter checkListAdapter = new CheckListAdapter(list,Calendar_Checklist_Activity.this);
                        recyclerView.setAdapter(checkListAdapter);
                    }
                    checkList.setCompleted(isCompleted);
                    item.setTitle(checkList.completeAll() ? "Uncheck" : "Check");
                    item.setIcon(checkList.completeAll() ? R.drawable.ic_square : R.drawable.ic_check);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int index) {
                    dialog.dismiss();
                }
            });
        } else {
            builder.setTitle("Uncheck all items");
            builder.setMessage("Are you sure you want to uncheck all items?");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int index) {
                    List<ItemCheckList> items = ItemCheckListDAO.getInstance().getByParentId(checkList.getId());
                    CheckListDAO.getInstance().changeCompleted(checkList.getId(), isCompleted);
                    for (ItemCheckList item : items) {
                        ItemCheckListDAO.getInstance().changeCompleted(item.getId(), isCompleted);
                        CheckListAdapter checkListAdapter = new CheckListAdapter(list,Calendar_Checklist_Activity.this);
                        recyclerView.setAdapter(checkListAdapter);
                    }
                    checkList.setCompleted(isCompleted);
                    item.setTitle(checkList.completeAll() ? "Uncheck" : "Check");
                    item.setIcon(checkList.completeAll() ? R.drawable.ic_square : R.drawable.ic_check);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int index) {
                    dialog.dismiss();
                }
            });
        }
        dialog = builder.create();
        dialog.show();
    }
    public void changeColorActionbar(Button button,Dialog dialog,int color,int colorWB, String colorBackground){

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable drawable = button.getBackground();
                ActionBar actionBar = getSupportActionBar();
                actionBar.setBackgroundDrawable(drawable);
                color_black=colorWB;
                changeIconToolBar(color_black);
                if(!themeName.equalsIgnoreCase("Dark")){
                    linearLayout.setBackgroundColor(Color.parseColor(colorBackground));
                    button_additem.setBackgroundColor(Color.parseColor(colorBackground));
                }
                if(themeName.equalsIgnoreCase("Dark")){
                    recyclerView.setBackgroundColor(Color.parseColor("#000000"));
                }else{
                    recyclerView.setBackgroundColor(Color.parseColor(colorBackground));
                }


                colorid = color;
                dialog.cancel();

            }
        });
    }
    public boolean addCheckList(int color){
        Date date = getDateFromCalendarFragment();
        CheckList checkList = new CheckList();
        CheckListDAO checkListDAO = CheckListDAO.getInstance();
        checkList.setId(checkListDAO.count()+1);
        checkList.setTitle(title_checklist.getText().toString());
        checkList.setReminderId(1);
        checkList.setColorId(color);
        checkList.setModifiedDate(date);
        checkList.setStatus(Constant.STATUS.NORMAL);
        numEdit = checkListDAO.insert(checkList);
//        Log.d("a",checkListDAO.insert(checkList)+"");

        return false;
    }
    public void addItemCheckList() {
        CheckListDAO checkListDAO = CheckListDAO.getInstance();

        for (int i = 0; i < list.size(); i++) {
//            ItemCheckList itemCheckList = new ItemCheckList();
            ItemCheckListDAO itemCheckListDAO = ItemCheckListDAO.getInstance();
            itemCheckList.setId(itemCheckListDAO.count()+1);
            itemCheckList.setContent(list.get(i));
            Date date = getDateFromCalendarFragment();
            itemCheckList.setModifiedDate(date);
            itemCheckList.setParentId(checkListDAO.count()+1);
            itemCheckList.setStatus(Constant.STATUS.NORMAL);
            itemCheckListDAO.insert(itemCheckList);
        }
        listItemSize = list.size();
        parentId = checkListDAO.count()+1;
    }
    public boolean editCheckList(int color){
        CheckListDAO checkListDAO = CheckListDAO.getInstance();
        checkList.setTitle(title_checklist.getText().toString());
        checkList.setReminderId(1);
        checkList.setColorId(color);
        Date date = Calendar.getInstance().getTime();
        checkList.setModifiedDate(date);
        checkList.setStatus(Constant.STATUS.NORMAL);
        checkListDAO.update(checkList);
        return true;
    }
    public void removeAllItemListDAO(){
//            list.removeAll(list);
        ItemCheckListDAO itemCheckListDAO = ItemCheckListDAO.getInstance();
        itemCheckListDAO.deleteByIdCheckList(parentId);
    }
    public void editItemChecklist(){
        for (int i = 0; i < list.size(); i++) {
//
            ItemCheckListDAO itemCheckListDAO = ItemCheckListDAO.getInstance();
            itemCheckList.setId(itemCheckListDAO.count() + 1);
            itemCheckList.setContent(list.get(i));
            Date date = Calendar.getInstance().getTime();
            itemCheckList.setModifiedDate(date);
            itemCheckList.setParentId(parentId);
            itemCheckList.setStatus(Constant.STATUS.NORMAL);
            itemCheckListDAO.insert(itemCheckList);
        }
    }
    public void closekeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    public void changeIconToolBar(int color){
        if(color==0){
            toolbar.getOverflowIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.getMenu().getItem(0).setIcon(R.drawable.ic_baseline_edit_24);
            if(checkIcon==false)
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24_w);
            else{
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_check_24_w);
            }
        }else{
            toolbar.setTitleTextColor(Color.BLACK);
            toolbar.getOverflowIcon().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
            toolbar.getMenu().getItem(0).setIcon(R.drawable.ic_baseline_edit_24_black);
            if(checkIcon==false)
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
            else{
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_check_24);
            }
        }
    }
    protected void onResume() {
        super.onResume();
        if(num_click==0) {
            SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
            int color = pre.getInt("default_color", 0xFFF7D539);
            ActionBar actionBar = getSupportActionBar();
            actionBar.setBackgroundDrawable(new ColorDrawable(color));
            if(color== ContextCompat.getColor(this, R.color.yellow_custom)){
                linearLayout.setBackgroundColor(Color.parseColor("#fff2b2"));
                button_additem.setBackgroundColor(Color.parseColor("#fff2b2"));
                colorid=2;
            }else if(color==ContextCompat.getColor(this, R.color.orange_custom)){
                linearLayout.setBackgroundColor(Color.parseColor("#FFEAD7"));
                button_additem.setBackgroundColor(Color.parseColor("#FFEAD7"));
                colorid=3;
            }else if(color==ContextCompat.getColor(this, R.color.red_custom)){
                linearLayout.setBackgroundColor(Color.parseColor("#f7cad0"));
                button_additem.setBackgroundColor(Color.parseColor("#f7cad0"));
                colorid=4;
            }else if(color==ContextCompat.getColor(this, R.color.green_custom)){
                linearLayout.setBackgroundColor(Color.parseColor("#b7efc5"));
                button_additem.setBackgroundColor(Color.parseColor("#b7efc5"));
                colorid=5;
            }else if(color==ContextCompat.getColor(this, R.color.blue_custom)){
                linearLayout.setBackgroundColor(Color.parseColor("#caf0f8"));
                button_additem.setBackgroundColor(Color.parseColor("#caf0f8"));
                colorid=6;
            }else if(color==ContextCompat.getColor(this, R.color.purple_custom)){
                linearLayout.setBackgroundColor(Color.parseColor("#dec9e9"));
                button_additem.setBackgroundColor(Color.parseColor("#dec9e9"));
                colorid=7;
            }else if(color==ContextCompat.getColor(this, R.color.black_custom)){
                linearLayout.setBackgroundColor(Color.parseColor("#adb5bd"));
                button_additem.setBackgroundColor(Color.parseColor("#adb5bd"));
                colorid=8;
            }else if(color==ContextCompat.getColor(this, R.color.gray_custom)){
                linearLayout.setBackgroundColor(Color.parseColor("#dee2e6"));
                button_additem.setBackgroundColor(Color.parseColor("#dee2e6"));
                colorid=9;
            }else if(color==ContextCompat.getColor(this, R.color.white)){
                linearLayout.setBackgroundColor(Color.parseColor("#ffffff"));
                button_additem.setBackgroundColor(Color.parseColor("#ffffff"));
                colorid=10;
            }
            if(themeName.equalsIgnoreCase("Dark")){
                button_additem.setBackgroundColor(Color.parseColor("#000000"));
                linearLayout.setBackgroundColor(Color.parseColor("#000000"));
            }
        }
    }

    private Date getDateFromCalendarFragment() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        if(bundle == null){
            return Calendar.getInstance().getTime();
        }
        Date date = new Date();
        String data = bundle.getString("date");
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(data);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}