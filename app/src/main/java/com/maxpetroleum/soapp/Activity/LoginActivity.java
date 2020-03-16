package com.maxpetroleum.soapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.maxpetroleum.soapp.R;

public class LoginActivity extends AppCompatActivity {

    public FirebaseAuth auth;
    public EditText etEmail,etPassword,etPassword1,etConfimPassword;
    public ProgressBar progressBar;
    String email,password;
    SharedPreferences preferences;
    LinearLayout email_login_form, change_password_form;
    public static final String SHAREDPREFS="My Data";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferences = getSharedPreferences(SHAREDPREFS,MODE_PRIVATE);
        auth = FirebaseAuth.getInstance();
        etEmail=findViewById(R.id.email);
        etPassword=findViewById(R.id.password);
        progressBar=findViewById(R.id.login_progress);
        etConfimPassword = findViewById(R.id.confirm_password1);
        etPassword1 = findViewById(R.id.password1);
        email_login_form = findViewById(R.id.email_login_form);
        change_password_form = findViewById(R.id.change_password_form);
        FirebaseUser user = auth.getCurrentUser();
        proceed(user);
    }
    public void proceed(FirebaseUser user){
        String text;
        text = preferences.getString("email","");
        if(user!=null){
            if(!text.isEmpty()){
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
            else{
                final SharedPreferences.Editor editor=preferences.edit();
                String text1 = email;
                text1 = text1.replace("@","a");
                text1 = text1.replace(".","d");
                editor.putString("email",text1);
                editor.apply();

                email_login_form.setVisibility(View.GONE);
                change_password_form.setVisibility(View.VISIBLE);

                findViewById(R.id.skip_for_now).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updatePasswordOnline(password);
                    }
                });
                findViewById(R.id.change_password_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!etPassword1.getText().toString().isEmpty() && etPassword1.getText().toString().equals(etConfimPassword.getText().toString())){
                            if(etPassword1.getText().toString().length()<=5){
                                Toast.makeText(LoginActivity.this, "Enter atleast 6 characters", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                progressBar.setVisibility(View.VISIBLE);
                                changePassword(etPassword1.getText().toString());
                            }
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }

    }

    private void changePassword(final String s) {
        final FirebaseUser user = auth.getCurrentUser();
        AuthCredential credential = EmailAuthProvider
                .getCredential(email, password);

        assert user != null;
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(s).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, "Password updated", Toast.LENGTH_SHORT).show();
                                        updatePasswordOnline(s);
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Error updating password", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                });
    }

    private void updatePasswordOnline(final String s) {
        final FirebaseUser user = auth.getCurrentUser();
        String temp = preferences.getString("email","");
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.keepSynced(true);
        ref.child("User").child("Sales officer").child(temp)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String uid;
                        uid = dataSnapshot.getValue().toString();
                        preferences.edit().putString("UID",uid).apply();
                        ref.child("UserData").child("Sales officer").child(uid).child("password").setValue(s);
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();

        findViewById(R.id.signIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=etEmail.getText().toString().toLowerCase().trim();
                password=etPassword.getText().toString().trim();
                etEmail.setText("");
                etPassword.setText("");
                progressBar.setVisibility(View.VISIBLE);
                if(!TextUtils.isEmpty(email.trim())&&!TextUtils.isEmpty(password.trim())) {
                    auth.signInWithEmailAndPassword(email, password)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    proceed(authResult.getUser());
                                    Toast.makeText(LoginActivity.this, "Log In Successful", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(LoginActivity.this, "Log In Failed!!!", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                }
            }
        });
    }
}