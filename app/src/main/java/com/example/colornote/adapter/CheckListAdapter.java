package com.example.colornote.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.colornote.R;
import com.example.colornote.activity.CheckList_Activity;
import com.example.colornote.util.Constant;
import com.google.android.material.resources.TextAppearance;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class CheckListAdapter extends RecyclerView.Adapter<CheckListAdapter.ViewHolder> implements Filterable {
    private ArrayList<String> listitem;
    private ArrayList<String> filterList;
    private Context context;
    String queryText = "";
    public CheckListAdapter(ArrayList<String> listitem,Context context){
        this.listitem = listitem;
        this.context = context;
        this.filterList = listitem;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_checklist,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        holder.textView.setText(filterList.get(position));
        if(Constant.num_edit==0){
            holder.button.setVisibility(View.GONE);
        }else{
            holder.button.setVisibility(View.VISIBLE);
        }

        String dataText = filterList.get(position);
        if(queryText!=null && !queryText.isEmpty()){
            int startsPos = dataText.toLowerCase().indexOf(queryText.toLowerCase());
            int endPos = startsPos+queryText.length();
            if(startsPos!=-1){
                Spannable spannable = new SpannableString(dataText);
                ColorStateList colorStateList = new ColorStateList(new int[][]{new int[]{}},new int[]{Color.BLUE});
                TextAppearanceSpan textAppearance = new TextAppearanceSpan(null, Typeface.BOLD,-1,colorStateList,null);
                spannable.setSpan(textAppearance,startsPos,endPos, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.textView.setText(spannable);
            }else{
                holder.textView.setText(dataText);
            }

        }else{
            holder.textView.setText(dataText);

        }
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }
    public void removeItem(int position){
        listitem.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if(constraint!=null && !constraint.equals("")){
                queryText = constraint.toString();
                List<String> newList = new ArrayList<>();
                for(int i = 0;i<listitem.size();i++){
                    if(listitem.get(i).toLowerCase().contains(constraint.toString().toLowerCase())){
                        newList.add(listitem.get(i));
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.count = newList.size();
                filterResults.values = newList;
                return filterResults;
            }

            return null;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if(results!=null && results.count>0){
                filterList = (ArrayList<String>) results.values;
                notifyDataSetChanged();
            }
        }
    };
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
                    Constant.num_edit = 1;
                    notifyDataSetChanged();
                    Dialog dialog = new Dialog(v.getContext());
                    dialog.setContentView(R.layout.dialog_additem_checklist);
                    TextView textViewtitle;
                    EditText editTextitemDialog;
                    Button button_ok,button_exit;
                    textViewtitle = (TextView) dialog.findViewById(R.id.textViewTitle);
                    editTextitemDialog = (EditText) dialog.findViewById(R.id.edtext_item);
                    button_ok = (Button) dialog.findViewById(R.id.btn_ok);
                    button_exit = (Button) dialog.findViewById(R.id.btn_exit);
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

