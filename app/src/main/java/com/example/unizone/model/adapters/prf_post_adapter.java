package com.example.unizone.model.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.unizone.R;
import com.example.unizone.comments;
import com.example.unizone.model.prf_post_model;

import java.util.ArrayList;

public class prf_post_adapter extends RecyclerView.Adapter<prf_post_adapter.ViewHolder> {
    Context context;
    ArrayList<prf_post_model> list;
    public prf_post_adapter(Context context, ArrayList<prf_post_model> list) {
        this.context=context;
        this.list=list;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.prf_post_item,parent,false);
        return new prf_post_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        prf_post_model prfPostModel=list.get(position);
        if(prfPostModel.getPost()!=null) {
            Glide.with(context).load(prfPostModel.getPost()).placeholder(R.drawable.profile).error(R.drawable.profile).into(holder.imageView);
        }
        holder.itemView.setOnClickListener(v -> {
            Intent intent=new Intent(context, comments.class);
            intent.putExtra("ids",prfPostModel.getAuth_id());
            String time=String.valueOf(prfPostModel.getTime());
            intent.putExtra("timeStamp",time);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageView);
        }
    }
}
