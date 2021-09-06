package com.example.colornote.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.appcompat.widget.SearchView;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.colornote.R;
import com.example.colornote.adapter.ViewAdapter;
import com.example.colornote.adapter.ViewListAdapter;
import com.example.colornote.dao.CheckListDAO;
import com.example.colornote.dao.TextDAO;
import com.example.colornote.mapper.CheckListMapper;
import com.example.colornote.mapper.TextMapper;
import com.example.colornote.model.Task;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    SearchView svSearch;

    GridView gvTaskSearch;
    ArrayList<Task> lsTask;
    ViewAdapter adapterTask;

    CheckListDAO checkListDAO;
    TextDAO textDAO;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        addControls(view);
        addEvents();
        return view;
    }

    private void addControls(View view) {
        svSearch = view.findViewById(R.id.svSearch);

        gvTaskSearch = view.findViewById(R.id.gvTaskSearch);
        lsTask = new ArrayList<>();


        adapterTask = new ViewListAdapter(lsTask, getActivity());
        gvTaskSearch.setAdapter(adapterTask);

        checkListDAO = CheckListDAO.getInstance();
        textDAO = TextDAO.getInstance();

    }

    private void addEvents() {
        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                char[] arrChar = s.toCharArray();
                for (char c : arrChar) {
                    showWithChar(c);
                }

                if (lsTask.isEmpty()) {
                    Toast.makeText(getActivity(), "Not found: " + s, Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                lsTask.clear();
                return false;
            }
        });
    }

    private void showWithChar(char s) {
        lsTask.clear();
        lsTask.addAll(checkListDAO.getWithKey(new CheckListMapper(), s));
        lsTask.addAll(textDAO.getWithKey(new TextMapper(), s));
        gvTaskSearch.deferNotifyDataSetChanged();
    }
}
