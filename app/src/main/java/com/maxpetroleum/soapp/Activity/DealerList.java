package com.maxpetroleum.soapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.maxpetroleum.soapp.Adapter.DealerListAddapter;
import com.maxpetroleum.soapp.Model.Dealer;
import com.maxpetroleum.soapp.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class DealerList extends AppCompatActivity implements DealerListAddapter.ClickHandler {

    RecyclerView deliveryList;
    ArrayList<Dealer> lists;
    DealerListAddapter addapter;
    public static String SOID="SO1";
    FirebaseDatabase database;
    DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_list);

        init();

        fetch();
    }

    private void fetch() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lists.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Dealer dealer=new Dealer(dataSnapshot1.getKey(),(Long) dataSnapshot1.getValue());
                    lists.add(dealer);
                }
                Collections.sort(lists,Collections.<Dealer>reverseOrder());
                addapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        addapter.notifyDataSetChanged();

    }

    private void init() {
        deliveryList=findViewById(R.id.dealerList);
        lists=new ArrayList<>();
        addapter=new DealerListAddapter(lists,this);
        deliveryList.setLayoutManager(new LinearLayoutManager(this));
        deliveryList.setAdapter(addapter);
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference("SO").child(SOID);
    }

    @Override
    public void itemClicked(int position) {
        Dealer dealer=lists.get(position);
        Intent intent=new Intent(DealerList.this,PoList.class);
        intent.putExtra("Data",dealer);
        startActivity(intent);
    }
}
