package com.example.milk_store_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.milk_store_app.adapter.UserAdapter;
import com.example.milk_store_app.models.entities.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MessageListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMessages;
    private UserAdapter userMessageAdapter;
    private List<User> userMessageList;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        userMessageList = new ArrayList<>();
        userMessageAdapter = new UserAdapter(userMessageList, userMessage -> {
            // Handle user message click
            openChat(userMessage.getUserId());
        });

        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMessages.setAdapter(userMessageAdapter);

        userRef = FirebaseDatabase.getInstance().getReference("users");

        loadUsers();
    }

    private void loadUsers() {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userMessageList.clear(); // Clear the previous list
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    if (user != null) {
                        userMessageList.add(user); // Add each user to the list
                    }
                }
                userMessageAdapter.notifyDataSetChanged(); // Notify adapter of changes
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MessageListActivity.this, "Failed to load users!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void openChat(String userId) {
        String currentStaffId = "5f3e80fd-3aeb-4480-872a-21db9b93b5ec"; // Staff ID
        // Construct the chat ID based on the user clicked and the current staff ID
        String chatId = "chat_" + userId + "_" + currentStaffId;
        DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("chats").child(chatId).child("messages").child("participants");

        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // If the chat exists, open the chat activity
                    Intent intent = new Intent(MessageListActivity.this, StaffChatActivity.class);
                    intent.putExtra("chatId", chatId);
                    intent.putExtra("userId", userId);
                    startActivity(intent);
                } else {
                    // If no chat exists, create a new one
                    Toast.makeText(MessageListActivity.this, "No chat exists with this user.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MessageListActivity.this, "Failed to check chats!", Toast.LENGTH_SHORT).show();
            }
        });
    }


}