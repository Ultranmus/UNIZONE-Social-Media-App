package com.example.unizone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.unizone.databinding.ActivityUsernameBinding;
import com.example.unizone.model.user_model;
import com.example.unizone.model.usernames_model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class Username extends AppCompatActivity {
    private ActivityUsernameBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUsernameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        ProgressDialog pg=new ProgressDialog(this);
        pg.setTitle("Creating Account!");
        pg.setMessage("The account is being created");
        binding.buttonN.setOnClickListener(view -> {

        String username=binding.username.getText().toString().trim();
        if(!TextUtils.isEmpty(username)) {
            if (username.length()>3) {
                pg.show();
                firestore.collection("Users").whereEqualTo("username", username).get().addOnSuccessListener(queryDocumentSnapshots -> {
                            if (queryDocumentSnapshots.isEmpty()) {

                                Intent intent = getIntent();
                                String email = intent.getStringExtra("email");
                                String password = intent.getStringExtra("password");
                                userCreate(pg, username, email, password);
                            } else {
                                pg.dismiss();
                                Toast.makeText(getApplicationContext(), username + " is already in use.", Toast.LENGTH_SHORT).show();
                            }

                        })
                        .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
            }
            else{
                binding.username.setError("Username need to be atleast 3 Characters long");
                binding.username.requestFocus();
            }
        }else {
                Toast.makeText(getApplicationContext(), "Enter Username!", Toast.LENGTH_SHORT).show();
            }

        });

    }
    public void userCreate(ProgressDialog pg,String username,String email,String password){
    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {

            if(task.isSuccessful()) {
                firestore.collection("Users").document(Objects.requireNonNull(auth.getCurrentUser()).getUid()).set(new user_model(email, password, username))
                        .addOnSuccessListener(unused ->
                            firestore.collection("Usernames").document(username).set(new usernames_model(auth.getCurrentUser().getUid()))
                                .addOnSuccessListener(unused1 -> {
                                    pg.dismiss();
                                    Intent intent=new Intent(Username.this, Tabs.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                    Toast.makeText(getApplicationContext(), "Account Created Successfully!", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    pg.dismiss();
                                    Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }))
                        .addOnFailureListener(e -> {
                            pg.dismiss();
                            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
            else{
                        pg.dismiss();
                Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
    });

    }
}