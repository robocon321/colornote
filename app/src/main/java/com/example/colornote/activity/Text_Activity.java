package com.example.colornote.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
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


import com.example.colornote.R;
import com.example.colornote.dao.TextDAO;
import com.example.colornote.model.Text;

import java.util.Date;

public class Text_Activity extends AppCompatActivity {
    Toolbar toolbar;
    EditText title_text,edit_text;
    boolean checkIcon = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        title_text = findViewById(R.id.title_text);
        edit_text = findViewById(R.id.edit_text);

        toolbar = findViewById(R.id.toolbar_text);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Title text");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_check_24);
//

        title_text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus &&!title_text.getText().toString().equals("")){
                    getSupportActionBar().setTitle(title_text.getText().toString());
                }
            }
        });
    }



    //
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
                    addText();
                    Toast.makeText(Text_Activity.this,"Saved",Toast.LENGTH_LONG).show();
                    checkIcon =false;
                }
                return true;
            case R.id.edit:
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_check_24);
                checkIcon = true;
            case R.id.color:
                Dialog dialog = new Dialog(Text_Activity.this);
                dialog.setContentView(R.layout.dialog_color);
                Button button_red,button_orange,button_yellow,button_green,button_blue,
                        button_purple,button_blue_1,button_gray,button_white;
                button_red = dialog.findViewById(R.id.btn_red);
                button_blue_1 = dialog.findViewById(R.id.btn_blue_1);
                button_orange = dialog.findViewById(R.id.btn_orange);
                button_yellow = dialog.findViewById(R.id.btn_yellow);
                button_green = dialog.findViewById(R.id.btn_green);
                button_blue = dialog.findViewById(R.id.btn_blue);
                button_purple = dialog.findViewById(R.id.btn_purple);
                button_gray = dialog.findViewById(R.id.btn_gray);
                button_white = dialog.findViewById(R.id.btn_white);

                changeColorActionbar(button_red);
                changeColorActionbar(button_blue_1);
                changeColorActionbar(button_orange);
                changeColorActionbar(button_yellow);
                changeColorActionbar(button_green);
                changeColorActionbar(button_purple);
                changeColorActionbar(button_gray);
                changeColorActionbar(button_blue);
                changeColorActionbar(button_white);

                dialog.show();
            default:break;

        }

        return super.onOptionsItemSelected(item);
    }
    public void changeColorActionbar(Button button){

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable drawable = button.getBackground();
                ActionBar actionBar = getSupportActionBar();
                actionBar.setBackgroundDrawable(drawable);
            }
        });
    }
    public void addText(){
        Text text = new Text();
        TextDAO textDAO = TextDAO.getInstance();
        text.setTitle(title_text.getText().toString());
        text.setContent(edit_text.getText().toString());
        text.setColorId(1);
        text.setModifiedDate(new Date(2020,5,19, 0,0,0));
        text.setReminder(new Date(2020,4,9, 0,0,0));
        text.setStatus(3);

//        textDAO.insert(text);
        Log.d("AA", textDAO.insert(text)+"");
    }

}