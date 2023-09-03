package com.example.unizone.model.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.unizone.LinearLayoutManagerWrapper;
import com.example.unizone.R;
import com.example.unizone.comments;
import com.example.unizone.follow_user;
import com.example.unizone.model.CommentModel;
import com.example.unizone.model.user_model;
import com.example.unizone.profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    Context context;
    ArrayList<CommentModel> list;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    String id,timeStamp;
    ItemClickListener itemClickListener;


    public CommentAdapter(Context context, ArrayList<CommentModel> list, FirebaseFirestore firestore, FirebaseAuth auth,String id,String timeStamp,ItemClickListener itemClickListener) {
        this.context = context;
        this.list = list;
        this.firestore = firestore;
        this.auth = auth;
        this.id=id;
        this.itemClickListener=itemClickListener;
        this.timeStamp=timeStamp;

    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.comments_item,parent,false);
        return new CommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {
        CommentModel commentModel=list.get(position);
        holder.comment.setText(commentModel.getComment());
        holder.time.setText(commentModel.getTimeStamp());

        holder.profile.setOnClickListener(v -> {
            Intent intent;
            if(commentModel.getId().equals(auth.getUid())){
            intent=new Intent(context, profile.class);}
            else {
                intent=new Intent((comments)context, follow_user.class);
        }
            intent.putExtra("id",commentModel.getId());
            ( (comments)context).startActivity(intent);
        });

        firestore.collection("Users").document(commentModel.getId()).addSnapshotListener((comments) context,(value, error) -> {
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
        });










        holder.reply.setOnClickListener(v -> {

            itemClickListener.onClick(commentModel.getDocumentId(),holder.username.getText().toString());
            holder.replyRv.setVisibility(View.VISIBLE);
//                holder.replyRv.setVisibility(View.VISIBLE);




        });

        ArrayList<CommentModel> replyList=new ArrayList<>();
        holder.replyRv.setLayoutManager(new LinearLayoutManagerWrapper((comments)context, LinearLayoutManager.VERTICAL, false));
        ReplyAdapter adapter=new ReplyAdapter((comments)context,replyList,firestore,auth);
        holder.replyRv.setAdapter(adapter);

        final boolean[] firstLoadPage = {true};
        firestore.collection("Users").document(id).collection("posts").document(timeStamp).collection("comments").document(commentModel.getDocumentId()).collection("reply").orderBy("time", Query.Direction.DESCENDING)
                .addSnapshotListener((comments)context,(value, error) -> {


            if (value != null) {
        for (DocumentChange doc : value.getDocumentChanges()
                ) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        CommentModel comment_Model = doc.getDocument().toObject(CommentModel.class);


                        if (firstLoadPage[0]) {
                            replyList.add(comment_Model);
                            adapter.notifyItemInserted(replyList.size() - 1);

                        } else {
                            replyList.add(0, comment_Model);
                            adapter.notifyItemInserted(0);
                        }
                    }


                }


                firstLoadPage[0] =false;
          }
        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profile;
        TextView comment,time,reply,username;
        RecyclerView replyRv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profile=itemView.findViewById(R.id.prf_img);
            comment=itemView.findViewById(R.id.comment);
            time=itemView.findViewById(R.id.time);
            reply=itemView.findViewById(R.id.replyButton);
            replyRv=itemView.findViewById(R.id.reply_rv);
            username=itemView.findViewById(R.id.textView3);
        }
    }
    public interface ItemClickListener {
        void onClick(String reply_to_id,String reply_to_username);
    }
}
