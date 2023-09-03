package com.example.unizone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.SnapHelper;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;

import com.example.unizone.databinding.ActivitySeeMomentsBinding;
import com.example.unizone.model.MomentModel;
import com.example.unizone.model.adapters.ViewPagerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class seeMoments extends AppCompatActivity {

    ActivitySeeMomentsBinding bind;
    MediaController mediaController;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    ListenerRegistration listenerRegistration;
    ArrayList<MomentModel> list;
    ViewPagerAdapter adapter;
    String timeStamp,byWho;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind=ActivitySeeMomentsBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());
        auth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        list=new ArrayList<>();

        Intent intent=getIntent();
        byWho=intent.getStringExtra("byWho");
        bind.viewPager.setLayoutManager(new LinearLayoutManager(this));
        bind.viewPager.setHasFixedSize(true);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(bind.viewPager);
        adapter=new ViewPagerAdapter(seeMoments.this,list,firestore,auth,bind.viewPager,byWho);
        bind.viewPager.setAdapter(adapter);
        long DAY_IN_MS = 1000 * 60 * 60 * 24;
        timeStamp=String.valueOf(new Date().getTime()-DAY_IN_MS);

        listenerRegistration=firestore.collection("Users").document(byWho).collection("status").orderBy("timeStamp", Query.Direction.ASCENDING).whereGreaterThan("timeStamp",timeStamp).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null){
                    Toast.makeText(seeMoments.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                }
                if(value!=null){
                    for (DocumentChange snap:value.getDocumentChanges()
                         ) {
                        if(snap.getType()== DocumentChange.Type.ADDED) {
                            MomentModel model = snap.getDocument().toObject(MomentModel.class);
                            list.add(model);
                            adapter.notifyItemInserted(list.size() - 1);
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!byWho.equals(auth.getUid())) {
            firestore.collection("Users").document(Objects.requireNonNull(auth.getUid())).collection("OtherStatus").document(byWho).collection("status").whereGreaterThan("timestamp", timeStamp).orderBy("timestamp", Query.Direction.ASCENDING).whereEqualTo("viewed", "0").limit(1).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if (queryDocumentSnapshots != null) {
                        if(queryDocumentSnapshots.size()>0) {
                            for (QueryDocumentSnapshot snap : queryDocumentSnapshots
                            ) {
                                for (int i = 0; i < list.size(); i++) {
                                    if (snap.getId().equals(list.get(i).getTimeStamp())) {
                                        bind.viewPager.smoothScrollToPosition(i);
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(seeMoments.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(listenerRegistration!=null){
            listenerRegistration.remove();
        }
    }
}