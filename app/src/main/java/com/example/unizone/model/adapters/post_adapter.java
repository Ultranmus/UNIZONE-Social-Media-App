package com.example.unizone.model.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.unizone.R;
import com.example.unizone.Tabs;
import com.example.unizone.comments;
import com.example.unizone.follow_user;
import com.example.unizone.model.id;
import com.example.unizone.model.other_post_model;
import com.example.unizone.model.prf_post_model;
import com.example.unizone.model.user_model;
import com.example.unizone.profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

public class post_adapter extends RecyclerView.Adapter<post_adapter.ViewHolder> {

    Context context;
    ArrayList<other_post_model> list;
    FirebaseFirestore firestore;
    FirebaseAuth auth=FirebaseAuth.getInstance();

    public post_adapter(Context context, ArrayList<other_post_model> list,FirebaseFirestore firestore) {
        this.context=context;
        this.list=list;
        this.firestore=firestore;
    }

    @NonNull
    @Override
    public post_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.post_item,parent,false);
        Glide.with(context).load(R.drawable.holder).into((ImageView)view.findViewById(R.id.post_img));

        return new post_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull post_adapter.ViewHolder holder, int position) {
        other_post_model otherPostModel=list.get(holder.getAdapterPosition());
        String id=otherPostModel.getPost_by_id();

        holder.profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if(otherPostModel.getPost_by_id().equals(auth.getUid())){
                    intent=new Intent(context, profile.class);
                }
                else{
                    intent=new Intent(context, follow_user.class);
                }
                intent.putExtra("id",otherPostModel.getPost_by_id());
                context.startActivity(intent);
            }
        });


        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, comments.class);
                intent.putExtra("ids",id);
                intent.putExtra("timeStamp",otherPostModel.getTimestamp());
                context.startActivity(intent);
            }
        });

        firestore.collection("Users").document(otherPostModel.getPost_by_id()).collection("posts").document(otherPostModel.getTimestamp()).get().addOnSuccessListener(documentSnapshot -> {
            if(documentSnapshot!=null){
                prf_post_model prfPostModel=documentSnapshot.toObject(prf_post_model.class);
                if(prfPostModel!=null) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(context).load(prfPostModel.getPost()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.holder).error(R.drawable.profile).into(holder.post_img);

                        }
                    },3000);
                    if(prfPostModel.getDescription().isEmpty()){
                        holder.description.setVisibility(View.GONE);
                    }else{
                        holder.description.setVisibility(View.VISIBLE);
                        holder.description.setText(prfPostModel.getDescription());
                    }
                }
            }
        }).addOnFailureListener(e -> Toast.makeText(context, "Problem in loading post", Toast.LENGTH_SHORT).show());


        firestore.collection("Users").document(id).collection("posts").document(otherPostModel.getTimestamp()).collection("Like").document(Objects.requireNonNull(auth.getUid())).addSnapshotListener( (value, error) -> {
//
            if(error!=null){
                return;
            }
            if( value!=null && value.exists()){
                holder.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_thumb_up_red_24,0,0,0);
            }
            else{
                holder.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_thumb_up_24,0,0,0);
            }
        });


        firestore.collection("Users").document(list.get(holder.getAdapterPosition()).getPost_by_id()).collection("posts").document(otherPostModel.getTimestamp()).collection("Like").addSnapshotListener( (value, error) -> {
            if(error!=null){
                return;
            }
            if(value!=null){
                holder.like.setText(String.valueOf(value.size()));
            }
        });
        firestore.collection("Users").document(otherPostModel.getPost_by_id()).addSnapshotListener((Tabs) context, (value, error) -> {
            if(error!=null){
                return;
            }
            if(value!=null){
                user_model userModel=value.toObject(user_model.class);
                if (userModel != null) {
                    Glide.with(context).load(userModel.getProfile()).error(R.drawable.profile).placeholder(R.drawable.profile).into(holder.profile_img);
                    holder.name.setText(userModel.getUsername());
                    holder.about.setText(userModel.getAbout());
                }


            }
        });
        firestore.collection("Users").document(otherPostModel.getPost_by_id()).collection("posts").document(otherPostModel.getTimestamp()).collection("comments").addSnapshotListener( (value, error) -> {
            if(error!=null){
                return;
            }
            if(value!=null){
                holder.comment.setText(String.valueOf(value.size()));
            }
        });
        holder.like.setOnClickListener(v -> firestore.collection("Users").document(otherPostModel.getPost_by_id()).collection("posts").document(otherPostModel.getTimestamp()).collection("Like").document(auth.getUid()).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot != null && documentSnapshot.exists()) {

                firestore.collection("Users").document(otherPostModel.getPost_by_id()).collection("posts").document(otherPostModel.getTimestamp()).collection("Like").document(auth.getUid()).delete().addOnSuccessListener(unused -> {
                    Toast.makeText(context, "Unliked", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> Toast.makeText(context, "Failed to unlike", Toast.LENGTH_SHORT).show());




            }
            else{
                firestore.collection("Users").document(otherPostModel.getPost_by_id()).collection("posts").document(otherPostModel.getTimestamp()).collection("Like").document(auth.getUid()).set(new id(auth.getUid())).addOnSuccessListener(unused -> {
                    Toast.makeText(context, "Liked", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> Toast.makeText(context, "Failed to like", Toast.LENGTH_SHORT).show());
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

        }));


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,about,description,like,comment,share;
        ImageView profile_img,post_img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.textView3);
            about=itemView.findViewById(R.id.textView4);
            description=itemView.findViewById(R.id.description);
            like=itemView.findViewById(R.id.textView5);
            comment=itemView.findViewById(R.id.textView6);
            share=itemView.findViewById(R.id.textView7);
            profile_img=itemView.findViewById(R.id.prf_img);
            post_img=itemView.findViewById(R.id.post_img);

        }
    }
}
