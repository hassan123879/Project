package com.example.project;

import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.*;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatbotActivity extends AppCompatActivity {

    TextView chatView;
    EditText inputMessage;
    Button sendBtn;

    StringBuilder conversationHistory = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);

        chatView = findViewById(R.id.chatView);
        inputMessage = findViewById(R.id.inputMessage);
        sendBtn = findViewById(R.id.sendBtn);

        sendBtn.setOnClickListener(v -> sendMessage());
    }

    private void sendMessage() {
        String userMsg = inputMessage.getText().toString().trim();
        if (userMsg.isEmpty()) return;

        chatView.append("\nYou: " + userMsg);
        conversationHistory.append("User: ").append(userMsg).append("\n");

        inputMessage.setText("");

        callGeminiAPI(userMsg);
    }

    private void callGeminiAPI(String userMsg) {

        String API_KEY = "AIzaSyBugJdPz7TaH9CykQKMiDl5kTZm6Hg9sqc";

        String prompt =
                "You are an intelligent AI assistant for a mobile app.\n" +
                        "App features:\n" +
                        "- QR Code Scanner\n" +
                        "- Chatbot assistance\n\n" +
                        "Rules:\n" +
                        "- Answer only app-related questions\n" +
                        "- Be clear and concise\n\n" +
                        "Conversation:\n" +
                        conversationHistory +
                        "\nUser: " + userMsg;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://generativelanguage.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GeminiApiService api = retrofit.create(GeminiApiService.class);

        GeminiRequest request = new GeminiRequest(prompt);

        Call<GeminiResponse> call = api.generateContent(API_KEY, request);

        call.enqueue(new Callback<GeminiResponse>() {
            @Override
            public void onResponse(Call<GeminiResponse> call, Response<GeminiResponse> response) {

                if (!response.isSuccessful() || response.body() == null) {
                    chatView.append("\nBot: Error " + response.code());
                    return;
                }
                Log.e("GEMINI_RAW", response.errorBody() != null
                        ? response.errorBody().toString()
                        : "No error body");

                try {
                    String reply = response.body()
                            .candidates[0]
                            .content
                            .parts[0]
                            .text;

                    conversationHistory.append("Bot: ").append(reply).append("\n");
                    chatView.append("\nBot: " + reply);

                } catch (Exception e) {
                    chatView.append("\nBot: Parsing error");
                }
            }

            @Override
            public void onFailure(Call<GeminiResponse> call, Throwable t) {
                chatView.append("\nBot: Network error");
            }
        });
    }

}
