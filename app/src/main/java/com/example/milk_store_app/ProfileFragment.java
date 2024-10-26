package com.example.milk_store_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.milk_store_app.models.response.UserResponse;
import com.example.milk_store_app.repository.UserRepository;
import com.example.milk_store_app.services.UserServices;
import com.example.milk_store_app.session.SessionManager;
import java.util.UUID;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private TextView userName, fullName, phone, email;
    private Button logoutButton;
    private UserServices userServices;
    private SessionManager sessionManager;
    private UserResponse user;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize UI components
        userName = view.findViewById(R.id.user_name);
        fullName = view.findViewById(R.id.full_name);
        phone = view.findViewById(R.id.phone);
        email = view.findViewById(R.id.email);
        logoutButton = view.findViewById(R.id.logout_button);

        userServices = UserRepository.getUserServices(getContext());
        sessionManager = new SessionManager(getContext());
        String userId = sessionManager.fetchUserId();
        // Fetch user data
        fetchUserData(userId);

        // Set up logout button listener
        logoutButton.setOnClickListener(v -> handleLogout());

        return view;
    }

    private void fetchUserData(String userId) {

        userServices.getUserById(UUID.fromString(userId)).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    user = response.body();
                    userName.setText(user.getUsername());
                    fullName.setText(user.getFullName());
                    phone.setText(user.getPhoneNumber());
                    email.setText(user.getEmail());
                }
            }
            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleLogout() {
        // Handle logout logic here
    }
}
