package com.example.milk_store_app;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
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
    private List<DeliveryResponse> originalDeliveryOrderList;
    private List<DeliveryResponse> filteredDeliveryOrderList;
    DeliveryStaffOrderAdapter adapter;
    RecyclerView recyclerOrder;
    TextView textViewDeliveryManName;
    SessionManager sessionManager;
    Button buttonAll, buttonAssigned, buttonShipping, buttonDelivered;

    private Button selectedButton;

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
        setupFilterButtons();
        loadOrders();
    }

    private void projectData() {

        textViewDeliveryManName = findViewById(R.id.textViewDeliveryManName);
        recyclerOrder = findViewById(R.id.recyclerViewDeliveryStaffOrders);
        deliveryServices = DeliveryRepository.getDeliveryServices(this);
        buttonAll = findViewById(R.id.buttonAll);
        buttonAssigned = findViewById(R.id.buttonAssigned);
        buttonShipping = findViewById(R.id.buttonShipping);
        buttonDelivered = findViewById(R.id.buttonDelivered);

        originalDeliveryOrderList = new ArrayList<>();
        filteredDeliveryOrderList = new ArrayList<>();
        adapter = new DeliveryStaffOrderAdapter(filteredDeliveryOrderList, this, R.layout.activity_single_order_delivery_view);
        recyclerOrder.setLayoutManager(new LinearLayoutManager(this));
        recyclerOrder.setAdapter(adapter);

        sessionManager = new SessionManager(this);



        // Load orders from API
        //loadOrders();



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
                    originalDeliveryOrderList.clear();
                    originalDeliveryOrderList.addAll(response.body());

                    // Display all orders initially
                    selectButton(buttonAll, "All");

                    if (!originalDeliveryOrderList.isEmpty()) {
                        String deliveryManName = originalDeliveryOrderList.get(0).getDeliveryManName();
                        textViewDeliveryManName.setText("Delivery Man: " + deliveryManName);
                    }
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

    private void filterOrders(String status) {
        int previousSize = filteredDeliveryOrderList.size();
        filteredDeliveryOrderList.clear(); // Clear the current filtered list

        if (status.equals("All")) {
            filteredDeliveryOrderList.addAll(originalDeliveryOrderList);
            Log.d("DeliveryViewActivity", "All orders: " + originalDeliveryOrderList);
        } else {
            for (DeliveryResponse delivery : originalDeliveryOrderList) {
                String orderStatus = delivery.getOrder().getOrderStatus();
                if (orderStatus != null && orderStatus.equalsIgnoreCase(status)) {
                    filteredDeliveryOrderList.add(delivery); // Add orders with matching status
                }
            }
        }

        // Notify adapter of new items
        adapter.notifyItemRangeInserted(0, filteredDeliveryOrderList.size());

        // Optionally notify removed items for a complete refresh
        if (previousSize > 0) {
            adapter.notifyItemRangeRemoved(0, previousSize);
        }
    }


    private void setupFilterButtons() {
        buttonAll.setOnClickListener(v -> selectButton(buttonAll, "All"));
        buttonAssigned.setOnClickListener(v -> selectButton(buttonAssigned, "Assigned"));
        buttonShipping.setOnClickListener(v -> selectButton(buttonShipping, "Shipping"));
        buttonDelivered.setOnClickListener(v -> selectButton(buttonDelivered, "Delivered"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadOrders();
    }

    private void selectButton(Button button, String status) {
        // Reset color of previously selected button
        if (selectedButton != null) {
            selectedButton.setBackgroundColor(getResources().getColor(R.color.button_unselected));
        }
        // Set color of newly selected button
        button.setBackgroundColor(getResources().getColor(R.color.button_selected));
        selectedButton = button; // Update the selected button reference

        // Apply filter
        filterOrders(status);
    }

}
