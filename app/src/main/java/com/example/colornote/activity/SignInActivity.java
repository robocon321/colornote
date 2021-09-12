package com.example.colornote.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.colornote.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.analytics.FirebaseAnalytics;

public class SignInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "";
    TextView txtHtml,textgmail;
    Button btn_more,btn_singinemail,btn_google,btn_facebook;
    LinearLayout layout_fa_google,layout_signin;
//    private FirebaseAnalytics firebaseAnalytics ;
    private GoogleApiClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        setTitle("Online Backup");
//        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        btn_singinemail =(Button) findViewById(R.id.signIn_email);
        txtHtml = findViewById(R.id.textView_html);
        btn_google =findViewById(R.id.btn_google);
        btn_facebook = findViewById(R.id.btn_facebook);
        textgmail = findViewById(R.id.textgmail);
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
        btn_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        btn_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleSignInClient = new GoogleApiClient.Builder(this).enableAutoManage(this, (GoogleApiClient.OnConnectionFailedListener) this).
                addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();

    }
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleSignInClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
//
        }
    }
    private void handleSignInResult(GoogleSignInResult result){
        Log.d(TAG,"handleSignInResult:"+result.isSuccess());
        if(result.isSuccess()){
            GoogleSignInAccount acct = result.getSignInAccount();
            textgmail.setText("Hello "+ acct.getDisplayName());
            Toast.makeText(SignInActivity.this,"" +acct.getDisplayName(),Toast.LENGTH_LONG).show();
        }else{

        }
    }
    private void signOut(){
        Auth.GoogleSignInApi.signOut(mGoogleSignInClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                textgmail.setText("Sign out");
                Toast.makeText(SignInActivity.this,"Sign out",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}