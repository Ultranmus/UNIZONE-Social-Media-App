package com.example.unizone;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.unizone.databinding.ActivityCreateMomentsBinding;
import com.example.unizone.model.MomentModel;
import com.example.unizone.model.Other_Status_Model;
import com.example.unizone.model.id;
import com.example.unizone.model.connection_model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Date;
import java.util.Objects;

public class createMoments extends AppCompatActivity {

    private final int REQUEST_TAKE_GALLERY_VIDEO=799;
    VideoView videoView;
    ImageButton imageButton;
    MediaController mediaController;
    AlertDialog dialog;
    EditText txt;
    ImageButton go;
    ProgressDialog buffer;
    FirebaseStorage storage;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.unizone.databinding.ActivityCreateMomentsBinding bind = ActivityCreateMomentsBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());
        videoView= bind.videoView;
        imageButton= bind.addBtn;
        buffer=new ProgressDialog(this);
        buffer.setMessage("Status is loading");
        buffer.setCancelable(false);
        buffer.setTitle("Uploading...");
        storage=FirebaseStorage.getInstance();
        auth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        imageButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("video/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Select Video"),REQUEST_TAKE_GALLERY_VIDEO);
        });
        mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
                assert data != null;
                Uri selectedVideoUri = data.getData();

                String vidPath;
                vidPath = selectedVideoUri.toString();


                // MEDIA GALLERY
                String selectedVideoPath = getPath(selectedVideoUri);
                if (selectedVideoPath != null) {
                    vidPath = selectedVideoPath;
                }
                imageButton.setVisibility(View.GONE);
                showStatusDialog();
                Uri uriVideo = Uri.parse(vidPath);
                videoView.setVideoURI(uriVideo);
                videoView.requestFocus();

                videoView.setOnPreparedListener(mp -> {
                    mp.setLooping(true);
                    videoView.start();
                });
                txt=dialog.findViewById(R.id.editTextTextMultiLine);
                go=dialog.findViewById(R.id.imageButton2);
                go.setOnClickListener(v -> {
                    String timeStamp = String.valueOf(new Date().getTime());
                    try {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(dialog.getWindow().getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                    String text=txt.getText().toString().trim();
                    txt.setText("");
                    txt.clearFocus();
                    buffer.show();
                    storage.getReference().child(Objects.requireNonNull(auth.getUid())).child("Status").child(timeStamp).putFile(uriVideo).addOnSuccessListener(taskSnapshot -> storage.getReference().child(auth.getUid()).child("Status").child(timeStamp).getDownloadUrl().addOnSuccessListener(uri -> firestore.collection("Users").document(auth.getUid()).collection("status").document(timeStamp).set(new MomentModel(uri.toString(), text, timeStamp)).addOnSuccessListener(unused -> firestore.collection("Users").document(auth.getUid()).collection("Connections").addSnapshotListener(createMoments.this, (value, error) -> {
                        if (error != null) {
                            dialog.dismiss();
                            Toast.makeText(createMoments.this, "Failed to create moments", Toast.LENGTH_SHORT).show();
                        }
                        if (value != null) {

                            for (QueryDocumentSnapshot snapshot : value) {
                                connection_model model = snapshot.toObject(connection_model.class);
                                firestore.collection("Users").document(model.getAuth_id()).collection("OtherStatus").document(auth.getUid()).collection("status").document(timeStamp).set(new Other_Status_Model(auth.getUid(), timeStamp,"0")).addOnSuccessListener(unused1 -> {
                                            firestore.collection("Users").document(model.getAuth_id()).collection("OtherStatus").document(auth.getUid()).set(new id(timeStamp));
                                        })
                                        .addOnFailureListener(e -> {
                                        });
                            }
                            buffer.dismiss();
                            Toast.makeText(createMoments.this, "Moments created successfully!!", Toast.LENGTH_SHORT).show();
                            finish();


                        } else {
                            dialog.dismiss();
                        }
                    })).addOnFailureListener(e -> {
                        dialog.dismiss();
                        Toast.makeText(createMoments.this, "Failed to create post!", Toast.LENGTH_SHORT).show();

                    })).addOnFailureListener(e -> {
                        buffer.dismiss();
                        Toast.makeText(createMoments.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    })).addOnFailureListener(e -> {
                        buffer.dismiss();
                        Toast.makeText(createMoments.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    });

                });
            }
        }
    }
    public String getPath(Uri uri) {
        String data;
        String[] projection = { MediaStore.Video.Media.DATA };
        Cursor cursor = getApplicationContext().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            data= cursor.getString(column_index);
        } else {
            data = null;
        }
        if(cursor!=null) {
            cursor.close();
        }
        return data;
    }
    private void showStatusDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.TransparentDialogTheme);
        View dialogView = getLayoutInflater().inflate(R.layout.keyboard, null);
        builder.setView(dialogView);
        dialog = builder.create();
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        dialog.setOnKeyListener((dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                finish();
                return true;
            }
            return false;
        });
        dialog.show();

        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}