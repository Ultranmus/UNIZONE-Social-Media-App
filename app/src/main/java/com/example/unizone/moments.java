package com.example.unizone;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.unizone.databinding.FragmentMomentsBinding;
import com.example.unizone.model.id;
import com.example.unizone.model.adapters.story_adapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;


public class moments extends Fragment {

    private FragmentMomentsBinding binding;
    private ListenerRegistration listener3;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    story_adapter storyAdapter;

    ArrayList<id> list;
    public moments() {

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMomentsBinding.inflate(inflater, container, false);
        View root= binding.getRoot();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        list=new ArrayList<>();
        binding.momentsRv.setLayoutManager(new LinearLayoutManagerWrapper(getContext(),LinearLayoutManager.VERTICAL,false));
        binding.momentsRv.setNestedScrollingEnabled(false);
        storyAdapter=new story_adapter(getActivity(),list,binding.momentsRv);
        binding.momentsRv.setAdapter(storyAdapter);

        binding.imageView1.setOnClickListener(v->{

            Intent intent=new Intent(getContext(),createMoments.class);
            startActivity(intent);



        });
        binding.imageView2.setOnClickListener(v->{

            Intent intent=new Intent(getContext(),createMoments.class);
            startActivity(intent);



        });
        binding.textView.setOnClickListener(v -> {
            long DAY_IN_MS = 1000 * 60 * 60 * 24;
            String timeStamp=String.valueOf(new Date().getTime()-DAY_IN_MS);
            firebaseFirestore.collection("Users").document(Objects.requireNonNull(firebaseAuth.getUid())).collection("status").orderBy("timeStamp", Query.Direction.ASCENDING).whereGreaterThan("timeStamp",timeStamp).get().addOnSuccessListener(queryDocumentSnapshots -> {
                int size=queryDocumentSnapshots.size();
                if (size>0){
                    Intent intent=new Intent(getContext(), seeMoments.class);
                    intent.putExtra("byWho", firebaseAuth.getUid());
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getContext(), "No moments exist", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> Toast.makeText(getContext(), "No moments created", Toast.LENGTH_SHORT).show());

        });

        showMoments();

        return root;
    }


    private void showMoments(){
        final boolean[] firstLoad = {true};
        list.clear();
        long DAY_IN_MS = 1000 * 60 * 60 * 24;
        String timeStamp=String.valueOf(new Date().getTime()-DAY_IN_MS);
        listener3=firebaseFirestore.collection("Users").document(Objects.requireNonNull(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid())).collection("OtherStatus").orderBy("id", Query.Direction.DESCENDING).whereGreaterThan("id",timeStamp).addSnapshotListener((value, error) -> {
            if (error != null) {
                Toast.makeText(getContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            } else {
                if (value != null) {

                    if (firstLoad[0]) {
                        list.clear();
                        for (DocumentChange doc : value.getDocumentChanges()) {

                            if(doc.getType()== DocumentChange.Type.ADDED) {

                                id model = doc.getDocument().toObject(id.class);
                                model.setId(doc.getDocument().getId());
                                list.add(model);
                                storyAdapter.notifyItemInserted(list.size()-1);

                            }
                        }
                        firstLoad[0] = false;
                    }
                    else {


                        for (DocumentChange snapshot : value.getDocumentChanges()) {
                            if (snapshot.getType()==DocumentChange.Type.ADDED) {
                                id model = snapshot.getDocument().toObject(id.class);
                                model.setId(snapshot.getDocument().getId());
                                list.add(0, model);
                                storyAdapter.notifyItemInserted(0);
                            }
                        }
                    }
                    binding.momentsRv.scrollToPosition(0);
                }
                else{
                    Toast.makeText(getContext(), "no id", Toast.LENGTH_SHORT).show();
                }
            }
        });




    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(listener3!=null){
            listener3.remove();
        }
    }


}