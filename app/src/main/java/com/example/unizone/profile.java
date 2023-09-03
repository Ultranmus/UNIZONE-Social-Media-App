package com.example.unizone;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.unizone.databinding.ActivityProfileBinding;
import com.example.unizone.model.adapters.connection_prf_adapter;
import com.example.unizone.model.adapters.prf_post_adapter;
import com.example.unizone.model.connection_model;
import com.example.unizone.model.prf_post_model;
import com.example.unizone.model.user_model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Objects;

public class profile extends AppCompatActivity {
    private ActivityProfileBinding bind;
    private ArrayList<connection_model> list;
    private ArrayList<prf_post_model> list_prf_post;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    private final int gallery=101;
    private final int gallery_bg=102;
    private connection_prf_adapter connectionAdapterPrf;
    prf_post_adapter prfPostAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind=ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());


        auth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        storage=FirebaseStorage.getInstance();
        list =new ArrayList<>();

                bind.prfImg.setOnClickListener(v -> {

                    Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent,gallery);
                });
                bind.diagonalLayout.setOnClickListener(v -> {

                    Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent,gallery_bg);
                });

                bind.aboutPrfPage.setOnClickListener(v -> {
                    Dialog dialog=new Dialog(profile.this);
                    dialog.setContentView(R.layout.about_dialog);
                    EditText about= dialog.findViewById(R.id.about);
                    Button save=dialog.findViewById(R.id.button2);
                    about.setText(bind.aboutPrfPage.getText());
                    save.setOnClickListener(v1 -> {
                        String about_user=about.getText().toString().trim();
                        if(!TextUtils.isEmpty(about_user)){
                            dialog.dismiss();
                            firestore.collection("Users").document(Objects.requireNonNull(auth.getUid())).update("about",about_user).addOnSuccessListener(unused -> Toast.makeText(profile.this,"Saved", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e -> Toast.makeText(profile.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
                        }
                        else{
                            about.setError("Field Empty");
                            about.requestFocus();
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                });

//        connections
        bind.connectionPrfRv.setLayoutManager(new LinearLayoutManager(profile.this,LinearLayoutManager.HORIZONTAL,false));
        connectionAdapterPrf=new connection_prf_adapter(profile.this, list,firestore);
        bind.connectionPrfRv.setAdapter(connectionAdapterPrf);

//      post
        list_prf_post =new ArrayList<>();
        prfPostAdapter=new prf_post_adapter(profile.this,list_prf_post);
        bind.postPrfRv.setNestedScrollingEnabled(false);
        bind.postPrfRv.setLayoutManager(new GridLayoutManager(this,3));
        bind.postPrfRv.setAdapter(prfPostAdapter);
    }


//for profile and profile_bg
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            if(resultCode==RESULT_OK){
                if(requestCode==gallery){
                    storage.getReference().child(Objects.requireNonNull(auth.getUid())).child("profile").putFile(data.getData()).addOnSuccessListener(taskSnapshot -> storage.getReference().child(auth.getUid()).child("profile").getDownloadUrl().addOnSuccessListener(uri -> firestore.collection("Users").document(auth.getUid()).update("profile",uri.toString())
                            .addOnSuccessListener(unused -> Toast.makeText(profile.this, "Image Uploaded!", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(profile.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show()))
                                    .addOnFailureListener(e -> Toast.makeText(profile.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show()))
                            .addOnFailureListener(e -> Toast.makeText(profile.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
                }
                if(requestCode==gallery_bg){
                    storage.getReference().child(Objects.requireNonNull(auth.getUid())).child("profile_bg").putFile(data.getData()).addOnSuccessListener(taskSnapshot -> storage.getReference().child(auth.getUid()).child("profile_bg").getDownloadUrl().addOnSuccessListener(uri -> firestore.collection("Users").document(auth.getUid()).update("profile_bg",uri.toString())
                            .addOnSuccessListener(unused -> Toast.makeText(profile.this, "Image Uploaded!", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(profile.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show()))
                            .addOnFailureListener(e -> Toast.makeText(profile.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show()))
                            .addOnFailureListener(e -> Toast.makeText(profile.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
                }
            }
        }


        }


    @Override
    protected void onStart() {
        super.onStart();

        firestore.collection("Users").document(Objects.requireNonNull(auth.getUid())).collection("Connections").addSnapshotListener(profile.this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value!=null){

                    list.clear();
                    for(QueryDocumentSnapshot snapshot:value){
                        connection_model model=snapshot.toObject(connection_model.class);
                        list.add(model);
//
                    }
                    connectionAdapterPrf.notifyDataSetChanged();
                }

            }
        });




        firestore.collection("Users").document(auth.getUid()).collection("posts").orderBy("time", Query.Direction.DESCENDING).addSnapshotListener(profile.this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null){
                    bind.textView15.setVisibility(View.INVISIBLE);
//                    Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(value!=null && !value.isEmpty()){
                    list_prf_post.clear();
                    bind.textView15.setVisibility(View.VISIBLE);
                    for (QueryDocumentSnapshot snapshot:value
                    ) {
                        prf_post_model prfPostModel=snapshot.toObject(prf_post_model.class);
                        prfPostModel.setAuth_id(auth.getUid());
                        list_prf_post.add(prfPostModel);
                    }
                    prfPostAdapter.notifyDataSetChanged();
                }
            }
        });


//        show profile and profile_bg
        firestore.collection("Users").document(auth.getUid()).addSnapshotListener(profile.this,(value, error) -> {
            if (value != null && value.exists()) {
                user_model userModel = value.toObject(user_model.class);
                bind.usernamePrfPage.setText(Objects.requireNonNull(userModel).getUsername());
                bind.aboutPrfPage.setText(userModel.getAbout());
                if(userModel.getProfile()!=null) {
                    Glide.with(getApplicationContext()).load(userModel.getProfile()).error(R.drawable.profile).placeholder(R.drawable.profile).into(bind.prfImg);
                }
                if(userModel.getProfile_bg()!=null) {
                    Glide.with(getApplicationContext()).load(userModel.getProfile_bg()).error(R.drawable.profile).placeholder(R.drawable.profile).into(bind.diagonalLayout);
                }
            }
        });

//        no.of followers
        firestore.collection("Users").document(auth.getUid()).collection("Follower").addSnapshotListener(profile.this, (value, error) -> {
            if(value!=null){
                String followers= String.valueOf(value.size());
                bind.textView11.setText(followers);
            }
        });

//        no. of following
        firestore.collection("Users").document(auth.getUid()).collection("Following").addSnapshotListener(profile.this, (value, error) -> {
            if(value!=null){
                String following= String.valueOf(value.size());
                bind.textView12.setText(following);
            }
        });

//        no. of connections
        firestore.collection("Users").document(auth.getUid()).collection("Connections").addSnapshotListener(profile.this, (value, error) -> {
            if(value!=null){
                String connections= String.valueOf(value.size());
                if (connections.equals("0")) {
                    bind.textView14.setVisibility(View.INVISIBLE);
                    bind.view3.setVisibility(View.INVISIBLE);
                }
                else{
                    bind.textView14.setVisibility(View.VISIBLE);
                    bind.view3.setVisibility(View.VISIBLE);
                }
                bind.textView13.setText(connections);
            }
        });



    }
}