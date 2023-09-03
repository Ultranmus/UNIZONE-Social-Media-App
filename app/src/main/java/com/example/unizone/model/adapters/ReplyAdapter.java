package com.example.unizone.model.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.unizone.R;
import com.example.unizone.comments;
import com.example.unizone.follow_user;
import com.example.unizone.model.CommentModel;
import com.example.unizone.model.user_model;
import com.example.unizone.profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.MyViewHolder>{


    Context context;
    ArrayList<CommentModel> replyList;
    FirebaseFirestore firestore;
    FirebaseAuth auth;

    public ReplyAdapter(Context context, ArrayList<CommentModel> replyList, FirebaseFirestore firestore, FirebaseAuth auth) {
        this.context = context;
        this.replyList = replyList;
        this.firestore = firestore;
        this.auth = auth;
    }




    @NonNull
    @Override
    public ReplyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.reply_item,parent,false);
        return new ReplyAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReplyAdapter.MyViewHolder holder, int position) {
        CommentModel commentModel=replyList.get(position);
        holder.comment.setText(commentModel.getComment());
        holder.time.setText(commentModel.getTimeStamp());
        holder.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if(commentModel.getId().equals(auth.getUid())){
                    intent=new Intent(context, profile.class);}
                else {
                    intent=new Intent(context, follow_user.class);
                }
                intent.putExtra("id",commentModel.getId());
                context.startActivity(intent);
            }
        });
        firestore.collection("Users").document(commentModel.getId()).addSnapshotListener((comments)context, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null){
                    return;
                }
                if(value!=null && value.exists()){
                    user_model userModel=value.toObject(user_model.class);
                    if (userModel != null) {
                        Glide.with(context).load(userModel.getProfile()).placeholder(R.drawable.profile).error(R.drawable.profile).into(holder.profile);
                        holder.username.setText(userModel.getUsername());
                    }
                }
            }
        });





    }

    @Override
    public int getItemCount() {
        return replyList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView profile;
        TextView comment,time,username;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            profile=itemView.findViewById(R.id.prfImg);
            comment=itemView.findViewById(R.id.textView4);
            time=itemView.findViewById(R.id.reply_time);
            username=itemView.findViewById(R.id.textView3);
        }
    }
}
