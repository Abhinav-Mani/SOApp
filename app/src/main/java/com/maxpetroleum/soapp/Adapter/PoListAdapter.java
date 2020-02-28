package com.maxpetroleum.soapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.maxpetroleum.soapp.Activity.PoList;
import com.maxpetroleum.soapp.Model.Dealer;
import com.maxpetroleum.soapp.Model.PO_Info;
import com.maxpetroleum.soapp.R;

import java.util.ArrayList;

public class PoListAdapter extends RecyclerView.Adapter<PoListAdapter.myViewHolder>{
    ArrayList<PO_Info> list;
    public interface ClickHandler {
        public void onItemClicked(PO_Info po_info);
    }
    ClickHandler clickHandler;

    public PoListAdapter(ArrayList<PO_Info> list, PoList poList)
    {
        this.list = list;
        this.clickHandler=poList;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_po,parent,false);
        myViewHolder myViewHolder=new myViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        final PO_Info po_info =list.get(position);
        holder.Amount.setText("₹"+po_info.getAmount());
        holder.date.setText(po_info.getPo_Date());
        holder.uid.setText(po_info.getPo_no());
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandler.onItemClicked(po_info);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {
        TextView Amount,date,uid;
        LinearLayout item;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            Amount=itemView.findViewById(R.id.poAmount);
            date=itemView.findViewById(R.id.poDate);
            uid=itemView.findViewById(R.id.poUID);
            item=itemView.findViewById(R.id.item);
        }
    }
}
