package com.example.unizone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.unizone.databinding.ActivitySearchBinding;
import com.example.unizone.model.adapters.search_adapter;
import com.example.unizone.model.user_model;
import com.example.unizone.model.usernames_model;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class search extends AppCompatActivity {

    private ActivitySearchBinding bind;
    private ArrayList<user_model> userList;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private search_adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind=ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());
        auth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        userList=new ArrayList<>();
        bind.searchTxt.setThreshold(3);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        firestore.collection("Users").addSnapshotListener(this,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null) {
                    userList.clear();
                    for (QueryDocumentSnapshot snapshot : value) {
                            if(!snapshot.getId().equals(auth.getUid()) ) {
                        user_model userModel = snapshot.toObject(user_model.class);
                        userModel.setId(snapshot.getId());

                        userList.add(userModel);
                        adapter=new search_adapter(search.this,userList,firestore);
                        bind.searchTxt.setAdapter(adapter);
                            }
                    }
                }
            }
        });
    }

}