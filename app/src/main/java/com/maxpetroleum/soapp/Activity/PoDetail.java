package com.maxpetroleum.soapp.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.maxpetroleum.soapp.Model.GradeInfo;
import com.maxpetroleum.soapp.Model.PO_Info;
import com.maxpetroleum.soapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

public class PoDetail extends AppCompatActivity implements View.OnClickListener {
    PO_Info po_info;
    String key;
    String DealerUID;
    EditText select_date;
    TextView status,po_no,amount,poDate,deliveryDate,billDate,paymentDate;
    Button Accept,Decline,edit;
    final Calendar myCalendar = Calendar.getInstance();
    boolean dateSet;
    FirebaseDatabase database;
    DatabaseReference myRef;
    String bill_Date;
    LinearLayout contols;
    Button grades;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_po_detail);

        check();

        init();

        setValues();

        setDate();

        setListners();
    }

    private void setListners() {
        Accept.setOnClickListener(this);
        grades.setOnClickListener(this);
        edit.setOnClickListener(this);
    }

    private void setValues() {
        po_no.setText(po_info.getPo_no());
        amount.setText("₹"+po_info.getAmount());
        poDate.setText(po_info.getPo_Date());
        deliveryDate.setText(po_info.getDelivery_date());
        billDate.setText(po_info.getBill_date());
        paymentDate.setText(po_info.getPayment_date());
        if(po_info.getBill_date()!=null){
            contols.setVisibility(View.GONE);
        }

        if(po_info.getPayment_date() == null){
            status.setText("Pending\nPayment Date");
            status.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        }
        else if(po_info.getBill_date() == null){
            status.setText("Pending\nBill Date");
            status.setTextColor(getResources().getColor(android.R.color.holo_red_light));
        }
        else if(po_info.getDelivery_date() == null){
            status.setText("Pending\nDelivery Date");
            status.setTextColor(getResources().getColor(android.R.color.holo_red_light));
        }
        else if(po_info.getDelivery_date() != null){
            status.setText("Deal Closed");
            status.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        }
    }

    private void setDate() {
        final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        select_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(PoDetail.this, dateSetListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "dd-MM-yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateSet=true;
        bill_Date=sdf.format(myCalendar.getTime());
        select_date.setText(sdf.format(myCalendar.getTime()));
    }

    private void init() {
        edit=findViewById(R.id.edit);
        dateSet=false;
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();
        status=findViewById(R.id.status_txt);
        po_no=findViewById(R.id.po_no);
        amount=findViewById(R.id.amt);
        poDate=findViewById(R.id.po_date);
        deliveryDate=findViewById(R.id.delivery_date);
        billDate=findViewById(R.id.bill_date);
        grades=findViewById(R.id.view_grades_but);

        paymentDate=findViewById(R.id.payment_date);

        select_date=findViewById(R.id.select_date);
        Accept=findViewById(R.id.send_button);

        contols=findViewById(R.id.control);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void check() {
        Intent intent=getIntent();
        if(intent.hasExtra("Key")&&intent.hasExtra("Data")){
            po_info=(PO_Info) intent.getSerializableExtra("Data");
            key=intent.getStringExtra("Key");
            DealerUID = intent.getStringExtra("DealerUID");
        }
        else {
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        if(view==Accept){
            if(!dateSet){
                Toast.makeText(this,"Set the Date",Toast.LENGTH_SHORT).show();
                return;
            }
            po_info.setBill_date(bill_Date);
            myRef.child("Dealer").child(PoList.dealer.getUid()).child(key).setValue("Accepted");
            myRef.child("PO").child(DealerUID).child(key).setValue(po_info).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    billDate.setText(bill_Date);
                    contols.setVisibility(View.GONE);
                }
            });
        } else if(view==grades){
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Grades");
            ListView listView = new ListView(this);
            QntAdapter adapter = new QntAdapter(this, po_info.getGrade());
            listView.setAdapter(adapter);
            dialog.setView(listView);
            dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }else if(view==edit){
            Intent intent=new Intent(this,AddPOs.class);
            intent.putExtra("flag",AddPOs.EDIT_FLAG);
            intent.putExtra("Data",po_info);
            intent.putExtra("Key",key);
            startActivity(intent);
        }
    }

    static class QntAdapter extends ArrayAdapter<ArrayList<GradeInfo>> {
        Context context; ArrayList<GradeInfo> grade;int flag;

        QntAdapter(Context context, ArrayList<GradeInfo> grade){
            super(context,R.layout.item_grades, Collections.singletonList(grade));
            this.context = context;
            this.grade = grade;
        }

        @Override
        public int getCount() {
            return grade.size();
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.item_grades,parent,false);
            TextView gname = view.findViewById(R.id.gname);
            TextView qnt = view.findViewById(R.id.qnt);
            TextView amt = view.findViewById(R.id.amt);

            gname.setText(grade.get(position).getGrade_name());
            qnt.setText(grade.get(position).getQnty());
            amt.setText("₹"+(Integer.parseInt(grade.get(position).getQnty())*Integer.parseInt(grade.get(position).getRate())));
            return view;
        }
    }
}
