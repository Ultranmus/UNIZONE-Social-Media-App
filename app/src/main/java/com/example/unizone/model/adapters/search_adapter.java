package com.example.unizone.model.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.unizone.R;
import com.example.unizone.follow_user;
import com.example.unizone.model.user_model;
import com.example.unizone.model.usernames_model;
import com.example.unizone.profile;
import com.example.unizone.search;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class search_adapter extends ArrayAdapter<user_model> {
    private final ArrayList<user_model> searchListFull;
    private FirebaseFirestore firestore;
//    private final ArrayList<user_model> searchListFull;

    public search_adapter(@NonNull Context context, @NonNull List<user_model> objects, FirebaseFirestore firestore) {
        super(context, 0, objects);
        searchListFull = new ArrayList<>(objects);
        this.firestore=firestore;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return userFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.moments_item, parent, false
            );
        }

        TextView username = convertView.findViewById(R.id.username_txt);
        TextView about = convertView.findViewById(R.id.status_txt);
        ImageView pic = convertView.findViewById(R.id.imageView1);

        user_model model = getItem(position);

        if (model != null) {

            username.setText(model.getUsername());
            about.setText(model.getAbout());
            Glide.with(getContext()).load(model.getProfile()).error(R.drawable.profile).placeholder(R.drawable.profile).into(pic);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getContext(), follow_user.class);
                    intent.putExtra("id",model.getId());
                    getContext().startActivity(intent);
                    ((search)getContext()).finish();
                }
            });
        }

        return convertView;
    }
    private final Filter userFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<user_model> suggestions = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
//                suggestions.addAll(searchListFull);

            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (user_model model : searchListFull) {
                    if (model.getUsername().toLowerCase().contains(filterPattern)) {
                        suggestions.add(model);
                    }
                }
            }

            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((user_model) resultValue).getUsername();
        }
    };
    }
