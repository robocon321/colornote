package com.example.colornote.activity;


import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import com.example.colornote.R;
import com.example.colornote.dao.TextDAO;
import com.example.colornote.model.Text;

import java.util.Calendar;
import java.util.Date;

public class CheckList_Activity extends AppCompatActivity {
    private int colorid;
    EditText title_checklist;
    Toolbar toolbar;
    boolean checkIcon = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);
        toolbar = findViewById(R.id.toolbar_checklist);
        title_checklist = findViewById(R.id.title_checklist);
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
                }
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
//                    addText(colorid);
                    Toast.makeText(CheckList_Activity.this,"Saved",Toast.LENGTH_LONG).show();
                    checkIcon =false;
                }
                return true;
            case R.id.edit:
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_check_24);
                title_checklist.setSelection(0);
                checkIcon = true;
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
                button_white_gray = dialog.findViewById(R.id.btn_gray);

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
    public void addText(int color){

    }
    protected void onResume() {
        super.onResume();
        SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
        int color =pre.getInt("default_color",0xFFF7D539);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(color));
    }
}
