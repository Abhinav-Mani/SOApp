package com.maxpetroleum.soapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.maxpetroleum.soapp.Activity.DealerList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button dealer;

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
    }

    @Override
    public void onClick(View view) {
        if(view==dealer){
            Intent intent=new Intent(MainActivity.this, DealerList.class);
            startActivity(intent);
        }
    }
}
