package com.example.milk_store_app;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.milk_store_app.adapter.ChatAdapter;
import com.example.milk_store_app.models.entities.Message;
import com.example.milk_store_app.session.SessionManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StaffChatActivity extends AppCompatActivity {
    private RecyclerView recyclerViewMessages;
    private EditText editTextMessage;
    private Button buttonSend;
    private ChatAdapter messageAdapter;
    private List<Message> messageList;

    private String currentUserId;
    private String chatId;
    private String selectedUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initializeViews();
        setupRecyclerView();

        currentUserId = new SessionManager(this).fetchUserId();
        chatId = getIntent().getStringExtra("chatId");
        selectedUserId = getIntent().getStringExtra("userId");

        // Load messages for the chat
        loadMessages();
    }

    private void initializeViews() {
        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);
    }

    private void setupRecyclerView() {
        messageList = new ArrayList<>();
        SessionManager sessionManager = new SessionManager(this);
        messageAdapter = new ChatAdapter(messageList, currentUserId, sessionManager);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMessages.setAdapter(messageAdapter);
    }

    private void loadMessages() {
        // Reference to the messages in the chat
        DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("chats").child(chatId).child("messages");
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messageList.clear();
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    Message message = messageSnapshot.getValue(Message.class);
                    if (message != null) {
                        messageList.add(message);
                    }
                }
                messageAdapter.notifyDataSetChanged();
                if (!messageList.isEmpty()) {
                    recyclerViewMessages.scrollToPosition(messageList.size() - 1); // Scroll to the last message
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(StaffChatActivity.this, "Failed to load messages!", Toast.LENGTH_SHORT).show();
            }
        });

        // Set the onClick listener to send messages to the selected user
        buttonSend.setOnClickListener(v -> sendMessage());
    }

    private void sendMessage() {
        String content = editTextMessage.getText().toString().trim();
        if (!TextUtils.isEmpty(content)) {
            long timestamp = System.currentTimeMillis();
            Message message = new Message("5f3e80fd-3aeb-4480-872a-21db9b93b5ec", selectedUserId, content, timestamp);

            // Push the message to Firebase
            DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("chats").child(chatId).child("messages").push();
            chatRef.setValue(message).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    editTextMessage.setText(""); // Clear input field
                    updateUserChatData(content, timestamp);
                } else {
                    Toast.makeText(StaffChatActivity.this, "Failed to send message!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Message cannot be empty!", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUserChatData(String lastMessage, long timestamp) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");

        // Update the last message and timestamp for both users
        userRef.child(currentUserId).child("lastMessage").setValue(lastMessage);
        userRef.child(currentUserId).child("timestamp").setValue(timestamp);

        userRef.child(selectedUserId).child("lastMessage").setValue(lastMessage);
        userRef.child(selectedUserId).child("timestamp").setValue(timestamp);
    }
}
