package com.example.unizone;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class OpenAIChatAPI {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    //don't use my api key
    private static final String API_KEY = "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";

    public static void sendMessage(String message, final okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();

        String json = "{\"messages\": [{\"role\": \"system\", \"content\": \"You are ChatGPT, a language model\"}, {\"role\": \"user\", \"content\": \"" + message + "\"}]}";
        RequestBody body = RequestBody.create(json, JSON);

        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        client.newCall(request).enqueue(callback);
    }
}