package com.example.unizone.model.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.unizone.PersonalChat;
import com.example.unizone.R;
import com.example.unizone.model.ChatModel;
import com.example.unizone.model.MsgModel;
import com.example.unizone.model.user_model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class chatsAdapter extends RecyclerView.Adapter<chatsAdapter.ViewHolder> {

    Context context;
    ArrayList<ChatModel> list;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    RecyclerView recyclerView;
    String unreadMsg=" unread messages*";


    public chatsAdapter(Context context, ArrayList<ChatModel> list,RecyclerView recyclerView) {
        this.context=context;
        this.list=list;
        this.recyclerView=recyclerView;
        auth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public chatsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.moments_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull chatsAdapter.ViewHolder holder, int position) {
        ChatModel chatModel=list.get(holder.getAdapterPosition());
        firestore.collection("Users").document(chatModel.getId()).addSnapshotListener((value, error) -> {
            if (error != null) {
                Toast.makeText(context, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
            else{
                if (value != null) {
                    user_model userModel=value.toObject(user_model.class);
                    if(userModel!=null){
                        Glide.with(context).load(userModel.getProfile()).placeholder(R.drawable.profile).error(R.drawable.profile).into(holder.profile_img);
                        holder.username_txt.setText(userModel.getUsername());
                    }
                }
            }
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent=new Intent(context, PersonalChat.class);
            intent.putExtra("id",chatModel.getId());
            context.startActivity(intent);
        });
        holder.itemView.setBackgroundResource(R.color.white);

            firestore.collection("Chats").document(Objects.requireNonNull(auth.getUid())).collection("AllChats").document(chatModel.getId()).collection("Messages").whereEqualTo("viewed", "0").orderBy("timeStamp").addSnapshotListener((value, error) -> {
                if (error != null) {
                    Toast.makeText(context, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }

                if (value != null) {
                    int count;
                    count = value.size();

                    if (count > 0) {
                        String showText="*" + count + unreadMsg;
                        holder.status_txt.setText(showText);

                        holder.itemView.setBackgroundResource(R.color.unViewed);

                    }
                    else{
                        firestore.collection("Chats").document(Objects.requireNonNull(auth.getUid())).collection("AllChats").document(chatModel.getId()).collection("Messages").orderBy("timeStamp", Query.Direction.DESCENDING).limit(1).addSnapshotListener((value1, error1) -> {
                            if (error1 != null) {
                                Toast.makeText(context, error1.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }

                            if (value1 != null) {
                                for (QueryDocumentSnapshot snap:value1
                                ) {
                                    MsgModel msgModel=snap.toObject(MsgModel.class);

                                    if(msgModel.getViewed().equals("1")){
                                        holder.status_txt.setText(msgModel.getMsg());
                                        holder.itemView.setBackgroundResource(R.color.white);
                                    }
                                }
//
                            }

                        });

                    }
                }

            });
        firestore.collection("Chats").document(Objects.requireNonNull(auth.getUid())).collection("AllChats").orderBy("timeStamp", Query.Direction.DESCENDING).limit(1).addSnapshotListener((value, error) -> {
            if (error != null) {
                Toast.makeText(context, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
                if (value != null) {
                    for (DocumentChange doc:value.getDocumentChanges()
                    ) {
                        if (doc.getType() == DocumentChange.Type.ADDED) {
                            ChatModel chatModel1 = doc.getDocument().toObject(ChatModel.class);
                            if (chatModel1.getId().equals(chatModel.getId())) {
                               notifyItemMoved(holder.getAdapterPosition(),0);
                               recyclerView.smoothScrollToPosition(0);
                            }
                        }
                    }

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView username_txt,status_txt;
        ImageView profile_img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username_txt=itemView.findViewById(R.id.username_txt);
            status_txt=itemView.findViewById(R.id.status_txt);
            profile_img=itemView.findViewById(R.id.imageView1);
        }
    }
}
