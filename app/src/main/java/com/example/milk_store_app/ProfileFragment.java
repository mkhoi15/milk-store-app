package com.example.milk_store_app;

import android.content.Intent;
import android.net.Uri;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.UUID;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private TextView userName, fullName, phone, email;
    private Button logoutButton, mapButton;
    private UserServices userServices;
    private SessionManager sessionManager;
    private UserResponse user;

    // Store coordinates
    private FusedLocationProviderClient fusedLocationProviderClient;
    private final double STORE_LATITUDE = 10.841254037474895; // Example: Replace with your store latitude
    private final double STORE_LONGITUDE = 106.80990445226593; // Example: Replace with your store longitude

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize UI components
        userName = view.findViewById(R.id.user_name);
        fullName = view.findViewById(R.id.full_name);
        phone = view.findViewById(R.id.phone);
        email = view.findViewById(R.id.email);
        logoutButton = view.findViewById(R.id.logout_button);
        mapButton = view.findViewById(R.id.map_button); // Map button

        userServices = UserRepository.getUserServices(getContext());
        sessionManager = new SessionManager(getContext());
        String userId = sessionManager.fetchUserId();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        // Fetch user data
        fetchUserData(userId);

        // Set up logout button listener
        logoutButton.setOnClickListener(v -> handleLogout());

        // Set up map button listener
        mapButton.setOnClickListener(v -> openMapLocation());

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

    private void openMapLocation() {
        String geoUri = "geo:" + STORE_LATITUDE + "," + STORE_LONGITUDE + "?q=" + STORE_LATITUDE + "," + STORE_LONGITUDE + "(VK2PQ+Milk+Store)";
        Uri gmmIntentUri = Uri.parse(geoUri);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");

        // Verify that the device can handle the Intent
        if (mapIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            Toast.makeText(getContext(), "Google Maps is not installed", Toast.LENGTH_SHORT).show();
        }
    }
}
