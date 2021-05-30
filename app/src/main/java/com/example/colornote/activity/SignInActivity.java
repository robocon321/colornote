package com.example.colornote.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.colornote.R;

public class SignInActivity extends AppCompatActivity {

    TextView txtHtml;
    Button btn_more,btn_singinemail;
    LinearLayout layout_fa_google,layout_signin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        setTitle("Online Backup");
        btn_singinemail =(Button) findViewById(R.id.signIn_email);
        txtHtml = findViewById(R.id.textView_html);
        String html = "<b>New to online sync?</b>" +
                "<p>Keep your note online. You can safely restore your notes if your phone is dropped, lost, or stolen. You can also sync your notes across multiple devices.</p>";
        txtHtml.setText(Html.fromHtml(html));
        btn_more = (Button) findViewById(R.id.btn_more);
        layout_fa_google = (LinearLayout) findViewById(R.id.layout_fa_google);
        layout_signin = (LinearLayout) findViewById((R.id.layout_tk));
        layout_signin.setVisibility(View.GONE);
        btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_fa_google.setVisibility(View.GONE);
                layout_signin.setVisibility(View.VISIBLE);
            }
        });
        btn_singinemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_fa_google.setVisibility(View.VISIBLE);
                layout_signin.setVisibility(View.GONE);
            }
        });



    }
}