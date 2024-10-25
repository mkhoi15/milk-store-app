package com.example.milk_store_app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.milk_store_app.R;
import com.example.milk_store_app.models.entities.UserChat;
import java.util.List;


public class UserChatAdapter extends RecyclerView.Adapter<UserChatAdapter.UserViewHolder> {
    private List<UserChat> userChatList;
    private OnUserMessageClickListener listener;

    public UserChatAdapter(List<UserChat> userChatList, OnUserMessageClickListener listener) {
        this.userChatList = userChatList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_list_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserChat userChat = userChatList.get(position);
        holder.textViewUserName.setText(userChat.getUserName());
        holder.textViewLastMessage.setText(userChat.getLastMessage());

        // Format the timestamp to a readable date format
        holder.itemView.setOnClickListener(v -> listener.onUserMessageClick(userChat));
    }

    @Override
    public int getItemCount() {
        return userChatList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView textViewUserName;
        TextView textViewLastMessage; // Added for last message
        TextView textViewTimestamp; // Added for timestamp

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUserName = itemView.findViewById(R.id.userNameTextView);
            textViewLastMessage = itemView.findViewById(R.id.lastMessageTextView); // Bind last message TextView
            textViewTimestamp = itemView.findViewById(R.id.timestampTextView); // Bind timestamp TextView
        }
    }

    public interface OnUserMessageClickListener {
        void onUserMessageClick(UserChat userChat);
    }
}
