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
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.unizone.R;
import com.example.unizone.model.id;
import com.example.unizone.model.user_model;
import com.example.unizone.seeMoments;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class story_adapter extends RecyclerView.Adapter<story_adapter.ViewHolder> {

    Context context;
    ArrayList<id> list;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    RecyclerView recyclerView;
    public story_adapter(Context context, ArrayList<id> list,RecyclerView recyclerView) {
        this.context=context;
        this.list=list;
        this.recyclerView=recyclerView;
        firestore =FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public story_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.moments_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull story_adapter.ViewHolder holder, int position) {
        id model=list.get(holder.getAdapterPosition());
        if(model!=null){
            firestore.collection("Users").document(model.getId()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error != null) {
                        Toast.makeText(context, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if (value != null) {
                            user_model userModel=value.toObject(user_model.class);
                            if(userModel!=null){
                                Glide.with(context).load(userModel.getProfile()).placeholder(R.drawable.profile).error(R.drawable.profile).into(holder.profile_img);
                                holder.username_txt.setText(userModel.getUsername());
                                holder.status_txt.setText(userModel.getAbout());
                            }
                        }
                    }
                }
            });

            holder.itemView.setBackgroundResource(R.color.white);
            firestore.collection("Users").document(Objects.requireNonNull(auth.getUid())).collection("OtherStatus").document(model.getId()).collection("status").whereEqualTo("viewed","0").limit(1).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error != null) {
                        Toast.makeText(context, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                    if (value != null) {
                        if(value.size()>0) {
                            holder.itemView.setBackgroundResource(R.color.unViewed);
                        }
                        else{
                            holder.itemView.setBackgroundResource(R.color.white);
                        }
                    }

                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, seeMoments.class);
                    intent.putExtra("byWho",model.getId());
                    context.startActivity(intent);
                }
            });



        firestore.collection("Users").document(Objects.requireNonNull(auth.getUid())).collection("OtherStatus").orderBy("id", Query.Direction.DESCENDING).limit(1).addSnapshotListener((value, error) -> {
            if (error != null) {
                Toast.makeText(context, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
            if (value != null) {
                for (DocumentChange doc:value.getDocumentChanges()
                ) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        if (model.getId().equals(doc.getDocument().getId())) {
                            notifyItemMoved(holder.getAdapterPosition(),0);
                            recyclerView.smoothScrollToPosition(0);
                        }
                    }
                }

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
