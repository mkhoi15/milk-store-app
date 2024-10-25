package com.example.milk_store_app;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.milk_store_app.adapter.DeliveryStaffOrderAdapter;
import com.example.milk_store_app.models.response.DeliveryResponse;
import com.example.milk_store_app.models.response.OrderDeliveryResponse;
import com.example.milk_store_app.repository.DeliveryRepository;
import com.example.milk_store_app.services.DeliveryServices;
import com.example.milk_store_app.session.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryViewActivity extends AppCompatActivity {

    DeliveryServices deliveryServices;
    List<DeliveryResponse> deliveryOrderList;
    DeliveryStaffOrderAdapter adapter;
    RecyclerView recyclerOrder;
    TextView textViewDeliveryManName;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_delivery_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Initialize views and adapter
        projectData();
    }

    private void projectData() {
        textViewDeliveryManName = findViewById(R.id.textViewDeliveryManName);
        recyclerOrder = findViewById(R.id.recyclerViewDeliveryStaffOrders);
        deliveryServices = DeliveryRepository.getDeliveryServices(this);
        deliveryOrderList = new ArrayList<>();
        adapter = new DeliveryStaffOrderAdapter(deliveryOrderList, this, R.layout.activity_single_order_delivery_view);
        recyclerOrder.setLayoutManager(new LinearLayoutManager(this));
        recyclerOrder.setAdapter(adapter);
        sessionManager = new SessionManager(this);
        loadOrders(); // Load orders from API
    }

    private void loadOrders() {
        deliveryServices.getDeliveriesByUserId(
                sessionManager.fetchUserId(), // Assuming you pass the userId here
                1, // Page number
                100 // Page size
        ).enqueue(new Callback<List<DeliveryResponse>>() {
            @Override
            public void onResponse(Call<List<DeliveryResponse>> call, Response<List<DeliveryResponse>> response) {
                if (response.isSuccessful()) {
                    deliveryOrderList.clear();
                    deliveryOrderList.addAll(response.body()); // Update the list with API data
                    adapter.notifyItemRangeInserted(0, deliveryOrderList.size()); // Notify the adapter to refresh RecyclerView
                }


                if (!deliveryOrderList.isEmpty()) {
                    String deliveryManName = deliveryOrderList.get(0).getDeliveryStaffName(); // Adjust based on actual API response
                    textViewDeliveryManName.setText("Delivery Man: " + deliveryManName);
                }

            }

            @Override
            public void onFailure(Call<List<DeliveryResponse>> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DeliveryViewActivity.this);
                builder.setTitle("Error");
                builder.setMessage("An error occurred while loading orders");
                builder.setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                });
                builder.show();
            }
        });
    }
}
