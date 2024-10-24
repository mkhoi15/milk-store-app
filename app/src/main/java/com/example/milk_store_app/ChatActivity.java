package com.example.milk_store_app;

import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private EditText messageInput;
    private Button sendButton;
    private RecyclerView recyclerViewMessages;
    private String currentUserId; // ID of the current user (customer)
    private DatabaseReference databaseReference; // Database reference for messages
    private ChatAdapter chatAdapter; // Adapter for RecyclerView
    private List<Message> messageList; // List of messages

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Initialize Firebase Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true); // Enable offline capabilities (optional)

        // Set the database reference to your messages path
        currentUserId = new SessionManager(this).fetchUserId();
        String chatId = "chat_" + currentUserId + "_" + "5f3e80fd-3aeb-4480-872a-21db9b93b5ec"; // Create a unique chat ID
        databaseReference = database.getReference("chats").child(chatId).child("messages");



        // Initialize UI components
        messageInput = findViewById(R.id.editTextMessage);
        sendButton = findViewById(R.id.buttonSend);
        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        messageList = new ArrayList<>();
        SessionManager sessionManager = new SessionManager(this);
        chatAdapter = new ChatAdapter(messageList, currentUserId, sessionManager);

        // Set up RecyclerView
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMessages.setAdapter(chatAdapter);

        // Load messages from Firebase
        loadMessages();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(messageInput.getText().toString());
            }
        });
    }

    private void loadMessages() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    messageList.clear(); // Clear the message list before adding new messages
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Message message = snapshot.getValue(Message.class);
                        if (message != null) {
                            messageList.add(message); // Add the message to the list
                        }
                    }
                    chatAdapter.notifyDataSetChanged(); // Notify the adapter to update the view
                    if (!messageList.isEmpty()) {
                        recyclerViewMessages.scrollToPosition(messageList.size() - 1); // Scroll to the last message
                    }
                } catch (Exception e) {
                    Toast.makeText(ChatActivity.this, "Error loading messages: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ChatActivity.this, "Failed to load messages!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sendMessage(String messageText) {
        if (messageText.isEmpty()) {
            messageInput.setError("Message cannot be empty!");
            return;
        }

        // Generate a unique message ID for this message
        String messageId = databaseReference.push().getKey();
        long timestamp = System.currentTimeMillis();

        // Create the message object
        Message message = new Message(currentUserId, "5f3e80fd-3aeb-4480-872a-21db9b93b5ec", messageText, timestamp);

        // Send the message to the "chats" node
        databaseReference.child(messageId).setValue(message).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Update participants in the "chats" node
                databaseReference.child("participants").child(currentUserId).setValue(true);
                databaseReference.child("participants").child("5f3e80fd-3aeb-4480-872a-21db9b93b5ec").setValue(true);

                // Update the "users" node to store the last message
                updateUserChatData(messageText, timestamp);

                // Clear the input field
                messageInput.setText("");
                Toast.makeText(ChatActivity.this, "Message sent!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ChatActivity.this, "Failed to send message!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserChatData(String lastMessage, long timestamp) {
        // Reference to the "users" node in Firebase
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
        String currentUserName = new SessionManager(this).fetchNameIdentifier();
        // Update the current user (customer)
        userRef.child(currentUserId).child("lastMessage").setValue(lastMessage);
        userRef.child(currentUserId).child("userId").setValue(currentUserId);
        userRef.child(currentUserId).child("userName").setValue(currentUserName); // Replace with actual user name if available
    }
}
