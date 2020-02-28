package com.maxpetroleum.soapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.maxpetroleum.soapp.Activity.DealerList;
import com.maxpetroleum.soapp.Model.Dealer;
import com.maxpetroleum.soapp.R;

import java.util.ArrayList;

public class DealerListAddapter extends RecyclerView.Adapter<DealerListAddapter.MyVieHolder>{
    ArrayList<Dealer> list;
    ClickHandler clickHandler;

    public DealerListAddapter(ArrayList<Dealer> list, DealerList clickHandler) {
        this.list = list;
        this.clickHandler = clickHandler;
    }

    @NonNull
    @Override
    public MyVieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_dealer,parent,false);
        MyVieHolder myVieHolder=new MyVieHolder(view);
        return myVieHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyVieHolder holder, final int position) {
        Dealer dealer=list.get(position);
        holder.dealerCode.setText(dealer.getUid());
        holder.pendingRequests.setText(dealer.getPendingRequests()+"");
        if(dealer.getPendingRequests()==0)
            holder.pendingRequests.setVisibility(View.GONE);
        else
            holder.pendingRequests.setVisibility(View.VISIBLE);
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandler.itemClicked(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface ClickHandler{
        public void itemClicked(int position);
    }

    public static class MyVieHolder extends RecyclerView.ViewHolder {
        TextView dealerCode,pendingRequests;
        LinearLayout item;
        public MyVieHolder(@NonNull View itemView) {
            super(itemView);
            dealerCode=itemView.findViewById(R.id.dealerCode);
            pendingRequests=itemView.findViewById(R.id.pendingRequests);
            item=itemView.findViewById(R.id.SingleDealer);
        }
    }
}
