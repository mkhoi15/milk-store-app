package com.example.milk_store_app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.milk_store_app.adapter.ProductAdapter;
import com.example.milk_store_app.models.response.ProductResponse;
import com.example.milk_store_app.repository.ProductRepository;
import com.example.milk_store_app.services.ProductServices;
import com.example.milk_store_app.session.CartManager;
import com.example.milk_store_app.session.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewActivity extends AppCompatActivity {
    ArrayList<ProductResponse> productsList;
    ProductServices productServices;
    ProductAdapter adapter;
    ListView listView;
    ScrollView scrollView;
    EditText search;
    Button btnSearch, btnCart, btnOrderHistory;
    CartManager cartManager;
    SessionManager sessionManager;

    private static final String CHANNEL_ID = "cart_notification_channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        projectData();

        createNotificationChannel();

        showCartNotification();

        btnSearch.setOnClickListener(v -> {
            loadProducts(search.getText().toString());
        });
        btnCart.setOnClickListener(v -> {
            Intent intent = new Intent(HomeViewActivity.this, CartViewActivity.class);
            startActivity(intent);
        });

        btnOrderHistory.setOnClickListener(v -> {
            Intent intent = new Intent(HomeViewActivity.this, CustomerOrderHistoryActivity.class);
            startActivity(intent);
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            ProductResponse product = productsList.get(position);
            Intent intent = new Intent(HomeViewActivity.this, ProductDetailActivity.class);
            intent.putExtra("productId", product.getId().toString());
            startActivity(intent);
        });
    }

    private void projectData() {
        productsList = new ArrayList<>();
        adapter = new ProductAdapter(this, productsList, R.layout.product_item_list);
        listView = (ListView) findViewById(R.id.product_list);
        search = (EditText) findViewById(R.id.search_bar);
        btnSearch = (Button) findViewById(R.id.btn_search);
        btnCart = (Button) findViewById(R.id.btn_cart);
        listView.setAdapter(adapter);
        btnOrderHistory = (Button) findViewById(R.id.btn_order_history);

        productServices = ProductRepository.getProductServices(this);
        cartManager = new CartManager(this);
        sessionManager = new SessionManager(this);

        if (sessionManager.isAdmin()) {
            btnOrderHistory.setVisibility(View.GONE);
        }

        loadProducts("");

        NotificationCompat.Builder builder = new NotificationCompat
                .Builder(this, "channelId")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Have items in cart")
                .setContentText("You have items in cart, please checkout")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);


    }

    private void loadProducts(String search) {
        productServices.getProducts(
                1,
                10,
                search,
                ""
        ).enqueue(new Callback<List<ProductResponse>>() {
            @Override
            public void onResponse(Call<List<ProductResponse>> call, Response<List<ProductResponse>> response) {
                if (response.isSuccessful()) {
                    productsList.clear();
                    productsList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<ProductResponse>> call, Throwable t) {
                Toast.makeText(HomeViewActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Create the notification channel (for Android 8+)
    private void createNotificationChannel() {
        CharSequence name = "Cart Notification";
        String description = "Notifications for cart items";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);
        // Register the channel with the system
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    // Show the cart notification
    private void showCartNotification() {
        if (cartManager.getCart().isEmpty()) {
            return;
        }
        NotificationCompat.Builder builder = new NotificationCompat
                .Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher) // Set your notification icon here
                .setContentTitle("Cart Reminder")
                .setContentText("You have items in your cart. Please checkout.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(1, builder.build());
    }
}
