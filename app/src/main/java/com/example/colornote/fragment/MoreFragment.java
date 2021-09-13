package com.example.colornote.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AlertDialogLayout;
import androidx.fragment.app.Fragment;

import com.example.colornote.R;
import com.example.colornote.activity.ArchiveActivity;
import com.example.colornote.activity.SettingsActivity;
import com.example.colornote.activity.SignInActivity;
import com.example.colornote.activity.TrashCanActivity;
import com.example.colornote.util.Constant;

import java.util.Timer;
import java.util.TimerTask;

public class MoreFragment extends Fragment {
    @Nullable
    Button btnMoreTrashCan,btnMoreArchive,btnMoreSettings,btnMoreTheme,btnThemeDark,btnThemeDefault;
    Dialog dialogTheme;
    LinearLayout relativeLayout_signIn;
    String themeName;
    SharedPreferences sharedPreferences;
    SharedPreferences preferencesAccount;
    TextView username;
//    CountDownTimer countDownTimer;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        addControls(view);
        addEvent();
        username = (TextView) view.findViewById(R.id.text_nameuser);
        checkLogin();
        return view;
    }

    public void checkLogin(){
        preferencesAccount = getActivity().getSharedPreferences("account", Context.MODE_PRIVATE);
        String nameAccount = preferencesAccount.getString("account_name", "");
        if(nameAccount.length() > 0) {
            username.setText(nameAccount);
        } else {
            username.setText("Not sign in");
        }
    }

    private void addControls(View view) {
        btnMoreTrashCan =  view.findViewById(R.id.btnMoreTrashCan);
        btnMoreArchive =  view.findViewById(R.id.btnMoreArchive);
        btnMoreSettings =  view.findViewById(R.id.btnMoreSettings);
        btnMoreTheme =  view.findViewById(R.id.btnMoreTheme);
        relativeLayout_signIn =  view.findViewById(R.id.relative_signIn);
    }

    private void addEvent() {
        btnMoreSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                getActivity().startActivity(intent);
            }
        });
        btnMoreTrashCan.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                     Intent intent = new Intent(getActivity(), TrashCanActivity.class);
                    getActivity().startActivity(intent);
    }
});
        btnMoreArchive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ArchiveActivity.class);
                getActivity().startActivity(intent);
            }
        });
        btnMoreTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View view =inflater.inflate(R.layout.dialog_theme,null);

                builder.setView(view).setTitle("Theme");
                AlertDialog dialog = builder.create();
                dialog.show();
                btnThemeDark = view.findViewById(R.id.btnThemeDark);
                btnThemeDefault = view.findViewById(R.id.btnThemeDefault);


                btnThemeDark.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setTheme("Dark");

                    }
                });
                btnThemeDefault.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       setTheme("Default");

                    }
                });

            }
        });

//        cho nay bao loi nen tao comment lai
        relativeLayout_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),SignInActivity.class);
                getActivity().startActivity(intent);

            }
        });

//
//        username.setText(getArguments().getString("key1"));
    }

    @Override
    public void onResume() {
        super.onResume();
        checkLogin();
        sharedPreferences = getActivity().getSharedPreferences("Theme", Context.MODE_PRIVATE);
        themeName = sharedPreferences.getString("ThemeName", "Default");
        if(themeName.equalsIgnoreCase("Dark")){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public void setTheme(String name) {
        // Create preference to store theme name
        SharedPreferences preferences = getActivity().getSharedPreferences("Theme", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ThemeName", name);
        editor.apply();
        getActivity().recreate();
    }
}

