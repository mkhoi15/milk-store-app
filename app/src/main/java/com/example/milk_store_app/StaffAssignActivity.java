package com.example.milk_store_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.milk_store_app.adapter.DeliveryAdapter;
import com.example.milk_store_app.models.request.CreateOrderRequest;
import com.example.milk_store_app.models.response.DeliveryResponse;
import com.example.milk_store_app.models.response.UserResponse;
import com.example.milk_store_app.repository.DeliveryRepository;
import com.example.milk_store_app.repository.UserRepository;
import com.example.milk_store_app.services.DeliveryServices;
import com.example.milk_store_app.services.OrderServices;
import com.example.milk_store_app.services.UserServices;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StaffAssignActivity extends AppCompatActivity {

    DeliveryServices deliveryServices;
    UserServices userServices;
    private List<UserResponse> deliveryList;
    private DeliveryAdapter adapter;
    private RecyclerView recyclerViewDelivery;
    Button SelectDelivery;
         //id   buttonSelectDelivery
         private UUID orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_staff_assign);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.Assign), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        deliveryServices = DeliveryRepository.getDeliveryServices(this);
        userServices = UserRepository.getUserServices(this);
        recyclerViewDelivery = findViewById(R.id.recyclerViewDelivery);
        deliveryList = new ArrayList<>();
    //    adapter = new DeliveryAdapter(deliveryList, this);
        String orderIdString = getIntent().getStringExtra("orderId");
        try {
            orderId = UUID.fromString(orderIdString);
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, "Invalid Order ID", Toast.LENGTH_SHORT).show();
            Log.e("StaffAssignActivity", "Invalid UUID for Order ID: " + orderIdString, e);
            finish(); // Kết thúc activity nếu Order ID không hợp lệ
            return;
        }
        adapter = new DeliveryAdapter(deliveryList, this,  deliveryStaffId -> {
            // Khi một nhân viên giao hàng được chọn, gọi API tạo đơn hàng cho người giao hàng
            try {
                UUID deliveryUUID = UUID.fromString(deliveryStaffId);
                createOrderToDelivery(orderId, deliveryUUID);
            } catch (IllegalArgumentException e) {
                Toast.makeText(this, "Invalid Delivery Staff ID", Toast.LENGTH_SHORT).show();
                Log.e("StaffAssignActivity", "Invalid UUID for Delivery Staff ID: " + deliveryStaffId, e);
            }        });


        recyclerViewDelivery.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewDelivery.setAdapter(adapter);

       // deliveryServices = DeliveryServices.
        loadDeliveries();

    }
    private void loadDeliveries() {
        userServices.getdelivery().enqueue(new Callback<List<UserResponse>>() {

            @Override
            public void onResponse(Call<List<UserResponse>> call, Response<List<UserResponse>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    deliveryList.clear();
                 deliveryList.addAll(response.body());


                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(StaffAssignActivity.this, "Failed to load deliveries.", Toast.LENGTH_SHORT).show();
                    Log.e("API_ERROR", "Response error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<UserResponse>> call, Throwable t) {

                Toast.makeText(StaffAssignActivity.this, "An error occurred: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", "Failure: " + t.getMessage());
            }
        });
    }


    private void createOrderToDelivery(UUID orderId, UUID deliveryStaffId) {
        CreateOrderRequest request = new CreateOrderRequest(orderId, deliveryStaffId);

        deliveryServices.createOrderToDelivery(request).enqueue(new Callback<DeliveryResponse>() {
            @Override
            public void onResponse(Call<DeliveryResponse> call, Response<DeliveryResponse> response) {
                if (response.isSuccessful()&& response.body() != null) {
                    Toast.makeText(StaffAssignActivity.this, "Order assigned to delivery successfully!", Toast.LENGTH_SHORT).show();
                    //// Quay lại trang trước đó
                    Intent intent = new Intent(StaffAssignActivity.this,StaffViewOrderActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(StaffAssignActivity.this, "Failed to assign order.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DeliveryResponse> call, Throwable t) {
                Toast.makeText(StaffAssignActivity.this, "An error occurred: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}