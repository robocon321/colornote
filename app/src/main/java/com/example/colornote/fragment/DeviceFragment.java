package com.example.colornote.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.colornote.R;
import com.example.colornote.adapter.BackupRclAdapter;
import com.example.colornote.database.Database;
import com.example.colornote.model.BackupInfo;
import com.example.colornote.util.MD5Hash;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DeviceFragment extends Fragment{
    Button btnBackup;
    RecyclerView rcyBackup;
    BackupRclAdapter adapter;
    ArrayList<BackupInfo> infos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device, container, false);
        init(view);
        setEvents();
        return view;
    }

    public void init(View view){
        btnBackup = view.findViewById(R.id.btnBackup);
        rcyBackup = view.findViewById(R.id.rclBackup);
        infos = getBackupInfos();
        adapter = new BackupRclAdapter(getActivity(), infos);
        rcyBackup.setAdapter(adapter);
        rcyBackup.setHasFixedSize(true);
        rcyBackup.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcyBackup.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
    }

    public void setEvents(){
        btnBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.dialog_backup, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);
                builder.setView(view);
                builder.setTitle("Backup");
                final EditText edtPwd = view.findViewById(R.id.edtPwd);
                final EditText edtRePwd = view.findViewById(R.id.edtRePwd);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (edtPwd.getText().toString().trim().length() == 0){
                            Toast.makeText(getActivity(), "Password length > 0", Toast.LENGTH_SHORT).show();
                        }else if(!edtPwd.getText().toString().equals(edtRePwd.getText().toString())){
                            Toast.makeText(getActivity(), "Re-enter password not match password", Toast.LENGTH_SHORT).show();
                        }else{
                            BackupInfo info = new BackupInfo();

                            info.setDate(Calendar.getInstance().getTime());
                            info.setPassword(edtPwd.getText().toString());
                            info.setType(false);

                            String nameFile = buildNameFileBackup(info);
                            String dst = buildPathBackup(nameFile, getActivity());
                            String src = Database.getInstance().getSqLiteDatabase().getPath();

                            info.setPath(dst);

                            copyFile(src, dst);
                            infos.add(info);
                            adapter.notifyDataSetChanged();

                            dialog.dismiss();
                        }
                    }
                });
            }
        });
    }

    public String buildPathBackup(String name, Activity activity){
        String path;
        if(Build.VERSION.SDK_INT >= 17)
            path = activity.getApplicationInfo().dataDir+"/backups/"+name;
        else
            path = "/data/data"+activity.getPackageName()+"/backups/"+name;
        return path;
    }

    public String buildNameFileBackup(BackupInfo info){
        String result = info.getDate().getTime()+"_"+info.isType()+"_"+ MD5Hash.MDA5(info.getPassword())+".sqlite";
        return result;
    }

    public void copyFile(String src, String dst){
        File fileDst = new File(dst);
        try {
            fileDst.getParentFile().mkdirs();
            fileDst.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        InputStream inStream = null;
        OutputStream outStream = null;

        try {
            inStream = new FileInputStream(src);
            outStream = new FileOutputStream(dst);

            int length;
            byte[] buffer = new byte[1024];

            // copy the file content in bytes
            while ((length = inStream.read(buffer)) > 0) {
                outStream.write(buffer, 0, length);
            }

            inStream.close();
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<BackupInfo> getBackupInfos(){
        ArrayList<BackupInfo> list = new ArrayList<>();
        File parent = new File(buildPathBackup("", getActivity()));
        if(!parent.exists()) parent.mkdirs();

        for(File file : parent.listFiles()){
            BackupInfo info = getBackupInfoFromNameFile(file);
            if(info != null) list.add(getBackupInfoFromNameFile(file));
        }

        return list;
    }

    public BackupInfo getBackupInfoFromNameFile(File file){
        BackupInfo info = new BackupInfo();
        info.setPath(file.getPath());

        String path = file.getName();
        String[] arr = path.split("_");
        if(arr.length == 3){
            info.setDate(new Date(Long.parseLong(arr[0])));
            info.setType(Boolean.parseBoolean(arr[1]));
            info.setPassword(arr[2]);
            return info;
        }else {
            return null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        Database.getInstance().pointToDatabaseMain(getActivity());
    }
}
