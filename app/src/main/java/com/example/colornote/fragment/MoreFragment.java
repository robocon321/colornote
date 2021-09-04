package com.example.colornote.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AlertDialogLayout;
import androidx.fragment.app.Fragment;

import com.example.colornote.R;
import com.example.colornote.activity.ArchiveActivity;
import com.example.colornote.activity.SignInActivity;
import com.example.colornote.activity.TrashCanActivity;

public class MoreFragment extends Fragment {
    @Nullable
    Button btnMoreTrashCan,btnMoreArchive,btnMoreSettings,btnMoreTheme;
    Dialog dialogTheme;
    RelativeLayout relativeLayout_signIn;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        addControls(view);
        addEvent();
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
            }
        });
        relativeLayout_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),SignInActivity.class);
                getActivity().startActivity(intent);
            }
        });
    }

}

