package com.example.colornote.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import android.app.Dialog;

import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.colornote.R;
import com.example.colornote.dao.TextDAO;
import com.example.colornote.fragment.CalendarFragment;
import com.example.colornote.model.Text;
import com.example.colornote.util.Constant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Text_Activity extends AppCompatActivity {
    private int colorid;
    Toolbar toolbar;
    EditText title_text, edit_text;
    TextView text_date;
    boolean checkIcon = true;

    private Date date;

    private long numEdit = 0;
    Text text = new Text();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        title_text = findViewById(R.id.title_text);
        edit_text = findViewById(R.id.edit_text);
        toolbar = findViewById(R.id.toolbar_text);
        text_date = findViewById(R.id.text_date);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Title text");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_check_24);
//

        title_text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !title_text.getText().toString().equals("")) {
                    getSupportActionBar().setTitle(title_text.getText().toString());
                }
            }
        });

        this.colorid = 2;
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        String data = bundle.getString("date");
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "" + date, Toast.LENGTH_SHORT).show();
    }

    //
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.text_checklist_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (checkIcon == false)
                    onBackPressed();
                else {
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
                    if(this.numEdit == 0)
                    addText(colorid);

                    else{
                        editText(colorid);
                    }
                    Toast.makeText(Text_Activity.this,"Saved",Toast.LENGTH_LONG).show();
                    checkIcon =false;
                    getSupportActionBar().setTitle(title_text.getText().toString());
                }
                return true;
            case R.id.edit:
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_check_24);
                title_text.setSelection(0);
                checkIcon = true;

                getSupportActionBar().setTitle(title_text.getText().toString());
                return  true;

            case R.id.color:
                getSupportActionBar().setTitle(title_text.getText().toString());
                Dialog dialog = new Dialog(Text_Activity.this);
                dialog.setContentView(R.layout.dialog_color);
                Button button_red, button_orange, button_yellow, button_green, button_blue,
                        button_purple, button_black, button_gray, button_white_gray;
                button_red = dialog.findViewById(R.id.btn_yellow);
                button_black = dialog.findViewById(R.id.btn_blue_1);
                button_orange = dialog.findViewById(R.id.btn_orange);
                button_yellow = dialog.findViewById(R.id.btn_red);
                button_green = dialog.findViewById(R.id.btn_green);
                button_blue = dialog.findViewById(R.id.btn_blue);
                button_purple = dialog.findViewById(R.id.btn_purple);
                button_gray = dialog.findViewById(R.id.btn_gray);
                button_white_gray = dialog.findViewById(R.id.btn_white);

                changeColorActionbar(button_red, dialog, 4);
                changeColorActionbar(button_black, dialog, 8);
                changeColorActionbar(button_orange, dialog, 3);
                changeColorActionbar(button_yellow, dialog, 2);
                changeColorActionbar(button_green, dialog, 5);
                changeColorActionbar(button_purple, dialog, 7);
                changeColorActionbar(button_gray, dialog, 9);
                changeColorActionbar(button_blue, dialog, 6);
                changeColorActionbar(button_white_gray, dialog, 10);

                dialog.show();
                return true;
            default:
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    public void changeColorActionbar(Button button, Dialog dialog, int color) {

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


    public boolean addText(int color) {
        if(date == null){
            date = Calendar.getInstance().getTime();
        }
        Text text = new Text();

        TextDAO textDAO = TextDAO.getInstance();
        text.setId(textDAO.count()+1);
        text.setTitle(title_text.getText().toString());
        text.setContent(edit_text.getText().toString());
        text.setColorId(color);

        text.setModifiedDate(date);
        text.setReminderId(-1);
        text.setStatus(Constant.STATUS.NORMAL);
        numEdit = textDAO.insert(text);

        Toast.makeText(Text_Activity.this,this.numEdit+"",Toast.LENGTH_LONG).show();
        closekeyboard();
        CalendarFragment calendarFragment = new CalendarFragment();
        try {
            calendarFragment.setIconEventDay();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }
    public boolean editText(int color){
//        Text text = new Text();
        TextDAO textDAO = TextDAO.getInstance();
        text.setTitle(title_text.getText().toString());
        text.setContent(edit_text.getText().toString());
        text.setColorId(color);
        Date date = Calendar.getInstance().getTime();
        text.setModifiedDate(date);
        text.setReminderId(-1);
        text.setStatus(Constant.STATUS.NORMAL);
        textDAO.update(text);
        closekeyboard();
        return true;
    }

    public void closekeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
        int color = pre.getInt("default_color", 0xFFF7D539);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(color));
    }
}
