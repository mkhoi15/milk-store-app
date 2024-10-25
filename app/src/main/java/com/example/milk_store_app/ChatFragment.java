package com.example.milk_store_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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

public class ChatFragment extends Fragment {
    private EditText messageInput;
    private Button sendButton;
    private RecyclerView recyclerViewMessages;
    private String currentUserId;
    private DatabaseReference databaseReference;
    private ChatAdapter chatAdapter;
    private List<Message> messageList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        // Initialize Firebase Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);

        // Set the database reference to your messages path
        currentUserId = new SessionManager(requireContext()).fetchUserId();
        String chatId = "chat_" + currentUserId + "_" + "5f3e80fd-3aeb-4480-872a-21db9b93b5ec";
        databaseReference = database.getReference("chats").child(chatId).child("messages");

        // Initialize UI components
        messageInput = view.findViewById(R.id.editTextMessage);
        sendButton = view.findViewById(R.id.buttonSend);
        recyclerViewMessages = view.findViewById(R.id.recyclerViewMessages);
        messageList = new ArrayList<>();
        SessionManager sessionManager = new SessionManager(requireContext());
        chatAdapter = new ChatAdapter(messageList, currentUserId, sessionManager);

        // Set up RecyclerView
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewMessages.setAdapter(chatAdapter);

        // Load messages from Firebase
        loadMessages();

        sendButton.setOnClickListener(v -> sendMessage(messageInput.getText().toString()));

        return view;
    }

    private void loadMessages() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    messageList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Message message = snapshot.getValue(Message.class);
                        if (message != null) {
                            messageList.add(message);
                        }
                    }
                    chatAdapter.notifyDataSetChanged();
                    if (!messageList.isEmpty()) {
                        recyclerViewMessages.scrollToPosition(messageList.size() - 1);
                    }
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Error loading messages: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load messages!", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getContext(), "Message sent!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Failed to send message!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserChatData(String lastMessage, long timestamp) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
        String currentUserName = new SessionManager(requireContext()).fetchNameIdentifier();

        userRef.child(currentUserId).child("lastMessage").setValue(lastMessage);
        userRef.child(currentUserId).child("userId").setValue(currentUserId);
        userRef.child(currentUserId).child("userName").setValue(currentUserName);
    }
}
