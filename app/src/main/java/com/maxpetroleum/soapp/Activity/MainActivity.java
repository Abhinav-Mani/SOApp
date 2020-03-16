package com.maxpetroleum.soapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.maxpetroleum.soapp.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button dealer;
    public static ArrayList<String> GRADE_NAME,GRADE_RATE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        setListeneres();
    }

    private void setListeneres() {
        dealer.setOnClickListener(this);
    }

    private void init() {

        dealer=findViewById(R.id.dealer);
        GRADE_NAME=new ArrayList<>();
        GRADE_RATE=new ArrayList<>();
        getRate();
    }

    public static void getRate(){
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference();
        ref.child("GradeRate").child("list").keepSynced(true);
        ref.child("GradeRate").child("list")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            GRADE_NAME.add(ds.child("name").getValue().toString());
                            GRADE_RATE.add(ds.child("rate").getValue().toString());
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void onClick(View view) {
        if(view==dealer){
            Intent intent=new Intent(MainActivity.this, DealerList.class);
            startActivity(intent);
        }
    }
}