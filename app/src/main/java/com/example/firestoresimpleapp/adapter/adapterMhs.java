package com.example.firestoresimpleapp.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firestoresimpleapp.R;
import com.example.firestoresimpleapp.model.modelMhs;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class adapterMhs extends RecyclerView.Adapter<adapterMhs.MyHolder> {
    private Context context;
    private List<modelMhs> list;
    private Dialog dialog;

    public adapterMhs(Context context, List<modelMhs> list) {
        this.context = context;
        this.list = list;
    }

   public interface Dialog{
        void onLongClick(int i);
   }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    public Context getContext() {
        return context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent,false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        modelMhs m = list.get(position);
         holder.output.setText(m.getNama());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView output;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            output = itemView.findViewById(R.id.textView);

            itemView.setOnLongClickListener(view -> {
                if (dialog!=null){
                    dialog.onLongClick(getLayoutPosition());
                }
                return true;
            });

        }
    }
}
