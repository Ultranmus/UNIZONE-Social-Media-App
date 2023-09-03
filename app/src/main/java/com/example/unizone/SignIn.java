package com.example.unizone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.unizone.databinding.ActivitySignInBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class SignIn extends AppCompatActivity {

    private ActivitySignInBinding bind;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind=ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());


        auth= FirebaseAuth.getInstance();

        ProgressDialog pg=new ProgressDialog(this);
        pg.setTitle("Login Account!");
        pg.setMessage("Logging into the account");
        bind.button.setOnClickListener(view -> {
            String email=bind.editTextTextEmailAddress.getText().toString().trim();
            String password=bind.editTextNumberPassword.getText().toString().trim();
            if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
                pg.show();
                auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                    pg.dismiss();
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
                        Intent intent =new Intent(getApplicationContext(),Tabs.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else{
                Toast.makeText(this, "Fields Empty!!", Toast.LENGTH_SHORT).show();
            }
        });
        bind.noAccount.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(),SignUp.class));
            finish();
        });
        bind.google.setOnClickListener(view -> {

        });

    }

    @Override
    protected void onStart() {
        final FirebaseUser current_user= auth.getCurrentUser();
        if (current_user != null) {
            startActivity(new Intent(getApplicationContext(), Tabs.class));
            finish();
        }
        super.onStart();
    }
}