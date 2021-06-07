package com.example.colornote.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.colornote.R;

public class MoreFragment extends Fragment {
    @Nullable

    Button btnMoreTrashCan,btnMoreArchive,btnMoreSettings,btnMoreTheme;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_more, container, false);
        addControls(view);
        addEvent();
        return view;

    }

    private void addControls(View view) {
        btnMoreTrashCan = (Button) view.findViewById(R.id.btnMoreTrashCan);
        btnMoreArchive = (Button) view.findViewById(R.id.btnMoreArchive);
        btnMoreSettings = (Button) view.findViewById(R.id.btnMoreSettings);
        btnMoreTheme = (Button) view.findViewById(R.id.btnMoreTheme);
    }

    private void addEvent() {
        btnMoreTrashCan.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

    }
});
    }

}
