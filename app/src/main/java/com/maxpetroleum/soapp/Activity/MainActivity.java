package com.maxpetroleum.soapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
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
    String uid;
    TextView name,email;
    SharedPreferences preferences;
    ImageView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        setListeneres();
        setDetails();
    }

    private void setDetails() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("UserData").child("Sales officer").child(uid);
        ref.keepSynced(true);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name.setText("Name: "+dataSnapshot.child("name").getValue().toString());
                email.setText("Email: "+dataSnapshot.child("email").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setListeneres() {
        dealer.setOnClickListener(this);
    }

    private void init() {

        dealer=findViewById(R.id.dealer);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        logout = findViewById(R.id.logout);
        GRADE_NAME=new ArrayList<>();
        GRADE_RATE=new ArrayList<>();
        getRate();

        preferences = getSharedPreferences(LoginActivity.SHAREDPREFS,MODE_PRIVATE);
        uid = preferences.getString("UID","");

        if(!isOnline()){
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setCancelable(false);
            dialog.setTitle("Alert");
            dialog.setMessage("You are not connected to internet!");
            dialog.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            dialog.show();
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("Alert");
                dialog.setMessage("Are you sure to Log out?");
                dialog.setPositiveButton(MainActivity.this.getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getSharedPreferences(LoginActivity.SHAREDPREFS,MODE_PRIVATE).edit().clear().apply();
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(MainActivity.this,LoginActivity.class));
                        finishAffinity();
                    }
                });
                dialog.setNegativeButton(MainActivity.this.getResources().getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
    }

    public static void getRate(){
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference();
        ref.child("GradeRate").child("list").keepSynced(true);
        ref.child("GradeRate").child("list")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        GRADE_RATE.clear();
                        GRADE_NAME.clear();
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

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}