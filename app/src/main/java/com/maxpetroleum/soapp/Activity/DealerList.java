package com.maxpetroleum.soapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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

public class DealerList extends AppCompatActivity implements DealerListAddapter.ClickHandler, View.OnClickListener {

    RecyclerView deliveryList;
    ArrayList<Dealer> lists;
    DealerListAddapter addapter;
    public static String SOID;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ImageView back;
    ProgressDialog progressDialog;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_list);

        init();
        fetch();
    }

    private void fetch() {
        myRef.child(SOID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lists.clear();
                progressDialog.dismiss();
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
        myRef=database.getReference("SO");
        back = findViewById(R.id.back);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
        progressDialog.setCancelable(false);
        progressDialog.show();
        preferences = getSharedPreferences(LoginActivity.SHAREDPREFS,MODE_PRIVATE);
        SOID = preferences.getString("UID","");

        back.setOnClickListener(this);
    }

    @Override
    public void itemClicked(int position) {
        Dealer dealer=lists.get(position);
        Intent intent=new Intent(DealerList.this,PoList.class);
        intent.putExtra("Data",dealer);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if(v == back) finish();
    }
}
