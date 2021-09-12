package com.example.colornote.activity;


import android.app.Dialog;

import android.content.Context;

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

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import androidx.preference.PreferenceManager;


import com.example.colornote.R;
import com.example.colornote.adapter.CheckListAdapter;
import com.example.colornote.dao.CheckListDAO;
import com.example.colornote.dao.ItemCheckListDAO;
import com.example.colornote.model.CheckList;
import com.example.colornote.model.ItemCheckList;
import com.example.colornote.model.Text;
import com.example.colornote.util.Constant;

import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CheckList_Activity extends AppCompatActivity {
    private int colorid;
    EditText title_checklist;
    TextView date_checklist;
    Toolbar toolbar;
    boolean checkIcon = true;
    Button button_additem;
    RecyclerView recyclerView;
    ArrayList<String> list = new ArrayList<>();
    private long numEdit = 0;
    CheckList checkList = new CheckList();
    ItemCheckList itemCheckList = new ItemCheckList();
    int listItemSize,parentId;
    LinearLayout linearLayout;
    int color_black =1;

    private Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);
        this.colorid = 2;
        toolbar = findViewById(R.id.toolbar_checklist);
        title_checklist = findViewById(R.id.title_checklist);
        date_checklist = findViewById(R.id.date_checklist);
        button_additem = findViewById(R.id.btn_additem);
        linearLayout = findViewById(R.id.layout_checkList);
        linearLayout.setBackgroundColor(Color.parseColor("#ffe77a"));
        button_additem.setBackgroundColor(Color.parseColor("#ffe77a"));
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
        String s = simpleDateFormat.format(date);
        date_checklist.setText(s);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_list) ;
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CheckList_Activity.this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);

        CheckListAdapter checkListAdapter = new CheckListAdapter(list,CheckList_Activity.this);
        recyclerView.setAdapter(checkListAdapter);



        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Title CheckList");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_check_24);
//

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
                Dialog dialog = new Dialog(CheckList_Activity.this);
                dialog.setContentView(R.layout.dialog_additem_checklist);
                EditText editTextitem;
                Button button_ok,button_exit;
                editTextitem = (EditText) dialog.findViewById(R.id.edtext_item);
                button_ok = (Button) dialog.findViewById(R.id.btn_ok);
                button_exit = (Button) dialog.findViewById(R.id.btn_exit);

                button_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String text = editTextitem.getText().toString();
                        if(!text.equals("")){
                            list.add(text);
//                            Toast.makeText(CheckList_Activity.this,""+list.size(),Toast.LENGTH_LONG).show();
                            CheckListAdapter checkListAdapter = new CheckListAdapter(list,CheckList_Activity.this);
                            recyclerView.setAdapter(checkListAdapter);
                            dialog.dismiss();
                        }else{
                            Toast.makeText(CheckList_Activity.this,"Ban chua nhap noi dung",Toast.LENGTH_LONG).show();
                        }
                    }
                });
                button_exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                checkIcon=true;
            }
        });

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        String data = bundle.getString("date");
        try {
            date = new SimpleDateFormat("dd-MM-yyyy").parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "" + date, Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.text_checklist_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                if(checkIcon==false)
                    onBackPressed();
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
                    Toast.makeText(CheckList_Activity.this,"Saved",Toast.LENGTH_LONG).show();
                    getSupportActionBar().setTitle(title_checklist.getText().toString());
                    title_checklist.clearFocus();
                    closekeyboard();
                    checkIcon =false;

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
                return  true;
            case R.id.color:
                Dialog dialog = new Dialog(CheckList_Activity.this);
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

                changeColorActionbar(button_red,dialog,4,1,"#fc6f6f");
                changeColorActionbar(button_black,dialog,8,0,"#b5b5b5");
                changeColorActionbar(button_orange,dialog,3,1,"#ffaf75");
                changeColorActionbar(button_yellow,dialog,2,1,"#ffe77a");
                changeColorActionbar(button_green,dialog,5,1,"#94f08d");
                changeColorActionbar(button_purple,dialog,7,1,"#e4a8ff");
                changeColorActionbar(button_gray,dialog,9,1,"#e6e6e6");
                changeColorActionbar(button_blue,dialog,6,1,"#97c2f7");
                changeColorActionbar(button_white_gray,dialog,10,1,"#ffffff");

                dialog.show();
                return true;
            default:break;

        }

        return super.onOptionsItemSelected(item);
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
                linearLayout.setBackgroundColor(Color.parseColor(colorBackground));
                recyclerView.setBackgroundColor(Color.parseColor(colorBackground));
                button_additem.setBackgroundColor(Color.parseColor(colorBackground));
                colorid = color;
                dialog.cancel();

            }
        });
    }
    public boolean addCheckList(int color){
        if(date == null){
            date = Calendar.getInstance().getTime();
        }
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
            Date date = Calendar.getInstance().getTime();
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
//            ItemCheckList itemCheckList = new ItemCheckList();
                ItemCheckListDAO itemCheckListDAO = ItemCheckListDAO.getInstance();
                itemCheckList.setId(itemCheckListDAO.count()+1);
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
        SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
        int color =pre.getInt("default_color",0xFFF7D539);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(color));
    }
}

