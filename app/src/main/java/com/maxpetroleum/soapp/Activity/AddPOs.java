package com.maxpetroleum.soapp.Activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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


public class AddPOs extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    final static String TASKS = "poList";
    public static int EDIT_FLAG = 1001;
    public static int VIEW_FLAG = 1002;
    static long net_amt;
    static TextView net_amt_tv;
    static int position;
    final Calendar myCalendar = Calendar.getInstance();
    EditText date, po_no, qnt;
    Spinner grade;
    Button add, save;
    ListView listView;
    String grade_name;
    ArrayList<GradeInfo> gradeInfo;
    ArrayList<PO_Info> datalist;
    ImageView back;
    int flag, edit_position;
    String rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pos);
        init();
        setDate();
    }

    public void init() {
        date = findViewById(R.id.date);
        po_no = findViewById(R.id.po_no);
        add = findViewById(R.id.add_button);
        grade = findViewById(R.id.grade);
        listView = findViewById(R.id.rv_quant);
        qnt = findViewById(R.id.qnt);
        back = findViewById(R.id.back);
        save = findViewById(R.id.save);
        net_amt_tv = findViewById(R.id.net_amt);
        net_amt = 0;

        datalist = new ArrayList<>();
        gradeInfo = new ArrayList<>();

        position = 0;
        flag = getIntent().getIntExtra("flag", 0);
        edit_position = getIntent().getIntExtra("position", 0);
        if (flag == EDIT_FLAG) {
//            setEditData();
        }

        back.setOnClickListener(this);
        grade.setOnItemSelectedListener(this);
        add.setOnClickListener(this);
        save.setOnClickListener(this);
    }

    private void setEditData() {
        PO_Info info = MainActivity.PO_DETAILS.get(edit_position);
        date.setText(info.getPo_Date());
        po_no.setText(info.getPo_no());
        gradeInfo = info.getGrade();
        QntAdapter adapter = new QntAdapter(this, info.getGrade(), EDIT_FLAG);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        for (int i = 0; i < info.getGrade().size(); i++) {
            net_amt += Integer.parseInt(gradeInfo.get(gradeInfo.size() - 1).getQnty()) * Integer.parseInt(gradeInfo.get(gradeInfo.size() - 1).getRate());
        }
        net_amt_tv.setText("Net Payable: ₹" + net_amt);
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

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(
                        AddPOs.this,
                        dateSetListener,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));

                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.show();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "dd-MM-yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        date.setText(sdf.format(myCalendar.getTime()));
    }

    public void setQntList() {
        gradeInfo.add(new GradeInfo(grade_name, qnt.getText().toString(), rate));
//        Toast.makeText(this, qnty.size()+"", Toast.LENGTH_SHORT).show();
        QntAdapter adapter = new QntAdapter(this, gradeInfo, EDIT_FLAG);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if (v == add) {
            qnt.setEnabled(false);
            qnt.setEnabled(true);
            if (!qnt.getText().toString().isEmpty() && !grade_name.isEmpty()) {
                setQntList();
                net_amt += Integer.parseInt(gradeInfo.get(gradeInfo.size() - 1).getQnty()) * Integer.parseInt(gradeInfo.get(gradeInfo.size() - 1).getRate());
                net_amt_tv.setText("Net Payable: ₹" + net_amt);
            } else {
                qnt.setError("Empty");
            }
        } else if (v == back) finish();
        else if (v == save) {
            prepareDatalist();
        }
    }

    private void prepareDatalist() {
        String uid = getSharedPreferences(LoginActivity.SHAREDPREFS, MODE_PRIVATE).getString("UID", "");

        if (date.getText().toString().isEmpty()) date.setError("Cannot be Empty");
        else if (po_no.getText().toString().isEmpty()) po_no.setError("Cannot be Empty");
        else if (gradeInfo.size() == 0)
            Toast.makeText(this, "Enter grade", Toast.LENGTH_SHORT).show();
        else {
            PO_Info po_info = new PO_Info(po_no.getText().toString(), net_amt + "", date.getText().toString(), gradeInfo);

            if (flag == EDIT_FLAG) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("PO").child(uid);
                ref.child(MainActivity.PO_ID.get(edit_position)).setValue(po_info);
                Intent intent = new Intent(this, PoDetail.class);
                intent.putExtra("position", position);
                startActivity(intent);
                finish();
            } else {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("PO").child(uid);
                ref.push().setValue(po_info);
                startActivity(new Intent(this, POList.class));
                finish();
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String[] s = this.getResources().getStringArray(R.array.grade_name);
        grade_name = s[position];
//        rate = MainActivity.GRADE_RATE.get(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        grade_name = "";
//        rate = MainActivity.GRADE_RATE.get(0);
    }

    @Override
    public void onBackPressed() {
//        startActivity(new Intent(this, POList.class));
        finish();
    }

    static class QntAdapter extends ArrayAdapter<ArrayList<GradeInfo>> {
        Context context;
        ArrayList<GradeInfo> grade;
        int flag;

        QntAdapter(Context context, ArrayList<GradeInfo> grade, int flag) {
            super(context, R.layout.item_grades, Collections.singletonList(grade));
            this.context = context;
            this.grade = grade;
            this.flag = flag;
        }

        @Override
        public int getCount() {
            return grade.size();
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.item_grades, parent, false);
            TextView gname = view.findViewById(R.id.gname);
            TextView qnt = view.findViewById(R.id.qnt);
            TextView amt = view.findViewById(R.id.amt);
            ImageView iv = view.findViewById(R.id.delete);

            if (flag == EDIT_FLAG) {
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AddPOs.net_amt -= Integer.parseInt(grade.get(position).getQnty()) * Integer.parseInt(grade.get(position).getRate());
                        grade.remove(position);
                        net_amt_tv.setText("Net Payable: ₹" + net_amt);
                        notifyDataSetChanged();
                    }
                });
            } else if (flag == VIEW_FLAG) {
                iv.setVisibility(View.GONE);
            }
            gname.setText(grade.get(position).getGrade_name());
            qnt.setText(grade.get(position).getQnty());
            amt.setText("₹" + (Integer.parseInt(grade.get(position).getQnty()) * Integer.parseInt(grade.get(position).getRate())));
            return view;
        }
    }
}
