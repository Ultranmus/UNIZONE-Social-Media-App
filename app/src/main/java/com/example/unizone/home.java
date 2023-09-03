package com.example.unizone;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unizone.databinding.FragmentHomeBinding;
import com.example.unizone.model.adapters.post_adapter;
import com.example.unizone.model.other_post_model;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;


public class home extends Fragment {

    ArrayList<other_post_model> list;
    post_adapter postAdapter;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    com.example.unizone.databinding.FragmentHomeBinding binding;
    DocumentSnapshot lastVisible;
    boolean isFirstPageFirstLoad;
    int totalItem;
    int lastVisibleItem;
    private ListenerRegistration listener;
    private boolean noMore=false;

    public home() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root= binding.getRoot();
        list=new ArrayList<>();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();

        binding.postRv.setLayoutManager(new LinearLayoutManagerWrapper(getContext(),LinearLayoutManager.VERTICAL,false));
        postAdapter=new post_adapter(getContext(),list,firebaseFirestore);
        binding.postRv.setAdapter(postAdapter);
        loadFirstQuery();////************************change in case of error

        return root;
    }
    @Override
    public void onStart() {
        super.onStart();

//        loadFirstQuery();////************************change in case of error
    }

    public void loadFirstQuery() {


        if (firebaseAuth.getCurrentUser() != null) {

            list.clear();

            firstPosts();
            // what we do when recycler reach bottom
            binding.postRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    LinearLayoutManager linearLayoutManager=(LinearLayoutManager) binding.postRv.getLayoutManager();
                    totalItem= Objects.requireNonNull(linearLayoutManager).getItemCount();
                    lastVisibleItem=linearLayoutManager.findLastCompletelyVisibleItemPosition();
                    if(totalItem<lastVisibleItem+2) {
                        boolean reachBottom = !recyclerView.canScrollVertically(1);


                        if (reachBottom && !noMore) {
                            loadMorePost(); // do load more post
                        }
                        if(noMore){
                            Toast.makeText(getContext(), "No more posts!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    binding.postRv.setItemViewCacheSize(list.size());



                }
            });


}

            }



    public void firstPosts(){
        isFirstPageFirstLoad=true;
        com.google.firebase.firestore.Query firstQuery = firebaseFirestore
                .collection("Users")
                .document(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid())
                .collection("OtherPosts")
                .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .limit(5);
        listener=firstQuery.addSnapshotListener((documentSnapshots, e) -> {


            if (documentSnapshots != null && !documentSnapshots.isEmpty()) {
                // please add if doc not empty
                if (isFirstPageFirstLoad) {
                    lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1); // array 0, 1, 2
                }
                int add = 0;
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {

                        other_post_model otherPostModel = doc.getDocument().toObject(other_post_model.class);


                        // if first page first load true
                        if (isFirstPageFirstLoad) {
                            list.add(otherPostModel);
                            postAdapter.notifyItemInserted(list.size()-1);
                        } else {
                            add++;
                            list.add(0, otherPostModel);
                            postAdapter.notifyItemInserted(0);
                        }


                        // fire the event
                        int position;
                        LinearLayoutManager layoutManager = (LinearLayoutManager) binding.postRv.getLayoutManager();
                        position= Objects.requireNonNull(layoutManager).findFirstVisibleItemPosition();
                        binding.postRv.scrollToPosition(position + add);

                    }

                }

                isFirstPageFirstLoad = false;
            }

        });

}

    // Method to load more post

    public void loadMorePost() {

        if (firebaseAuth.getCurrentUser() != null) {

            String currentUserId = firebaseAuth.getCurrentUser().getUid();

            com.google.firebase.firestore.Query nextQuery = firebaseFirestore
                    .collection("Users")
                    .document(currentUserId)
                    .collection("OtherPosts")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .startAfter(lastVisible)
                    .limit(5);


            nextQuery.get().addOnSuccessListener(documentSnapshots -> {
                if(documentSnapshots.isEmpty()){
                    noMore=true;

                }
                if (!documentSnapshots.isEmpty()) {

                    lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
                    for (QueryDocumentSnapshot snapshot:documentSnapshots) {

                        if (snapshot!=null) {
                            other_post_model otherPostModel = snapshot.toObject(other_post_model.class);
                            list.add(otherPostModel);
                            postAdapter.notifyItemInserted(list.size()-1);
                        }

                    }
                }
            })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    });

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        listener.remove();
    }
}