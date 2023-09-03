
package com.example.unizone;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.unizone.databinding.ActivityFollowUserBinding;
import com.example.unizone.model.adapters.connection_adapter_prf;
import com.example.unizone.model.adapters.prf_post_adapter;
import com.example.unizone.model.connection_model;
import com.example.unizone.model.other_post_model;
import com.example.unizone.model.prf_post_model;
import com.example.unizone.model.user_model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class follow_user extends AppCompatActivity {
        private ActivityFollowUserBinding bind;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
        private  String id;
    private connection_adapter_prf connectionAdapterPrf;
    private ProgressDialog dialog;
    private ArrayList<connection_model> list;
    prf_post_adapter prfPostAdapter;
    ArrayList<prf_post_model> list_prf_post;



    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            bind=ActivityFollowUserBinding.inflate(getLayoutInflater());
            setContentView(bind.getRoot());

            dialog=new ProgressDialog(this);
            dialog.setTitle("Connecting");
            dialog.setMessage("Currently Working on task");
           auth = FirebaseAuth.getInstance();
            firestore=FirebaseFirestore.getInstance();
            list = new ArrayList<>();

            Intent intent_search=getIntent();
            id=intent_search.getStringExtra("id");
            if(id==null){
                Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
                finish();
            }

//connections
        bind.connectionPrfRv.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        connectionAdapterPrf=new connection_adapter_prf(follow_user.this, list,firestore);
        bind.connectionPrfRv.setAdapter(connectionAdapterPrf);

//show post
        list_prf_post = new ArrayList<>();

            prfPostAdapter=new prf_post_adapter(follow_user.this, list_prf_post);
            bind.postPrfRv.setNestedScrollingEnabled(false);
            bind.postPrfRv.setLayoutManager(new GridLayoutManager(follow_user.this,3));
            bind.postPrfRv.setAdapter(prfPostAdapter);

        long timel=new Date(new Date().getTime()-604800000L).getTime();

        //            follow/connection
            bind.followBtn.setOnClickListener(v ->
            {dialog.show();
                    firestore.collection("Users").document(id).collection("Follower").document(Objects.requireNonNull(auth.getUid())).set(new connection_model(auth.getUid(), new Date().getTime())).addOnSuccessListener(unused -> firestore.collection("Users").document(auth.getUid()).collection("Following").document(id).set(new connection_model(id,new Date().getTime()))
                .addOnSuccessListener(unused1 -> {
                    firestore.collection("Users").document(id).collection("Following").document(Objects.requireNonNull(auth.getUid())).addSnapshotListener(this, (value, error) -> {
                        if(error!=null){
                            dialog.dismiss();
                        }
                        if (value != null && value.exists()) {
                            firestore.collection("Users").document(id).collection("Connections").document(auth.getUid()).set(new connection_model(auth.getUid(), new Date().getTime())).addOnSuccessListener(unused2 ->
                            firestore.collection("Users").document(auth.getUid()).collection("Connections").document(id).set(new connection_model(id, new Date().getTime())).addOnSuccessListener(unused21 ->
                            {
                                long time=new Date(new Date().getTime()-604800000L).getTime();

                                firestore.collection("Users").document(id).collection("posts").whereGreaterThan("time",time).addSnapshotListener(follow_user.this, (value12, error12) -> {
                                    if(value12 !=null){
                                        for (QueryDocumentSnapshot snapshot: value12
                                             ) {
                                            prf_post_model prfPostModel=snapshot.toObject(prf_post_model.class);
                                            firestore.collection("Users").document(auth.getUid()).collection("OtherPosts").document(prfPostModel.getTime()+id).set(new other_post_model(id,String.valueOf(prfPostModel.getTime())));
                                        }
                                    }
                                }); firestore.collection("Users").document(auth.getUid()).collection("posts").whereGreaterThan("time",time).addSnapshotListener(follow_user.this, (value1, error1) -> {
                                    if(value1 !=null){
                                        for (QueryDocumentSnapshot snapshot: value1
                                             ) {
                                            prf_post_model prfPostModel=snapshot.toObject(prf_post_model.class);
                                            firestore.collection("Users").document(id).collection("OtherPosts").document(prfPostModel.getTime()+auth.getUid()).set(new other_post_model(auth.getUid(), String.valueOf(prfPostModel.getTime())));
                                        }
                                    }
                                });
                                dialog.dismiss();
                                Toast.makeText(follow_user.this, "Connection Established", Toast.LENGTH_SHORT).show();
                            }).addOnFailureListener(e -> {
                                dialog.dismiss();
                                Toast.makeText(follow_user.this, "Connection Failed", Toast.LENGTH_SHORT).show();
                            })).addOnFailureListener(e -> {
                                dialog.dismiss();
                                Toast.makeText(follow_user.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            });
                        }
                        else{
                            dialog.dismiss();
                        }
                    });
                    Toast.makeText(follow_user.this, "Followed", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }))
                .addOnFailureListener(e -> {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Failed to Follow", Toast.LENGTH_SHORT).show();
                });
            });
    }



    @SuppressLint("NotifyDataSetChanged")
    @Override
        protected void onStart() {
            super.onStart();


        firestore.collection("Users").document(id).collection("Connections").addSnapshotListener(follow_user.this, (value, error) -> {
            if(value!=null){

                list.clear();
                for(QueryDocumentSnapshot snapshot:value){
                    connection_model model=snapshot.toObject(connection_model.class);
                    list.add(model);
                }
                connectionAdapterPrf.notifyDataSetChanged();
            }

        });



        firestore.collection("Users").document(id).collection("posts").orderBy("time", Query.Direction.DESCENDING).addSnapshotListener(follow_user.this, (value, error) -> {
            if(error!=null){
                bind.textView15.setVisibility(View.INVISIBLE);
                return;
            }

            if(value!=null && !value.isEmpty()){
                list_prf_post.clear();
                bind.textView15.setVisibility(View.VISIBLE);
                for (QueryDocumentSnapshot snapshot:value
                ) {
                    prf_post_model prfPostModel=snapshot.toObject(prf_post_model.class);
                    prfPostModel.setAuth_id(id);
                    list_prf_post.add(prfPostModel);
                }
                prfPostAdapter.notifyDataSetChanged();
            }
        });



//            show profile and profile_bg
            firestore.collection("Users").document(id).addSnapshotListener(follow_user.this,(value, error) -> {
                if (value != null && value.exists()) {
                    user_model userModel = value.toObject(user_model.class);
                    bind.usernamePrfPage.setText(Objects.requireNonNull(userModel).getUsername());
                    bind.aboutPrfPage.setText(userModel.getAbout());
                    Glide.with(getApplicationContext()).load(userModel.getProfile()).error(R.drawable.profile).placeholder(R.drawable.profile).into(bind.prfImg);
                    Glide.with(getApplicationContext()).load(userModel.getProfile_bg()).error(R.drawable.profile).placeholder(R.drawable.profile).into(bind.diagonalLayout);

                }
            });

//            follow btn drawable change
        firestore.collection("Users").document(id).collection("Follower").document(Objects.requireNonNull(auth.getUid())).addSnapshotListener(follow_user.this, (value, error) -> {
            if (value != null && value.exists()) {
                bind.followBtn.setImageResource(R.drawable.ic_baseline_person_24);
                bind.followBtn.setClickable(false);
            }
        });


        //            no.of followers
        firestore.collection("Users").document(id).collection("Follower").addSnapshotListener(follow_user.this, (value, error) -> {
            if(value!=null){
                String followers= String.valueOf(value.size());
                bind.textView11.setText(followers);
            }
        });


//        no. of following
        firestore.collection("Users").document(id).collection("Following").addSnapshotListener(follow_user.this, (value, error) -> {
            if(value!=null){
                String following= String.valueOf(value.size());
                bind.textView12.setText(following);
            }
        });


//        no. of connections and hide connections textview if connections are zero
        firestore.collection("Users").document(id).collection("Connections").addSnapshotListener(follow_user.this, (value, error) -> {
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