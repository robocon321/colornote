package com.example.colornote.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.colornote.R;

public class HomeFragment extends Fragment {
    Toolbar toolbar;
    Button btnSort;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home, container, false);
        setHasOptionsMenu(true);
        init(view);
        setEvents();
        return view;
    }

    public void init(View view){
        toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        btnSort = view.findViewById(R.id.btnSort);
    }

    public void setEvents(){
        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogSortFragment dialog =  new DialogSortFragment();
                dialog.show(getActivity().getSupportFragmentManager(),"Show");
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.top_home_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.mnView:
                Dialog dialog = new Dialog(getActivity(), R.style.Dialog);
                dialog.setContentView(R.layout.layout_view_option);
                dialog.setTitle("View");
                dialog.show();
                break;
            case R.id.mnBackup:
                break;
            case R.id.mnColorOption:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
