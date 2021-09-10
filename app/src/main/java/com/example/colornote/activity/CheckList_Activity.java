package com.example.colornote.activity;


import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CheckList_Activity extends AppCompatActivity {
    private int colorid;
    EditText title_checklist;
    Toolbar toolbar;
    boolean checkIcon = true;
    Button button_additem;
    RecyclerView recyclerView;
    ArrayList<String> list = new ArrayList<>();
    private long numEdit = 0;
    CheckList checkList = new CheckList();
    ItemCheckList itemCheckList = new ItemCheckList();
    int listItemSize,parentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);
        this.colorid = 2;
        toolbar = findViewById(R.id.toolbar_checklist);
        title_checklist = findViewById(R.id.title_checklist);
        button_additem = findViewById(R.id.btn_additem);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_list) ;
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CheckList_Activity.this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);

        CheckListAdapter checkListAdapter = new CheckListAdapter(list,getApplicationContext());
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
                    Toast.makeText(CheckList_Activity.this,"1",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(CheckList_Activity.this,"tonch",Toast.LENGTH_LONG).show();
                }
            }
        });
        button_additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportActionBar().setTitle(title_checklist.getText().toString());
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
                            Toast.makeText(CheckList_Activity.this,""+list.size(),Toast.LENGTH_LONG).show();
                            CheckListAdapter checkListAdapter = new CheckListAdapter(list,getApplicationContext());
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
            }
        });
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
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
                    if(numEdit==0) {
                        addItemCheckList();
                        addCheckList(colorid);
                    }else{
                        editItemChecklist();
                        editCheckList(colorid);
                    }
                    Toast.makeText(CheckList_Activity.this,"Saved",Toast.LENGTH_LONG).show();
                    checkIcon =false;
                    getSupportActionBar().setTitle(title_checklist.getText().toString());
                }
                return true;
            case R.id.edit:
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_check_24);
                title_checklist.setSelection(0);
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

                changeColorActionbar(button_red,dialog,4);
                changeColorActionbar(button_black,dialog,8);
                changeColorActionbar(button_orange,dialog,3);
                changeColorActionbar(button_yellow,dialog,2);
                changeColorActionbar(button_green,dialog,5);
                changeColorActionbar(button_purple,dialog,7);
                changeColorActionbar(button_gray,dialog,9);
                changeColorActionbar(button_blue,dialog,6);
                changeColorActionbar(button_white_gray,dialog,10);

                dialog.show();
                return true;
            default:break;

        }

        return super.onOptionsItemSelected(item);
    }
    public void changeColorActionbar(Button button,Dialog dialog,int color){

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable drawable = button.getBackground();
                ActionBar actionBar = getSupportActionBar();
                actionBar.setBackgroundDrawable(drawable);
                colorid = color;
                dialog.cancel();

            }
        });
    }
    public boolean addCheckList(int color){
//        CheckList checkList = new CheckList();
        CheckListDAO checkListDAO = CheckListDAO.getInstance();
        checkList.setId(checkListDAO.count()+1);
        checkList.setTitle(title_checklist.getText().toString());
        checkList.setReminderId(1);
        checkList.setColorId(color);
        Date date = Calendar.getInstance().getTime();
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
    public void editItemChecklist(){
        for (int i = 0; i < list.size(); i++) {
//            ItemCheckList itemCheckList = new ItemCheckList();
            if(i<listItemSize) {
                ItemCheckListDAO itemCheckListDAO = ItemCheckListDAO.getInstance();
//            itemCheckList.setId(itemCheckListDAO.count()+1);
                itemCheckList.setContent(list.get(i));
                Date date = Calendar.getInstance().getTime();
                itemCheckList.setModifiedDate(date);
//            itemCheckList.setParentId(checkListDAO.count()+1);
                itemCheckList.setStatus(Constant.STATUS.NORMAL);
                itemCheckListDAO.update(itemCheckList);
            }
            else{
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
    }
    protected void onResume() {
        super.onResume();
        SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
        int color =pre.getInt("default_color",0xFFF7D539);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(color));
    }
}

