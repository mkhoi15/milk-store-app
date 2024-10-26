package com.example.milk_store_app;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ChatFragment chatFragment;
    CustomerHomeFragment cusHomeFragment;
    CustomerOrder cusOrderFragment;
    ProfileFragment profileFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        cusHomeFragment = new CustomerHomeFragment();
        chatFragment = new ChatFragment();
        cusOrderFragment = new CustomerOrder();
        profileFragment = new ProfileFragment();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.menu_home) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_fragment_container, cusHomeFragment) // Replace with your container ID
                        .commit();
                return true;
            } else if (item.getItemId() == R.id.menu_chat) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_fragment_container, chatFragment) // Replace with your container ID
                        .commit();
                return true;
            } else if (item.getItemId() == R.id.menu_order) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_fragment_container, cusOrderFragment)
                        .commit();
                return true;
            } else if (item.getItemId() == R.id.menu_profile) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_fragment_container, profileFragment)
                        .commit();
                return true;
            }
            return false;
        });
        // Load the default fragment on startup
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_fragment_container, cusHomeFragment)
                    .commit();
        }
    }
}