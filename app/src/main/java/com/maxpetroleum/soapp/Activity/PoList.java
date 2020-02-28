package com.maxpetroleum.soapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.maxpetroleum.soapp.Adapter.PoListAdapter;
import com.maxpetroleum.soapp.Adapter.ViewPagerAddapter;
import com.maxpetroleum.soapp.Model.Dealer;
import com.maxpetroleum.soapp.Model.PO_Info;
import com.maxpetroleum.soapp.R;

import java.util.HashMap;

public class PoList extends AppCompatActivity implements PoListAdapter.ClickHandler {

    ViewPager viewPager;
    ViewPagerAddapter addapter;
    TabLayout tabLayout;
    public static Dealer dealer;
    public static HashMap<String,String> hashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_po_list);

        check();

        init();
    }

    private void check() {
        Intent intent=getIntent();
        if(intent.hasExtra("Data")){
            dealer= (Dealer) intent.getSerializableExtra("Data");
        }else {
            finish();
        }

    }

    private void init() {
        viewPager=findViewById(R.id.pager);
        addapter=new ViewPagerAddapter(getSupportFragmentManager());
        viewPager.setAdapter(addapter);
        tabLayout=findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        hashMap=new HashMap<>();

    }

    @Override
    public void onItemClicked(PO_Info po_info) {
        Intent intent=new Intent(this,PoDetail.class);
        intent.putExtra("Key",hashMap.get(po_info.getPo_no()));
        intent.putExtra("Data",po_info);
        startActivity(intent);
    }
}
