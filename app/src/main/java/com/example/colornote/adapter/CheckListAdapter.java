package com.example.colornote.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.colornote.R;
import com.example.colornote.activity.CheckList_Activity;
import com.example.colornote.model.Color;

import java.util.ArrayList;

public class CheckListAdapter extends RecyclerView.Adapter<CheckListAdapter.ViewHolder> {
    private ArrayList<String> listitem;
    private Context context;
    public CheckListAdapter(ArrayList<String> listitem,Context context){
        this.listitem = listitem;
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_checklist,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(listitem.get(position));
    }

    @Override
    public int getItemCount() {
        return listitem.size();
    }
    public void removeItem(int position){
        listitem.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        Button button;
        public ViewHolder( View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text_itemchecklist);
            button = (Button) itemView.findViewById(R.id.button_deleteitem);
            textView.setBackgroundDrawable(itemView.getBackground());
            button.setBackgroundDrawable(itemView.getBackground());
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeItem(getAdapterPosition());
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dialog = new Dialog(v.getContext());
                    dialog.setContentView(R.layout.dialog_additem_checklist);
                    TextView textViewtitle;
                    EditText editTextitemDialog;
                    Button button_ok,button_exit;
                    textViewtitle = (TextView) dialog.findViewById(R.id.textViewTitle);
                    editTextitemDialog = (EditText) dialog.findViewById(R.id.edtext_item);
                    button_ok = (Button) dialog.findViewById(R.id.btn_ok);
                  //  button_exit = (Button) dialog.findViewById(R.id.btn_exit);
                    textViewtitle.setText("Edit Item");
                    button_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String text = editTextitemDialog.getText().toString();
                            textView.setText(text);
                            listitem.set(getAdapterPosition(),text);
                            dialog.dismiss();

                        }
                    });
                    dialog.show();
                }
            });
        }
    }
}

