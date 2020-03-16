package com.maxpetroleum.soapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.maxpetroleum.soapp.Activity.DealerList;
import com.maxpetroleum.soapp.Model.Dealer;
import com.maxpetroleum.soapp.R;

import java.util.ArrayList;

public class DealerListAddapter extends RecyclerView.Adapter<DealerListAddapter.MyVieHolder> {
    private ArrayList<Dealer> list;
    private ClickHandler clickHandler;

    public DealerListAddapter(ArrayList<Dealer> list, DealerList clickHandler) {
        this.list = list;
        this.clickHandler = clickHandler;
    }

    @NonNull
    @Override
    public MyVieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_dealer_item, parent, false);
        MyVieHolder myVieHolder = new MyVieHolder(view);
        return myVieHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyVieHolder holder, final int position) {
        Dealer dealer = list.get(position);
        holder.dealerCode.setText("Dealer Code: " + dealer.getUid());
        holder.pendingRequests.setText(dealer.getPendingRequests() + "");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("UserData").child("Dealer")
                .child(dealer.getUid());
        ref.keepSynced(true);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                holder.name.setText("Name: " + dataSnapshot.child("name").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if (dealer.getPendingRequests() == 0)
            holder.pendingRequests.setVisibility(View.GONE);
        else
            holder.pendingRequests.setVisibility(View.VISIBLE);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
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

    public interface ClickHandler {
        void itemClicked(int position);
    }

    public static class MyVieHolder extends RecyclerView.ViewHolder {
        TextView dealerCode, pendingRequests, name;

        public MyVieHolder(@NonNull View itemView) {
            super(itemView);
            dealerCode = itemView.findViewById(R.id.dealerCode);
            name = itemView.findViewById(R.id.name);
            pendingRequests = itemView.findViewById(R.id.pendingRequests);
        }
    }
}
