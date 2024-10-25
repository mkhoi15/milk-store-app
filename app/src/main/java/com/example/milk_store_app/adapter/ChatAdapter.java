package com.example.milk_store_app.adapter;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.milk_store_app.R;
import com.example.milk_store_app.models.entities.Message;
import com.example.milk_store_app.session.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<Message> messageList;
    private String currentUserId;
    private SessionManager sessionManager;

    public ChatAdapter(List<Message> messageList, String currentUserId, SessionManager sessionManager) {
        this.messageList = messageList;
        this.currentUserId = currentUserId;
        this.sessionManager = sessionManager;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_items, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Message message = messageList.get(position);

        // Check for null values in message
        if (message != null) {
            String messageContent = message.getContent();
            String senderId = message.getSenderId();
            long timestamp = message.getTimestamp();

            // Set message text and ensure it's not null
            holder.messageTextView.setText(messageContent != null ? messageContent : ""); // Default to empty string if null

            // Check sender ID to determine message alignment and background
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.messageContainer.getLayoutParams();

            // Check if the user is staff or not using SessionManager.isShopStaff()
            // If the current user is staff, handle alignment for staff messages
            if(sessionManager.isCustomer()) {
                if (senderId != null && senderId.equals(currentUserId)) {
                    // This is the staff's own message
                    holder.messageTextView.setBackgroundResource(R.drawable.message_bubble); // Staff's message bubble
                    layoutParams.gravity = Gravity.END; // Align to the right
                    holder.messageTextView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END); // Text alignment for staff
                } else {
                    // This is the customer's message
                    holder.messageTextView.setBackgroundResource(R.drawable.message_bubble_recieved); // Customer's message bubble
                    layoutParams.gravity = Gravity.START; // Align to the left
                    holder.messageTextView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START); // Text alignment for customer
                }
            } else if(sessionManager.isShopStaff()){
                if (senderId != null && senderId.equals("5f3e80fd-3aeb-4480-872a-21db9b93b5ec")) {
                    // This is the staff's own message
                    holder.messageTextView.setBackgroundResource(R.drawable.message_bubble); // Staff's message bubble
                    layoutParams.gravity = Gravity.END; // Align to the right
                    holder.messageTextView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END); // Text alignment for staff
                } else {
                    // This is the customer's message
                    holder.messageTextView.setBackgroundResource(R.drawable.message_bubble_recieved); // Customer's message bubble
                    layoutParams.gravity = Gravity.START; // Align to the left
                    holder.messageTextView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START); // Text alignment for customer
                }
            }

            holder.messageContainer.setLayoutParams(layoutParams);

            // Set message timestamp, ensuring it's valid
            holder.timestampTextView.setText(formatTimestamp(timestamp));
        }
    }


    @Override
    public int getItemCount() {
        return messageList != null ? messageList.size() : 0; // Handle case where messageList could be null
    }

    // ViewHolder for chat messages
    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        public TextView messageTextView;
        public TextView timestampTextView;
        public LinearLayout messageContainer;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            timestampTextView = itemView.findViewById(R.id.timestampTextView);
            messageContainer = itemView.findViewById(R.id.messageContainer);
        }
    }

    // Helper method to format timestamp
    private String formatTimestamp(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }
}
