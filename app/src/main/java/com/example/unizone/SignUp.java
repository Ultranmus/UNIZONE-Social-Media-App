package com.example.unizone;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.unizone.databinding.ActivitySignUpBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SignUp extends AppCompatActivity {

   private ActivitySignUpBinding bind;
   private FirebaseAuth auth;
    String email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind=ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());



        auth= FirebaseAuth.getInstance();


        bind.button.setOnClickListener(view -> {


        email=bind.editTextTextEmailAddress.getText().toString().trim();
        password=bind.editTextNumberPassword.getText().toString().trim();
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){

            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {



                bind.editTextTextEmailAddress.setError("incorrect email!");
                bind.editTextTextEmailAddress.requestFocus();

            }
            else if(!(password.length() >=6)){
                bind.editTextNumberPassword.setError("password can't be less than 6 character!");
                bind.editTextNumberPassword.requestFocus();
            }
            else{
                Intent i = new Intent(this, Username.class);
                i.putExtra("email", email);
                i.putExtra("password", password);
                startActivity(i);
            }
        }
        else{

            Toast.makeText(this, "Fields Empty!!", Toast.LENGTH_SHORT).show();
        }
        });
        bind.haveAccount.setOnClickListener(view -> {
            startActivity(new Intent(SignUp.this,SignIn.class));
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