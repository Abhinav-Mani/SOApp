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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.maxpetroleum.soapp.Activity.LoginActivity;
import com.maxpetroleum.soapp.Activity.PoList;
import com.maxpetroleum.soapp.Adapter.PoListAdapter;
import com.maxpetroleum.soapp.Model.PO_Info;
import com.maxpetroleum.soapp.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AcceptedPOFragment extends Fragment {
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private RecyclerView recyclerView;
    private ArrayList<PO_Info> list;
    private PoListAdapter adapter;
    public AcceptedPOFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_accepted_po, container, false);
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
                    if (String.valueOf(dataSnapshot1.getValue()).equalsIgnoreCase("Accepted")) {
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
                PO_Info po_info=(PO_Info) dataSnapshot.getValue(PO_Info.class);
                boolean found = false;
                for(int i=0;i<list.size();i++){
                    if(list.get(i).getPo_no().equalsIgnoreCase(po_info.getPo_no())){
                        list.get(i).setBill_date(po_info.getBill_date());
                        found=true;
                    }
                }
                if(!found)
                    list.add(po_info);

                adapter.notifyDataSetChanged();
                PoList.hashMap.put(po_info.getPo_no(),key);
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
