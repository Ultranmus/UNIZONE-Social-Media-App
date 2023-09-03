package com.example.unizone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextPaint;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.unizone.databinding.ActivityMainBinding;
import com.example.unizone.databinding.ActivitySeeMomentsBinding;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
//splash screen
    ActivityMainBinding bind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind= ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());
        TextPaint paint = bind.textView18.getPaint();
        float width = paint.measureText("UNIZONE");

        Shader textShader = new LinearGradient(0, 0, width, bind.textView18.getTextSize(),
                new int[]{
                        Color.parseColor("#F97C3C"),
                        Color.parseColor("#FDB54E"),
                        Color.parseColor("#64B678"),
                        Color.parseColor("#478AEA"),
                        Color.parseColor("#8446CC"),
                }, null, Shader.TileMode.CLAMP);
        bind.textView18.getPaint().setShader(textShader);
        Animation animation= AnimationUtils.loadAnimation(MainActivity.this,R.anim.move);
        Animation animation2= AnimationUtils.loadAnimation(MainActivity.this,R.anim.splash);
        bind.img.startAnimation(animation);
        bind.textView18.startAnimation(animation2);

        new Handler().postDelayed(() -> {
            startActivity(new Intent(MainActivity.this,SignIn.class));
            finish();
        },6000);
    }
}