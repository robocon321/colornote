package com.example.colornote.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
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
import com.example.colornote.dao.TextDAO;
import com.example.colornote.model.Text;
import com.example.colornote.util.Constant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Calendar_Text_Activity extends AppCompatActivity {

    private int colorid = 2;
    Toolbar toolbar;
    EditText title_text, edit_text;
    TextView text_date;
    boolean checkIcon = true;
    private long numEdit = 0;
    Text text = new Text();
    LinearLayout linearLayout;
    int color_black =1;
    int num_click = 0;
    String putDate;
    SharedPreferences sharedPreferences;
    String themeName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("Theme", Context.MODE_PRIVATE);
        themeName = sharedPreferences.getString("ThemeName", "Default");

        setContentView(R.layout.activity_text);
        title_text = findViewById(R.id.title_text);
        edit_text = findViewById(R.id.edit_text);
        toolbar = findViewById(R.id.toolbar_text);
        text_date = findViewById(R.id.text_date);
        linearLayout = findViewById(R.id.layout_text);
        if(themeName.equalsIgnoreCase("Dark")){
            linearLayout.setBackgroundColor(Color.parseColor("#000000"));
        }else{
            linearLayout.setBackgroundColor(Color.parseColor("#ffe77a"));
        }

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
        String s = simpleDateFormat.format(date);
        text_date.setText(s);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Title text");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_check_24);
        //edit item on home
        if(Constant.num_click==1){
            if(getIntent().getExtras()!=null) {
                num_click=1;
                text = (Text) getIntent().getExtras().get("text");
                title_text.setText(text.getTitle());
                actionBar.setTitle(text.getTitle());
                edit_text.setText(text.getContent());
                colorid = text.getColorId();
                text_date.setText(simpleDateFormat.format(text.getModifiedDate()));
                String colorSub = getIntent().getStringExtra("colorSub");
                putDate = getIntent().getStringExtra("putDate");
                //  Toast.makeText(Calendar_Activity.this,colorSub,Toast.LENGTH_LONG).show();
//                Drawable colorDrawable = new ColorDrawable(Color.parseColor(colorSub));
                actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(colorSub)));
                if(themeName.equalsIgnoreCase("Dark")){
                    linearLayout.setBackgroundColor(Color.parseColor("#000000"));
                }else{
                    linearLayout.setBackgroundColor(Color.parseColor(getIntent().getStringExtra("colorMain")));
                }

                Constant.num_click = 0;
                numEdit = 1;
                title_text.setVisibility(View.GONE);
                checkIcon = false;
                if(color_black==0){
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24_w);
                }else{
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
                }
            }else{
                Toast.makeText(Calendar_Text_Activity.this,"null",Toast.LENGTH_LONG).show();
            }

        }
        title_text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !title_text.getText().toString().equals("")) {
                    getSupportActionBar().setTitle(title_text.getText().toString());
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
        edit_text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    title_text.setVisibility(View.VISIBLE);
                    checkIcon = true;
                    if(color_black==0){
                        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_check_24_w);
                    }else{
                        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_check_24);
                    }

                }
            }
        });


//         this.colorid = 2;


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.text_checklist_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.find);


        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(Calendar_Text_Activity.this,query,Toast.LENGTH_LONG).show();
//                highlightText(query);
                String textToHightLight = query;
                String replaceWith = "<span style='background-color:yellow'>"+textToHightLight+"</span>";
                //get text from edittext
                String origianlText = edit_text.getText().toString();
                //
                String modifiedText = origianlText.replaceAll(textToHightLight,replaceWith);
                edit_text.setText(Html.fromHtml(modifiedText));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("aaaaa",newText);
                return false;
            }
        });
//         return super.onCreateOptionsMenu(menu);

        menu.findItem(R.id.mnCheck).setTitle(text.completeAll() ? "Uncheck" : "Check");
        menu.findItem(R.id.mnCheck).setIcon(text.completeAll() ? R.drawable.ic_square : R.drawable.ic_check);
        return super.onCreateOptionsMenu(menu);
