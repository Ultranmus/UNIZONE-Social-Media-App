package com.example.unizone.model.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.unizone.R;
import com.example.unizone.follow_user;
import com.example.unizone.model.connection_model;
import com.example.unizone.model.user_model;
import com.example.unizone.profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

public class connection_prf_adapter extends RecyclerView.Adapter<connection_prf_adapter.ViewHolder> {
    Context context;
    ArrayList<connection_model> list;
    FirebaseFirestore firestore;
    public connection_prf_adapter(Context context, ArrayList<connection_model> list,FirebaseFirestore firestore) {
        this.context=context;
        this.list=list;
        this.firestore=firestore;
    }
    @NonNull
    @Override
    public connection_prf_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.connection_item_prf,parent,false);
        return new connection_prf_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull connection_prf_adapter.ViewHolder holder, int position) {
        connection_model model=list.get(position);
        if (model != null) {


            firestore.collection("Users").document(model.getAuth_id()).addSnapshotListener((profile)context,new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if(value.exists() && value!=null){
                        user_model userModel=value.toObject(user_model.class);
                        model.setPrf_img_connection(userModel.getProfile());
                            Glide.with(context).load(model.getPrf_img_connection()).placeholder(R.drawable.profile).error(R.drawable.profile).into(holder.prf_img);
                        notifyItemChanged(holder.getAdapterPosition());
                    }
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(model.getAuth_id().equals(FirebaseAuth.getInstance().getUid())){
                        context.startActivity(new Intent(context, profile.class));
                    }else{
                        Intent intent1=new Intent(context, follow_user.class);

                        intent1.putExtra("id",model.getAuth_id());

                        context.startActivity(intent1);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public static  class ViewHolder extends RecyclerView.ViewHolder {
        ImageView prf_img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            prf_img=itemView.findViewById(R.id.prf_img);
        }
    }

}
