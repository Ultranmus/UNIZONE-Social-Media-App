package com.example.unizone;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.unizone.databinding.ActivityConnectionsBinding;
import com.example.unizone.model.adapters.createChatAdapter;
import com.example.unizone.model.connection_model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class Connections extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private createChatAdapter chatAdapter;
    private ArrayList<connection_model> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.unizone.databinding.ActivityConnectionsBinding binding = ActivityConnectionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firestore=FirebaseFirestore.getInstance();
        binding.connectionRv.setLayoutManager(new LinearLayoutManagerWrapper(this,LinearLayoutManager.VERTICAL,false));
        list=new ArrayList<>();
        chatAdapter=new createChatAdapter(Connections.this,list,firestore);
        binding.connectionRv.setAdapter(chatAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        AtomicBoolean firstLoad= new AtomicBoolean(false);
        list.clear();

        firestore.collection("Users").document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).collection("Connections").addSnapshotListener(this, (value, error) -> {
            if(error!=null){
                Toast.makeText(Connections.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
            else{
                if(value!=null){
                    if(firstLoad.get()){
                        list.clear();
                    }
                    for (QueryDocumentSnapshot snapshot:value
                         ) {
                        connection_model connectionModel=snapshot.toObject(connection_model.class);
                        list.add(connectionModel);
                        chatAdapter.notifyItemInserted(list.size()-1);
                    }
                    firstLoad.set(true);
                }
            }
        });
    }
}