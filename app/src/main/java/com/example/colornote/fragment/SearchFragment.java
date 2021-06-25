package com.example.colornote.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
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
import com.example.colornote.mapper.RowMapper;
import com.example.colornote.mapper.TextMapper;
import com.example.colornote.model.Task;
import com.example.colornote.model.Text;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    EditText edtSearch;

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
        edtSearch = (EditText) view.findViewById(R.id.edtSearch);

        gvTaskSearch = (GridView) view.findViewById(R.id.gvTaskSearch);
        lsTask = new ArrayList<Task>();

        adapterTask = new ViewListAdapter(lsTask, getActivity());
        gvTaskSearch.setAdapter(adapterTask);

        checkListDAO = CheckListDAO.getInstance();
        textDAO = TextDAO.getInstance();

    }

    private void addEvents() {
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                showWithChar(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void showWithChar(String s) {
//        khong the add cung luc textdao va checklisdao
        lsTask.clear();
        lsTask.addAll(textDAO.getWithKey(new TextMapper(), s));
        gvTaskSearch.deferNotifyDataSetChanged();
//        lsTask.addAll(checkListDAO.getWithKey(new CheckListMapper(),s));
//        gvTaskSearch.deferNotifyDataSetChanged();
    }
}
