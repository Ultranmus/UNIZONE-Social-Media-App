package com.example.unizone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.unizone.databinding.ActivitySettingBinding;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Setting extends AppCompatActivity {
private ActivitySettingBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Glide.with(this).load(R.drawable.holder).into(binding.imageView4);
        binding.btn.setOnClickListener(v -> {
            String text=binding.text.getText().toString().toLowerCase(Locale.ROOT).trim();
            if(TextUtils.isEmpty(text)){
                binding.text.setError("Empty!!");
                binding.text.requestFocus();
            }
            else{
                generateText(text);
            }
        });
    }
    private void generateText(String message) {
        OkHttpClient client = new OkHttpClient();

        String json = "{\"messages\": [{\"role\": \"system\", \"content\": \"You are ChatGPT, a language model\"}, {\"role\": \"user\", \"content\": \"" + message + "\"}], \"max_tokens\": 50, \"temperature\": 0.7, \"model\": \"gpt-3.5-turbo\"}";
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(json, mediaType);

        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                //don't use my api key
                .addHeader("Authorization", "Bearer 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")
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                try {
                    String responseBody = Objects.requireNonNull(response.body()).string();
                    extractResponseContent(responseBody);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }
        });
    }


    public void extractResponseContent(String responseBody) {

        try {
            JSONObject jsonObject = new JSONObject(responseBody);
            JSONArray choicesArray = jsonObject.getJSONArray("choices");
            JSONObject firstChoice = choicesArray.getJSONObject(0);
            JSONObject msg = firstChoice.getJSONObject("message");
            updateUIWithResponse(msg.getString("content"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void updateUIWithResponse(final String response) {
        runOnUiThread(() -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(Setting.this);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.gpt_dialog, null);
            TextView messageTextView = dialogView.findViewById(R.id.dialog_message);

            builder.setView(dialogView)
                    .setTitle("'"+binding.text.getText().toString().trim()+"'")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            messageTextView.setText(response);

            binding.text.setText("");
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }
}
