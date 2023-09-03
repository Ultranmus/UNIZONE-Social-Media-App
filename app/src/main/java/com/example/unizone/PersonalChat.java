package com.example.unizone;

import static androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_SWIPE;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.unizone.databinding.ActivityPersonalChatBinding;
import com.example.unizone.model.ChatModel;

import com.example.unizone.model.MsgModel;
import com.example.unizone.model.adapters.chatAdapter;
import com.example.unizone.model.user_model;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class PersonalChat extends AppCompatActivity{

    private ActivityPersonalChatBinding binding;
    private String id;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private ArrayList<MsgModel> list;
    private chatAdapter adapter;
    private ListenerRegistration listenerRegistration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPersonalChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        if (id == null) {
            finish();
        }
        binding.search.setChecked(false);
        if (binding.search.isChecked()) {
            checked();

        } else {
            notChecked();

        }
        list = new ArrayList<>();
        adapter = new chatAdapter(this, list);
        binding.msgRv.setHasFixedSize(true);
        binding.usernameTxt.setSelected(true);
        binding.msgRv.setLayoutManager(new LinearLayoutManagerWrapper(PersonalChat.this, LinearLayoutManager.VERTICAL, true));
        binding.msgRv.setAdapter(adapter);
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchHelper.attachToRecyclerView(binding.msgRv);




        binding.search.setTextOff("Click to search");


        binding.search.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checked();
                } else {
                    notChecked();
                }
            }
        });


        binding.imageView1.setOnClickListener(v -> {
            Intent intent1 = new Intent(PersonalChat.this, follow_user.class);
            intent1.putExtra("id", id);
            startActivity(intent1);
        });

        show();
    }
    public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {
        private final RecyclerView.Adapter mAdapter;

        public SwipeToDeleteCallback(RecyclerView.Adapter adapter) {
            super(0, ItemTouchHelper.LEFT);
            mAdapter = adapter;
        }


        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            // check if the swipe direction is left

                int position = viewHolder.getAdapterPosition();
                if( dX<0 && dX >-300){
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                    binding.editTextTextMultiLine.setText(list.get(position).getMsg());
                }
        }
    }




    @Override
    protected void onStart() {
        super.onStart();

//        show();
            
                        firestore.collection("Users").document(id).addSnapshotListener(this, (value, error) -> {
                            if (error != null) {
                                Toast.makeText(PersonalChat.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                if (value != null) {
                                    user_model userModel =value.toObject(user_model.class);
                                    if (userModel != null) {
                                        Glide.with(PersonalChat.this).load(userModel.getProfile()).error(R.drawable.profile).placeholder(R.drawable.profile).into(binding.imageView1);
                                        binding.usernameTxt.setText(userModel.getUsername());

                                    }
                                }
                            }
                        });

        firestore.collection("Chats").document(Objects.requireNonNull(auth.getUid())).collection("AllChats").document(id).collection("Messages").whereEqualTo("viewed", "0").orderBy("timeStamp").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots!=null){
                    int count =queryDocumentSnapshots.size()-1;


                    if (count > 0) {
                        binding.msgRv.smoothScrollToPosition(count);
                    }
                }
            }
        });



    }
    private void show(){
        final boolean[] firstLoad = {true};
        list.clear();
       listenerRegistration= firestore.collection("Chats").document(Objects.requireNonNull(auth.getUid())).collection("AllChats").document(id).collection("Messages").orderBy("timeStamp", Query.Direction.DESCENDING).addSnapshotListener(  (value, error) -> {

            if(error!=null){
                Toast.makeText(PersonalChat.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
            else{
                if(value!=null) {
                    boolean unViewed=true;
                    if (firstLoad[0]) {
                        list.clear();

                        for (DocumentChange snapshot : value.getDocumentChanges()) {
                            if(snapshot.getType()== DocumentChange.Type.ADDED) {
                                MsgModel msgModel = snapshot.getDocument().toObject(MsgModel.class);
                                msgModel.setDocumentId(snapshot.getDocument().getId());
                                list.add(msgModel);
                                adapter.notifyItemInserted(list.size() - 1);
                            }
                        }

                        firstLoad[0] = false;
                    } else {
                        for (DocumentChange snapshot : value.getDocumentChanges()) {
                            if (snapshot.getType() == DocumentChange.Type.ADDED) {
                                MsgModel msgModel = snapshot.getDocument().toObject(MsgModel.class);
                                msgModel.setDocumentId(snapshot.getDocument().getId());
                                list.add(0, msgModel);
                                adapter.notifyItemInserted(0);
                            }
                        }
                        binding.msgRv.scrollToPosition(value.getDocumentChanges().size() - 1);

                    }

                }
            }
        });

    }

    private void checked(){
        binding.text.setText(R.string.send_msg);
        binding.editTextTextMultiLine.setHint(R.string.search_txt);
        binding.imageButton2.setOnClickListener(v -> {
            String msg = binding.editTextTextMultiLine.getText().toString().trim();
            if (TextUtils.isEmpty(msg)) {
                binding.editTextTextMultiLine.setError("Empty");
                binding.editTextTextMultiLine.requestFocus();
            } else {
                binding.editTextTextMultiLine.setText("");
                Intent intent1=new Intent(PersonalChat.this,IncognitoActivity.class);
                intent1.putExtra("txt",msg);
                startActivity(intent1);
            }
        });
    }
    private void notChecked() {
        binding.text.setText(R.string.search_txt);
        binding.editTextTextMultiLine.setHint(R.string.enter_text);
        binding.imageButton2.setOnClickListener(v -> {
            String msg = binding.editTextTextMultiLine.getText().toString().trim();
            if (TextUtils.isEmpty(msg)) {
                binding.editTextTextMultiLine.setError("Empty");
                binding.editTextTextMultiLine.requestFocus();
            } else {
                binding.editTextTextMultiLine.setText("");
                DateFormat formatter = SimpleDateFormat.getDateTimeInstance();
                Date date = new Date();
                String timeS = formatter.format(date);
                String timeStamp=String.valueOf(new Date().getTime());
                firestore.collection("Chats").document(Objects.requireNonNull(auth.getUid())).collection("AllChats").document(id).set(new ChatModel(msg,id,timeStamp)).addOnSuccessListener(unused -> firestore.collection("Chats").document(id).collection("AllChats").document(auth.getUid()).set(new ChatModel(msg, auth.getUid(), timeStamp)).addOnFailureListener(e -> Toast.makeText(PersonalChat.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show())).addOnFailureListener(e -> Toast.makeText(PersonalChat.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
                firestore.collection("Chats").document(Objects.requireNonNull(auth.getUid())).collection("AllChats").document(id).collection("Messages").document().set(new MsgModel(timeStamp, auth.getUid(), msg, timeS,"1")).addOnSuccessListener(unused -> firestore.collection("Chats").document(id).collection("AllChats").document(auth.getUid()).collection("Messages").document().set(new MsgModel(timeStamp, auth.getUid(), msg, timeS,"0")).addOnFailureListener(e -> Toast.makeText(PersonalChat.this, "Failed to send msg", Toast.LENGTH_SHORT).show())).addOnFailureListener(e -> Toast.makeText(PersonalChat.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(listenerRegistration!=null){
            listenerRegistration.remove();
        }
    }

}