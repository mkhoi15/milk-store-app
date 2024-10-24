package com.example.milk_store_app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.milk_store_app.R;
import com.example.milk_store_app.models.entities.User;
import java.util.List;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> userList;
    private OnUserMessageClickListener listener;

    public UserAdapter(List<User> userList, OnUserMessageClickListener listener) {
        this.userList = userList;
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
        User user = userList.get(position);
        holder.textViewUserName.setText(user.getUserName());
        holder.textViewLastMessage.setText(user.getLastMessage());

        // Format the timestamp to a readable date format
        holder.itemView.setOnClickListener(v -> listener.onUserMessageClick(user));
    }

    @Override
    public int getItemCount() {
        return userList.size();
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
        void onUserMessageClick(User user);
    }
}
