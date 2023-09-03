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
import com.example.unizone.Connections;
import com.example.unizone.PersonalChat;
import com.example.unizone.R;
import com.example.unizone.model.connection_model;
import com.example.unizone.model.user_model;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class createChatAdapter extends RecyclerView.Adapter<createChatAdapter.ViewHolder> {

    Context context;
    ArrayList<connection_model> list;

    public createChatAdapter(Context context, ArrayList<connection_model> list, FirebaseFirestore firestore) {
        this.context = context;
        this.list = list;
        this.firestore = firestore;
    }

    FirebaseFirestore firestore;


    @NonNull
    @Override
    public createChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.moments_item,parent,false);
        return new createChatAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull createChatAdapter.ViewHolder holder, int position) {
        connection_model connectionModel=list.get(holder.getAdapterPosition());
        if(connectionModel!=null){
            firestore.collection("Users").document(connectionModel.getAuth_id()).addSnapshotListener((Connections)context, (value, error) -> {
                if(error!=null){
                    Toast.makeText(context, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
                else{
                    if(value!=null && value.exists()){
                        user_model userModel=value.toObject(user_model.class);
                        if (userModel != null) {
                            holder.username_txt.setText(userModel.getUsername());
                            holder.status_txt.setText(userModel.getAbout());
                            Glide.with(context).load(userModel.getProfile()).placeholder(R.drawable.profile).error(R.drawable.profile).into(holder.profile_img);
                        }
                    }

                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, PersonalChat.class);
                    intent.putExtra("id",connectionModel.getAuth_id());
                    context.startActivity(intent);
                    ((Connections) context).finish();
                }
            });
        }
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
