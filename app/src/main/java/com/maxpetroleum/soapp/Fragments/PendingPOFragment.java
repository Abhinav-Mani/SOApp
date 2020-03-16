package com.maxpetroleum.soapp.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.maxpetroleum.soapp.Activity.DealerList;
import com.maxpetroleum.soapp.Activity.PoList;
import com.maxpetroleum.soapp.Adapter.PoListAdapter;
import com.maxpetroleum.soapp.Model.Dealer;
import com.maxpetroleum.soapp.Model.PO_Info;
import com.maxpetroleum.soapp.R;

import java.security.Key;
import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class PendingPOFragment extends Fragment {

    FirebaseDatabase database;
    DatabaseReference myRef;
    RecyclerView recyclerView;
    ArrayList<PO_Info> list;
    PoListAdapter adapter;
    public PendingPOFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_pending_po, container, false);
        init(view,(PoList) getActivity());
        fetch();
        return view;
    }

    private void fetch() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                Log.d("ak47", dataSnapshot.getKey()+" "+dataSnapshot.getValue()+" "+"onDataChange: "+dataSnapshot.getChildrenCount());
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                    if (String.valueOf(dataSnapshot1.getValue()).equalsIgnoreCase("Pending")) {
                        getMoreInfo(dataSnapshot1.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getMoreInfo(final String key) {
        database.getReference("PO").child(PoList.dealer.getUid()).child(key).keepSynced(true);
        database.getReference("PO").child(PoList.dealer.getUid()).child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                PO_Info po_info=dataSnapshot.getValue(PO_Info.class);
                boolean found = false;
                if(po_info!=null) {
                    for (int i = 0; i < list.size(); i++) {
                        Log.d("mak", "onDataChange: " + list.get(i) + " " + po_info);
                        if (list.get(i).getPo_no().equalsIgnoreCase(po_info.getPo_no())) {
                            list.get(i).setBill_date(po_info.getBill_date());
                            found = true;
                        }
                        if (list.get(i).getBill_date() != null) {
                            list.remove(i);
                            found = true;
                            Log.d("hello", "found");
                        }
                    }
                }
                if(!found)
                    list.add(po_info);
                adapter.notifyDataSetChanged();
                if (po_info != null) {
                    PoList.hashMap.put(po_info.getPo_no(),key);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void init(View view,PoList poList) {
        database= FirebaseDatabase.getInstance();
        Log.d("ak47", "init: "+PoList.dealer.getUid());
        myRef=database.getReference().child("Dealer").child(PoList.dealer.getUid());
        myRef.keepSynced(true);
        recyclerView=view.findViewById(R.id.pendingRequestsPO);
        list=new ArrayList<>();
        adapter=new PoListAdapter(list,poList);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);
    }

}
