package com.example.unizone;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.unizone.databinding.ActivityCreatePostBinding;
import com.example.unizone.model.connection_model;
import com.example.unizone.model.other_post_model;
import com.example.unizone.model.prf_post_model;
import com.example.unizone.model.user_model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Date;
import java.util.Objects;

public class create_post extends AppCompatActivity {
private ActivityCreatePostBinding binding;
private final int gallery_post=121;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCreatePostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        storage=FirebaseStorage.getInstance();
        firestore.collection("Users").document(auth.getUid()).addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null){
                    Toast.makeText(create_post.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
                else{
                    if(value!=null){
                        user_model userModel = value.toObject(user_model.class);
                        assert userModel != null;
                        Glide.with(getApplicationContext()).load(userModel.getProfile()).error(R.drawable.profile).placeholder(R.drawable.profile).into(binding.prfImg);

                    }
                }
            }
        });
        binding.text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                   binding.textView16.setText(s);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.imageButton.setOnClickListener(v -> {
            Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent,gallery_post);
        });

    }


    //for creating post
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            if(resultCode==RESULT_OK){
                if(requestCode==gallery_post){
                    binding.postImg.setImageURI(data.getData());
                    binding.makePost.setVisibility(View.VISIBLE);
                    binding.postImg.setImageURI(data.getData());
                    binding.makePost.setOnClickListener(v -> {
                        ProgressDialog dialog=new ProgressDialog(create_post.this);
                        dialog.setTitle("Creating Post");
                        dialog.setMessage("Post is being created!");
                        dialog.setIcon(R.drawable.ic_baseline_save_24);
                        dialog.setCancelable(false);
                        dialog.show();
                        try {
                            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                        long time= new Date().getTime();
                        String timeS= String.valueOf(time);

                        storage.getReference().child(Objects.requireNonNull(auth.getUid())).child("posts").child(timeS).putFile(data.getData()).addOnSuccessListener(taskSnapshot -> storage.getReference().child(auth.getUid()).child("posts").child(timeS).getDownloadUrl().addOnSuccessListener(uri -> firestore.collection("Users").document(auth.getUid()).collection("posts").document(timeS).set(new prf_post_model(uri.toString(),binding.textView16.getText().toString(),time)).addOnSuccessListener(unused -> {
                            firestore.collection("Users").document(auth.getUid()).collection("Connections").addSnapshotListener(create_post.this, (value, error) -> {
                                if(value!=null){

                                    for(QueryDocumentSnapshot snapshot:value) {
                                        connection_model model = snapshot.toObject(connection_model.class);
                                        firestore.collection("Users").document(model.getAuth_id()).collection("OtherPosts").document(timeS + auth.getUid()).set(new other_post_model(auth.getUid(), timeS)).addOnSuccessListener(unused1 -> {
                                        })
                                                .addOnFailureListener(e -> {
                                                });
                                    }
                                        dialog.dismiss();
                                        Toast.makeText(create_post.this, "Post created successfully!!", Toast.LENGTH_SHORT).show();
                                        finish();

                                }else{
                                    dialog.dismiss();
                                }
                            });
                        }).addOnFailureListener(e -> {
                            dialog.dismiss();
                            Toast.makeText(create_post.this, "Failed to create post!", Toast.LENGTH_SHORT).show();
                        }))
                                .addOnFailureListener(e -> {
                                    dialog.dismiss();
                                    Toast.makeText(create_post.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                                }))
                                .addOnFailureListener(e -> {
                                    dialog.dismiss();
                                    Toast.makeText(create_post.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                });
                    });
                }
              }
            }
    }
}