//         return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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

                    if(this.numEdit == 0)
                        addText(colorid);

                    else{
                        editText(colorid);
                    }
                    Toast.makeText(Calendar_Text_Activity.this,"Saved",Toast.LENGTH_LONG).show();
                    checkIcon =false;
                    getSupportActionBar().setTitle(title_text.getText().toString());
                    title_text.clearFocus();
                    edit_text.clearFocus();
                    title_text.setVisibility(View.GONE);

                }
                return true;
            case R.id.edit:
                if(color_black==0){
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_check_24_w);
                }else{
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_check_24);
                }
                checkIcon = true;
                title_text.setVisibility(View.VISIBLE);

                getSupportActionBar().setTitle(title_text.getText().toString());
                title_text.requestFocus();
                return  true;

            case R.id.color:
                getSupportActionBar().setTitle(title_text.getText().toString());
                Dialog dialog = new Dialog(Calendar_Text_Activity.this);
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


                changeColorActionbar(button_red,dialog,4,1,"#f7cad0");
                changeColorActionbar(button_black,dialog,8,0,"#adb5bd");
                changeColorActionbar(button_orange,dialog,3,1,"#FFEAD7");
                changeColorActionbar(button_yellow,dialog,2,1,"#fff2b2");
                changeColorActionbar(button_green,dialog,5,1,"#b7efc5");
                changeColorActionbar(button_purple,dialog,7,1,"#dec9e9");
                changeColorActionbar(button_gray,dialog,9,1,"#dee2e6");
                changeColorActionbar(button_blue,dialog,6,1,"#caf0f8");
                changeColorActionbar(button_white_gray,dialog,10,1,"#ffffff");

                if(color_black==0){
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_check_24_w);
                }else{
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_check_24);
                }
                checkIcon = true;
                title_text.setVisibility(View.VISIBLE);

                getSupportActionBar().setTitle(title_text.getText().toString());
                dialog.show();
                return true;
            case R.id.find:

            case R.id.mnCheck:
                TextDAO.getInstance().changeCompleted(text.getId(), !text.completeAll());
                text.setCompleted(!text.completeAll());

                item.setTitle(text.completeAll() ? "Uncheck" : "Check");
                item.setIcon(text.completeAll() ? R.drawable.ic_square : R.drawable.ic_check);
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
            default:
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    public void deleteTask() {
        TextDAO.getInstance().changeStatus(text.getId(), Constant.STATUS.RECYCLE_BIN);
        onBackPressed();
    }

    public void archiveTask() {
        TextDAO.getInstance().changeStatus(text.getId(), Constant.STATUS.ARCHIVE);
        onBackPressed();
    }

    public void changeReminderActivitiy(){
        Intent intent = new Intent(this, ReminderActivity.class);
        intent.putExtra("task", text);
        startActivity(intent);
    }

    public void sendTask() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, text.getTitle() + "\n" + text.showContent());
        this.startActivity(Intent.createChooser(intent, "Share tasks"));
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
                if(themeName.equalsIgnoreCase("Dark")){

                    linearLayout.setBackgroundColor(Color.parseColor("#000000"));
                }else{
                    linearLayout.setBackgroundColor(Color.parseColor(colorBackground));
                }

                colorid = color;
                dialog.cancel();

            }
        });
    }


    public boolean addText(int color) {
        Text text = new Text();
        Date date = getDateFromCalendarFragment();

        TextDAO textDAO = TextDAO.getInstance();
        text.setId(textDAO.count()+1);
        text.setTitle(title_text.getText().toString());
        text.setContent(edit_text.getText().toString());
        text.setColorId(color);

        text.setModifiedDate(date);
        text.setReminderId(-1);
        text.setStatus(Constant.STATUS.NORMAL);
        numEdit = textDAO.insert(text);

        Toast.makeText(Calendar_Text_Activity.this,this.numEdit+"",Toast.LENGTH_LONG).show();
        closekeyboard();
        return true;
    }

    public boolean editText(int color){
        TextDAO textDAO = TextDAO.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(putDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        text.setTitle(title_text.getText().toString());
        text.setContent(edit_text.getText().toString());
        text.setColorId(color);
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

    @Override
    protected void onResume() {
        super.onResume();
        if(num_click==0) {
            SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
            int color = pre.getInt("default_color", 0xFFF7D539);
            ActionBar actionBar = getSupportActionBar();
            actionBar.setBackgroundDrawable(new ColorDrawable(color));
            // String hexColor = String.format("#%06X", (0xFFFFFF & color));


            if(color== ContextCompat.getColor(this, R.color.yellow_custom)){
                linearLayout.setBackgroundColor(Color.parseColor("#fff2b2"));
                colorid=2;
            }else if(color==ContextCompat.getColor(this, R.color.orange_custom)){
                linearLayout.setBackgroundColor(Color.parseColor("#FFEAD7"));
                colorid=3;
            }else if(color==ContextCompat.getColor(this, R.color.red_custom)){
                linearLayout.setBackgroundColor(Color.parseColor("#f7cad0"));
                colorid=4;
            }else if(color==ContextCompat.getColor(this, R.color.green_custom)){
                linearLayout.setBackgroundColor(Color.parseColor("#b7efc5"));
                colorid=5;
            }else if(color==ContextCompat.getColor(this, R.color.blue_custom)){
                linearLayout.setBackgroundColor(Color.parseColor("#caf0f8"));
                colorid=6;
            }else if(color==ContextCompat.getColor(this, R.color.purple_custom)){
                linearLayout.setBackgroundColor(Color.parseColor("#dec9e9"));
                colorid=7;
            }else if(color==ContextCompat.getColor(this, R.color.black_custom)){
                linearLayout.setBackgroundColor(Color.parseColor("#adb5bd"));
                colorid=8;
            }else if(color==ContextCompat.getColor(this, R.color.gray_custom)){
                linearLayout.setBackgroundColor(Color.parseColor("#dee2e6"));
                colorid=9;
            }else if(color==ContextCompat.getColor(this, R.color.white)){
                linearLayout.setBackgroundColor(Color.parseColor("#ffffff"));
                colorid=10;
            }  if(themeName.equalsIgnoreCase("Dark")){

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