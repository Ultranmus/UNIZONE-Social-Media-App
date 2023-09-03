package com.example.unizone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.unizone.databinding.ActivityCommentsBinding;
import com.example.unizone.model.CommentModel;
import com.example.unizone.model.adapters.CommentAdapter;
import com.example.unizone.model.id;
import com.example.unizone.model.prf_post_model;
import com.example.unizone.model.user_model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class comments extends AppCompatActivity {
    private ActivityCommentsBinding binding;
    private String id,timeStamp;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private ArrayList<CommentModel> list;
    private String comment;
    private CommentAdapter commentAdapter;

    CommentAdapter.ItemClickListener itemClickListener;
    private String reply_To_id;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommentsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        list=new ArrayList<>();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        binding.editText.setOnTouchListener((view, event) -> {

            if (view.getId() == R.id.editText) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                    view.getParent().requestDisallowInterceptTouchEvent(false);
                }
            }
            return false;
        });

        binding.commentRv.setNestedScrollingEnabled(false);

        Intent intent_search = getIntent();
        id = intent_search.getStringExtra("ids");
        timeStamp = intent_search.getStringExtra("timeStamp");
        if (id == null || timeStamp == null) {
            Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
            finish();
        }


        binding.prfImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if(id.equals(auth.getUid())){
                    intent=new Intent(comments.this, profile.class);
                }
                else{
                    intent=new Intent(comments.this, follow_user.class);
                }
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });


        firestore.collection("Users").document(id).collection("posts").document(timeStamp).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot != null) {
                prf_post_model prfPostModel = documentSnapshot.toObject(prf_post_model.class);
                if (prfPostModel != null) {
                    Glide.with(getApplicationContext()).load(prfPostModel.getPost()).placeholder(R.drawable.profile).error(R.drawable.profile).into(binding.postImg);
                    if (prfPostModel.getDescription().isEmpty()) {
                        binding.description.setVisibility(View.GONE);
                    } else {
                        binding.description.setVisibility(View.VISIBLE);
                        binding.description.setText(prfPostModel.getDescription());
                    }
                }
            }
        }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Problem in loading post", Toast.LENGTH_SHORT).show());

        binding.textView6.setOnClickListener(v -> binding.text.setHint("Enter Comment"));

        itemClickListener= (reply_to_id, reply_to_username) -> {
            binding.text.setHint(reply_to_username);
            reply_To_id=reply_to_id;
            binding.viewDivider.getParent().requestChildFocus(binding.viewDivider,binding.viewDivider);
            binding.editText.requestFocus();
        };
        binding.send.setOnClickListener(v -> {
            comment= Objects.requireNonNull(binding.editText.getText()).toString().trim();
            if(TextUtils.isEmpty(comment)){
                binding.editText.setError("Please Enter something");
                binding.editText.requestFocus();
            }
            else{
                binding.editText.setText("");
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                } catch(Exception ignored) {
                }
                String username= Objects.requireNonNull(binding.text.getHint()).toString().trim();
                DateFormat formatter =  SimpleDateFormat.getDateTimeInstance();
                Date date = new Date();
                String timeS=formatter.format(date);
                if(username.equals("Enter Comment")){

                    firestore.collection("Users").document(id).collection("posts").document(timeStamp).collection("comments").document()
                            .set(new CommentModel(auth.getUid(),timeS,comment,String.valueOf(new Date().getTime()))).addOnSuccessListener(unused -> {
                                Toast.makeText(comments.this, "Commented", Toast.LENGTH_SHORT).show();
                                binding.editText.clearFocus();
//
                            })
                            .addOnFailureListener(e -> Toast.makeText(comments.this, "Failed to comment", Toast.LENGTH_SHORT).show());
                }
                else{
                    firestore.collection("Users").document(id).collection("posts").document(timeStamp).collection("comments").document(reply_To_id).collection("reply").document().set(new CommentModel(auth.getUid(),timeS,comment,String.valueOf(new Date().getTime()))).addOnSuccessListener(unused -> {
                        Toast.makeText(comments.this, "Commented", Toast.LENGTH_SHORT).show();
                        binding.editText.clearFocus();
                    }).addOnFailureListener(e -> Toast.makeText(comments.this, "Failed to comment", Toast.LENGTH_SHORT).show());
                }
            }
        });
        binding.textView5.setOnClickListener(v -> {
            firestore.collection("Users").document(id).collection("posts").document(timeStamp).collection("Like").document(Objects.requireNonNull(auth.getUid())).get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot != null && documentSnapshot.exists()) {

                    firestore.collection("Users").document(id).collection("posts").document(timeStamp).collection("Like").document(auth.getUid()).delete().addOnSuccessListener(unused -> {
                        Toast.makeText(getApplicationContext(), "Unliked", Toast.LENGTH_SHORT).show();
                    }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to unlike", Toast.LENGTH_SHORT).show());


                } else {
                    firestore.collection("Users").document(id).collection("posts").document(timeStamp).collection("Like").document(auth.getUid()).set(new id(auth.getUid())).addOnSuccessListener(unused -> {
                        Toast.makeText(getApplicationContext(), "Liked", Toast.LENGTH_SHORT).show();
                    }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to like", Toast.LENGTH_SHORT).show());
                }
            }).addOnFailureListener(e -> {
                Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();


            });
        });

        commentAdapter=new CommentAdapter(comments.this,list,firestore,auth,id,timeStamp,itemClickListener);
        binding.commentRv.setLayoutManager(new LinearLayoutManagerWrapper(comments.this, LinearLayoutManager.VERTICAL, false));
        binding.commentRv.setAdapter(commentAdapter);

    }


    @Override
    protected void onStart() {
        super.onStart();
        final boolean[] firstLoad = {true};
        firestore.collection("Users").document(id).collection("posts").document(timeStamp).collection("Like").document(Objects.requireNonNull(auth.getUid())).addSnapshotListener(comments.this, (value, error) -> {
            if (error != null) {
                return;
            }
            if (value != null && value.exists()) {
                binding.textView5.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_thumb_up_red_24, 0, 0, 0);
            } else {
                binding.textView5.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_thumb_up_24, 0, 0, 0);
            }
        });

        list.clear();
        firestore.collection("Users").document(id).collection("posts").document(timeStamp).collection("comments").orderBy("time", Query.Direction.DESCENDING).addSnapshotListener( comments.this, (value, error) -> {
            if (error != null) {
                Toast.makeText(comments.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
            if (value != null) {
                for (DocumentChange doc : value.getDocumentChanges()
                ) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        CommentModel commentModel = doc.getDocument().toObject(CommentModel.class);
                        commentModel.setDocumentId(doc.getDocument().getId());


                        if(firstLoad[0]){
                            list.add( commentModel);
                            commentAdapter.notifyItemInserted(list.size()-1);

                        }
                        else {
                            list.add(0, commentModel);
                            commentAdapter.notifyItemInserted(0);
                        }
                    }
                }
                firstLoad[0] =false;
            }
        });


        firestore.collection("Users").document(id).collection("posts").document(timeStamp).collection("Like").addSnapshotListener(comments.this, (value, error) -> {
            if (error != null) {
                return;
            }
            if (value != null) {
                binding.textView5.setText(String.valueOf(value.size()));
            }
        });
        firestore.collection("Users").document(id).addSnapshotListener(comments.this, (value, error) -> {
            if (error != null) {
                return;
            }
            if (value != null) {
                user_model userModel = value.toObject(user_model.class);
                if (userModel != null) {
                    Glide.with(getApplicationContext()).load(userModel.getProfile()).error(R.drawable.profile).placeholder(R.drawable.profile).into(binding.prfImg);
                    binding.textView3.setText(userModel.getUsername());
                    binding.textView4.setText(userModel.getAbout());
                }
            }
        });
        firestore.collection("Users").document(id).collection("posts").document(timeStamp).collection("comments").addSnapshotListener(comments.this, (value, error) -> {
            if (error != null) {
                return;
            }
            if (value != null) {
                binding.textView6.setText(String.valueOf(value.size()));
            }
        });

    }
}