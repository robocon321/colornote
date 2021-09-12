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
    RelativeLayout relativeLayout_signIn;
    String themeName;
    SharedPreferences sharedPreferences;
    TextView username;
//    CountDownTimer countDownTimer;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        addControls(view);
        addEvent();
        username = (TextView) view.findViewById(R.id.text_nameuser);
        content();
        username.setText(Constant.textSignin);

        return view;
    }

    private void addControls(View view) {
        btnMoreTrashCan = (Button) view.findViewById(R.id.btnMoreTrashCan);
        btnMoreArchive = (Button) view.findViewById(R.id.btnMoreArchive);
        btnMoreSettings = (Button) view.findViewById(R.id.btnMoreSettings);
        btnMoreTheme = (Button) view.findViewById(R.id.btnMoreTheme);
        relativeLayout_signIn = (RelativeLayout) view.findViewById(R.id.relative_signIn);

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
    public void content(){
        username.setText(Constant.textSignin);
        refresh(1000);
    }
    public void refresh(int mili){
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
           content();
            }
        };
        handler.postDelayed(runnable,mili);
    }

}

