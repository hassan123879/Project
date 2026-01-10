package com.example.project;


import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText messageEditText;
    private Button sendButton;

    private FirebaseFirestore db;

    private String senderId = "USER_1";     // Logged-in user
    private String receiverId = "USER_2";   // Chatting with

    private String chatId;

    private List<Message> messageList = new ArrayList<>();
    private ChatAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerView = findViewById(R.id.chatRecyclerView);
        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);

        db = FirebaseFirestore.getInstance();

        chatId = senderId.compareTo(receiverId) < 0 ?
                senderId + "_" + receiverId :
                receiverId + "_" + senderId;

        adapter = new ChatAdapter(messageList, senderId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        listenForMessages();

        sendButton.setOnClickListener(v -> sendMessage());
    }

    private void sendMessage() {
        String msg = messageEditText.getText().toString().trim();
        if (msg.isEmpty()) return;

        Message message = new Message(
                senderId,
                receiverId,
                msg,
                System.currentTimeMillis()
        );

        db.collection("chats")
                .document(chatId)
                .collection("messages")
                .add(message);

        messageEditText.setText("");
    }

    private void listenForMessages() {
        db.collection("chats")
                .document(chatId)
                .collection("messages")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener((value, error) -> {
                    if (value != null) {
                        messageList.clear();
                        messageList.addAll(value.toObjects(Message.class));
                        adapter.notifyDataSetChanged();
                        recyclerView.scrollToPosition(messageList.size() - 1);
                    }
                });
    }
}
