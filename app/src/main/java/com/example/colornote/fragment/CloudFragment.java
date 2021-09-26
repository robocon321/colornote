package com.example.colornote.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.colornote.R;
import com.example.colornote.activity.SignInActivity;
import com.example.colornote.util.DateConvert;
import com.example.colornote.util.SyncFirebase;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class CloudFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener{
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "";
    Button btn_google,btn_facebook,btn_signup_logout;
    LinearLayout layout_fa_google,layout_tk;
    //    private FirebaseAnalytics firebaseAnalytics ;
    private GoogleApiClient mGoogleSignInClient;
    TextView text_username,text_gmail, txtTitleLastSync, txtContentLastSync;
    private static final String EMAIL = "email";
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    SharedPreferences sharedPreferences;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_sign_in, container, false);
        btn_google =view.findViewById(R.id.btn_google);
        btn_facebook = view.findViewById(R.id.btn_facebook);
        btn_signup_logout = view.findViewById(R.id.button_singUp);
        text_username = view.findViewById(R.id.username);
        text_gmail = view.findViewById(R.id.gmail);
        layout_fa_google = (LinearLayout) view.findViewById(R.id.layout_fa_google);
        layout_tk = (LinearLayout) view.findViewById((R.id.layout_tk));
        txtTitleLastSync = view.findViewById(R.id.txtTitleLastSync);
        txtContentLastSync = view.findViewById(R.id.txtContentLastSync);

        sharedPreferences = this.getActivity().getSharedPreferences("account",Context.MODE_PRIVATE);
        content();
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
        mGoogleSignInClient = new GoogleApiClient.Builder(this.getActivity()).enableAutoManage(this.getActivity(), (GoogleApiClient.OnConnectionFailedListener) this).
                addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();

        return view;
    }
    private void signIn() {
        if(checkAvailableInternet()) {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleSignInClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
        } else {
            Toast.makeText(this.getActivity(), "Internet không có sẵn", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void handleSignInResult(GoogleSignInResult result){
        Log.d(TAG,"handleSignInResult:"+result.isSuccess());
        if(result.isSuccess()){
            GoogleSignInAccount acct = result.getSignInAccount();
            text_gmail.setText(acct.getId());
            SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("account", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("account_id", acct.getId());
            editor.putString("account_name", acct.getDisplayName());
            editor.putString("account_email", acct.getEmail());
            editor.putLong("last_sync", Calendar.getInstance().getTimeInMillis());
            SyncFirebase.getInstance().login(acct.getId(),getActivity());
            editor.apply();
            Toast.makeText(this.getActivity(),"Đăng nhập thành công",Toast.LENGTH_LONG).show();
//          urlImage = acct.getPhotoUrl().toString();
            content();
        }else{
            Toast.makeText(this.getActivity(), "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
        }
    }
    private void signOut(){
        if(checkAvailableInternet()) {
            Auth.GoogleSignInApi.signOut(mGoogleSignInClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    SharedPreferences sharedPreferences = getSharedPreferences();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.commit();
                    content();
//                    Toast.makeText(SignInActivity.this,"Sign out",Toast.LENGTH_LONG).show();
                }

            });
        } else {
//            Toast.makeText(this, "Internet không có sẵn", Toast.LENGTH_SHORT).show();
        }
    }
    public SharedPreferences getSharedPreferences(){
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("account", Context.MODE_PRIVATE);
        return sharedPreferences;
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
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        }
        else
            return false;
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
