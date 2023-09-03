package com.example.unizone.model.adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.unizone.R;
import com.example.unizone.model.MomentModel;
import com.example.unizone.seeMoments;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {
    Context context;
    ArrayList<MomentModel> list;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    MediaController mediaController;
    RecyclerView pager;
    String byWho;

    public ViewPagerAdapter(Context context, ArrayList<MomentModel> list, FirebaseFirestore firestore, FirebaseAuth auth,RecyclerView pager,String byWho) {
        this.context = context;
        this.list = list;
        this.firestore = firestore;
        this.auth = auth;
        this.pager=pager;
        this.byWho=byWho;
    }

    @NonNull
    @Override
    public ViewPagerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.viewpager_item,parent,false);
        return new ViewPagerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewPagerAdapter.ViewHolder holder, int position) {
        mediaController = new MediaController(context);
        mediaController.setAnchorView(holder.videoView);
        holder.videoView.setMediaController(mediaController);

        MomentModel model=list.get(position);
        holder.description.setText(model.getDescription());
        holder.description.setSelected(true);
        holder.videoView.setVideoURI(Uri.parse(model.getVideo()));
        holder.videoView.requestFocus();
        holder.videoView.setOnPreparedListener(mp -> {
            mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                    if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START)
                        holder.buffer.setVisibility(View.VISIBLE);
                    if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END)
                        holder.buffer.setVisibility(View.GONE);
                    return false;
                }
            });
            holder.buffer.setVisibility(View.GONE);
            holder.videoView.start();
            if(!byWho.equals(auth.getUid())) {
                firestore.collection("Users").document(Objects.requireNonNull(auth.getUid())).collection("OtherStatus").document(byWho).collection("status").document(model.getTimeStamp()).update("viewed", "1");
            }
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if(holder.getAdapterPosition() == list.size()-1){
                        ((seeMoments)context).finish();
                    }
                    else{
                        pager.smoothScrollToPosition(holder.getAdapterPosition()+1);
                    }
                }
            });
        });
        holder.videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                holder.buffer.setVisibility(View.GONE);
                return false;
            }
        });
    }
    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        // Resume the video playback when the view is attached to the window
        holder.buffer.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ProgressBar buffer;
        VideoView videoView;
        TextView description;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            buffer=itemView.findViewById(R.id.buffer);
            videoView=itemView.findViewById(R.id.videoView);
            description=itemView.findViewById(R.id.description);
        }
    }
}
