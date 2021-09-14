package com.example.colornote.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.colornote.R;
import com.example.colornote.util.DateConvert;
import com.example.colornote.util.SyncFirebase;
import com.example.colornote.fragment.MoreFragment;
import com.example.colornote.model.Text;
import com.example.colornote.util.Constant;
//import com.facebook.AccessToken;
//import com.facebook.CallbackManager;
//import com.facebook.FacebookCallback;
//import com.facebook.FacebookException;
//import com.facebook.login.LoginManager;
//import com.facebook.login.LoginResult;
//import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class SignInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "";
    Button btn_google,btn_facebook,btn_signup_logout;
    LinearLayout layout_fa_google,layout_tk;
//    private FirebaseAnalytics firebaseAnalytics ;
    private GoogleApiClient mGoogleSignInClient;
    TextView text_username,text_gmail, txtTitleLastSync, txtContentLastSync;
    String name_user = "",urlImage;
//    CallbackManager callbackManager;
//    LoginButton loginButton;
    private static final String EMAIL = "email";
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

//        getSupportActionBar().setTitle("Online Backup");
//        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        btn_google =findViewById(R.id.btn_google);
        btn_facebook = findViewById(R.id.btn_facebook);
        btn_signup_logout = findViewById(R.id.button_singUp);
        text_username = findViewById(R.id.username);
        text_gmail = findViewById(R.id.gmail);
        layout_fa_google = (LinearLayout) findViewById(R.id.layout_fa_google);
        layout_tk = (LinearLayout) findViewById((R.id.layout_tk));
        txtTitleLastSync = findViewById(R.id.txtTitleLastSync);
        txtContentLastSync = findViewById(R.id.txtContentLastSync);

        sharedPreferences = getSharedPreferences("account",MODE_PRIVATE);
        content();
//        if(Constant.num_acct==0) {
//            layout_tk.setVisibility(View.GONE);
//            layout_fa_google.setVisibility(View.VISIBLE);
//        }
//        else{
//            layout_tk.setVisibility(View.VISIBLE);
//            layout_fa_google.setVisibility(View.GONE);
//        }

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
        btn_signup_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String accountId = sharedPreferences.getString("account_id", "");
                if(accountId.length() > 0) {
                    signOut();
                }
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleSignInClient = new GoogleApiClient.Builder(this).enableAutoManage(this, (GoogleApiClient.OnConnectionFailedListener) this).
                addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();

//        callbackManager = CallbackManager.Factory.create();
//
//
//        loginButton = (LoginButton) findViewById(R.id.login_button);
//        loginButton.setReadPermissions(Arrays.asList(EMAIL));
//        // If you are using in a fragment, call loginButton.setFragment(this);
//
//        // Callback registration
//        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                // App code
//            }
//
//            @Override
//            public void onCancel() {
//                // App code
//            }
//
//            @Override
//            public void onError(FacebookException exception) {
//                // App code
//            }
//        });
//        LoginManager.getInstance().registerCallback(callbackManager,
//                new FacebookCallback<LoginResult>() {
//                    @Override
//                    public void onSuccess(LoginResult loginResult) {
//                        // App code
//                    }
//
//                    @Override
//                    public void onCancel() {
//                        // App code
//                    }
//
//                    @Override
//                    public void onError(FacebookException exception) {
//                        // App code
//                    }
//                });
//        AccessToken accessToken = AccessToken.getCurrentAccessToken();
//        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LoginManager.getInstance().logInWithReadPermissions(SignInActivity.this, Arrays.asList("public_profile"));
//            }
//        });
    }
    private void signIn() {
        if(checkAvailableInternet()) {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleSignInClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
        } else {
            Toast.makeText(this, "Internet không có sẵn", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        callbackManager.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    private void handleSignInResult(GoogleSignInResult result){
        Log.d(TAG,"handleSignInResult:"+result.isSuccess());
        if(result.isSuccess()){
            GoogleSignInAccount acct = result.getSignInAccount();
            text_gmail.setText(acct.getId());
            SharedPreferences sharedPreferences = getSharedPreferences("account", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("account_id", acct.getId());
            editor.putString("account_name", acct.getDisplayName());
            editor.putString("account_email", acct.getEmail());
            editor.putLong("last_sync", Calendar.getInstance().getTimeInMillis());
            SyncFirebase.getInstance().login(acct.getId(), this);
            editor.apply();
            Toast.makeText(SignInActivity.this,"Đăng nhập thành công",Toast.LENGTH_LONG).show();
//          urlImage = acct.getPhotoUrl().toString();
            content();
        }else{
            Toast.makeText(this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
        }
    }
    private void signOut(){
        if(checkAvailableInternet()) {
            Auth.GoogleSignInApi.signOut(mGoogleSignInClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    SharedPreferences sharedPreferences = getSharedPreferences("account", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.commit();
                    content();
                    Toast.makeText(SignInActivity.this,"Sign out",Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(this, "Internet không có sẵn", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    public void content(){
        String accountId = sharedPreferences.getString("account_id", "");
        String accountName = sharedPreferences.getString("account_name", "");
        String accountEmail = sharedPreferences.getString("account_email", "");
        long accountLastSyncMilli = sharedPreferences.getLong("last_sync", Calendar.getInstance().getTimeInMillis());

        if(accountId.length() == 0) {
            layout_tk.setVisibility(View.GONE);
            layout_fa_google.setVisibility(View.VISIBLE);
            btn_signup_logout.setText("Sign up");
            txtTitleLastSync.setVisibility(View.INVISIBLE);
            txtContentLastSync.setVisibility(View.INVISIBLE);
        }
        else{
            layout_tk.setVisibility(View.VISIBLE);
            layout_fa_google.setVisibility(View.GONE);
            text_username.setText(accountName);
            text_gmail.setText(accountEmail);
            btn_signup_logout.setText("Log out");
            txtTitleLastSync.setVisibility(View.VISIBLE);
            txtContentLastSync.setVisibility(View.VISIBLE);

            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(accountLastSyncMilli);
            txtContentLastSync.setText("Lúc: " + new DateConvert(cal.getTime()).showTime());
        }
    }

    public boolean checkAvailableInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        }
        else
            return false;
    }

}