package com.example.unizone.model.adapters;



import android.content.Context;

import android.os.Build;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unizone.R;
import com.example.unizone.model.MsgModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.Objects;

public class chatAdapter extends RecyclerView.Adapter<chatAdapter.MyViewHolder> {

    FirebaseFirestore firestore;
    FirebaseAuth auth;
    Context context;
    ArrayList<MsgModel> list;
    String username;

    public chatAdapter(Context context, ArrayList<MsgModel> list) {
        this.context = context;
        this.list = list;
        firestore=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.msg_send_item,parent,false);


        return new chatAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MsgModel msgModel=list.get(holder.getAdapterPosition());
        if(msgModel!=null){
           if(msgModel.getByWho().equals(auth.getUid())){
               String you="you";
               holder.name.setText(you);
               holder.layout.setGravity(Gravity.END);
           }else{
               holder.layout.setGravity(Gravity.START);
               firestore.collection("Users").document(msgModel.getByWho()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                   @Override
                   public void onSuccess(DocumentSnapshot documentSnapshot) {
                       if(documentSnapshot!=null){
                           holder.name.setText(Objects.requireNonNull(documentSnapshot.get("username")).toString());
                       }
                   }
               });
               holder.name.setText(username);

           }


            holder.msg.setText(msgModel.getMsg());
            holder.time.setText(msgModel.getDate());
            firestore.collection("Chats").document(Objects.requireNonNull(auth.getUid())).collection("AllChats").document(msgModel.getByWho()).collection("Messages").document(msgModel.getDocumentId()).update("viewed","1");

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layout;
        TextView name, time, msg;
        String message;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textView3);
            msg = itemView.findViewById(R.id.textView4);
            time = itemView.findViewById(R.id.reply_time);
            layout = itemView.findViewById(R.id.layout);


        }
    }

}
