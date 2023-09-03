package com.example.unizone;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.unizone.databinding.FragmentChatBinding;
import com.example.unizone.model.ChatModel;
import com.example.unizone.model.adapters.chatsAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class chat extends Fragment {

    FragmentChatBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    ArrayList<ChatModel> list;
    private chatsAdapter chatsAdapter1;
    private ListenerRegistration listener2;
    public chat() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater, container, false);
        View root= binding.getRoot();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();

        list=new ArrayList<>();
        chatsAdapter1=new chatsAdapter(getContext(),list,binding.chatRv);

        binding.chatRv.setLayoutManager(new LinearLayoutManagerWrapper(getContext(),LinearLayoutManager.VERTICAL,false));
        binding.chatRv.setAdapter(chatsAdapter1);

        binding.newChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),Connections.class));
            }
        });
        showChats();

        return root;
    }


    private void showChats(){
        final boolean[] firstLoad = {true};
        list.clear();
        listener2=firebaseFirestore.collection("Chats").document(Objects.requireNonNull(firebaseAuth.getUid())).collection("AllChats").orderBy("timeStamp", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(getContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    if (value != null) {
                        if (firstLoad[0]) {
                            list.clear();

                            for (DocumentChange doc : value.getDocumentChanges()) {
                                if(doc.getType()== DocumentChange.Type.ADDED) {
                                    ChatModel chatModel = doc.getDocument().toObject(ChatModel.class);
                                    list.add(chatModel);
                                    chatsAdapter1.notifyItemInserted(list.size()-1);
                                }
                            }
                            firstLoad[0] = false;
                        }
                        else {
                            for (DocumentChange snapshot : value.getDocumentChanges()) {
                                if (snapshot.getType()==DocumentChange.Type.ADDED) {
                                    ChatModel chatModel = snapshot.getDocument().toObject(ChatModel.class);
                                    list.add(0, chatModel);
                                    chatsAdapter1.notifyItemInserted(0);
                                }
                            }
                        }
                        binding.chatRv.scrollToPosition(0);
                    }
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(listener2!=null){
            listener2.remove();
        }
    }
}