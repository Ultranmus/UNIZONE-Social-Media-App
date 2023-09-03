package com.example.unizone;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.unizone.databinding.ActivityTabsBinding;
import com.example.unizone.model.adapters.SectionsPagerAdapter;
import com.example.unizone.model.user_model;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Objects;

public class Tabs extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private NavigationView navigationView;
    TextView header_username;
    TextView header_about;
    ImageView header_profile;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.unizone.databinding.ActivityTabsBinding binding = ActivityTabsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        drawer=binding.drawer;
//        toolbar=binding.toolBar;
        toolbar=findViewById(R.id.toolBar);
        ViewPager viewPager=findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(3); // ***************************** important for fragment state

        navigationView=binding.navigationView;
        View header=navigationView.getHeaderView(0);
        header_profile=header.findViewById(R.id.header_profile);
        header_username=header.findViewById(R.id.header_username);
        header_about=header.findViewById(R.id.header_about);
        setSupportActionBar(toolbar);


        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());

        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(item ->

        {
            int id = item.getItemId();
            if (id == R.id.log_out) {
                auth.signOut();
                startActivity(new Intent(getApplicationContext(), SignIn.class));
                finish();
            }
            else if (id == R.id.setting){
                startActivity(new Intent(getApplicationContext(), Setting.class));
                drawer.closeDrawer(navigationView);


            }else if (id == R.id.search){
                startActivity(new Intent(getApplicationContext(), search.class));
                drawer.closeDrawer(navigationView);


            }
            else if (id == R.id.createPost){
                startActivity(new Intent(getApplicationContext(), create_post.class));
                drawer.closeDrawer(navigationView);


            }
            else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext())
                        .setIcon(R.drawable.ic_baseline_logout_24)
                        .setTitle("LOG OUT")
                        .setMessage("Are You Sure!")
                        .setNegativeButton("No", (dialogInterface, i) -> Toast.makeText(getApplicationContext(), "You are Logged in", Toast.LENGTH_SHORT).show());
                builder.show();

                builder.setCancelable(false);

            }

            return true;
        });
        header_profile.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(),profile.class));
            drawer.closeDrawer(navigationView);
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        firestore.collection("Users").document(Objects.requireNonNull(auth.getUid())).addSnapshotListener(this,new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                assert value != null;
                if(value.exists()){
                    user_model userModel=value.toObject(user_model.class);
                    assert userModel != null;
                    header_username.setText(userModel.getUsername());
                    header_about.setText(userModel.getAbout());
                    Glide.with(getApplicationContext()).load(userModel.getProfile()).error(R.drawable.profile).placeholder(R.drawable.profile).into(header_profile);

                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(navigationView))
            drawer.closeDrawer(navigationView);

        else
            super.onBackPressed();
    }
    }
