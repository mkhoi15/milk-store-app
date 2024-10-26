package com.example.milk_store_app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.milk_store_app.models.request.OrderUpdateRequest;
import com.example.milk_store_app.models.response.DeliveryResponse;
import com.example.milk_store_app.models.response.OrderResponse;
import com.example.milk_store_app.repository.DeliveryRepository;
import com.example.milk_store_app.repository.OrderRepository;
import com.example.milk_store_app.services.DeliveryServices;
import com.example.milk_store_app.services.OrderServices;
import com.example.milk_store_app.untils.DateTimeHelper;
import com.example.milk_store_app.untils.NumberHelper;

import java.time.LocalDateTime;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDeliveryDetailActivity extends AppCompatActivity {

    TextView tvDeliveryManName, tvCustomerName, tvOrderDate, tvOrderStatus, tvTotalPrice, tvAddress, tvPhoneNumber, tvOrderId;
    DeliveryResponse deliveryResponse;
    OrderServices orderServices;
    DeliveryServices deliveryServices;
    Button btnUpdateOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_delivery_detail);

        // Initialize views
        tvDeliveryManName = findViewById(R.id.tvDeliveryManName);
        tvCustomerName = findViewById(R.id.tvCustomerName);
        tvOrderDate = findViewById(R.id.tvOrderDate);
        tvOrderStatus = findViewById(R.id.tvOrderStatus);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvAddress = findViewById(R.id.tvAddress);
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        tvOrderId = findViewById(R.id.tvOrderId);
        btnUpdateOrder = findViewById(R.id.btnUpdateOrder);
        orderServices = OrderRepository.getOrderServices(this);
        deliveryServices = DeliveryRepository.getDeliveryServices(this);

        // Get the passed data
        deliveryResponse = (DeliveryResponse) getIntent().getSerializableExtra("orderDetails");

        if (deliveryResponse != null) {
            updateUI();
            setupButtonAction();
        }
    }

    private void updateUI() {
        tvOrderId.setText("Order: " + deliveryResponse.getOrderId());
        tvDeliveryManName.setText("Delivery Man: " + deliveryResponse.getDeliveryManName());
        tvCustomerName.setText("Customer: " + deliveryResponse.getOrder().getCustomerName());
        LocalDateTime orderDate = DateTimeHelper.parseStringToLocalDateTime(deliveryResponse.getOrder().getOrderDate());
        tvOrderDate.setText("Order Date: " + DateTimeHelper.formatLocalDateTimeToString(orderDate));
        tvOrderStatus.setText("Status: " + deliveryResponse.getOrder().getOrderStatus());
        tvTotalPrice.setText("Total: " + NumberHelper.formatNumber(deliveryResponse.getOrder().getTotalPrice()));
        tvAddress.setText("Address: " + deliveryResponse.getOrder().getAddress());
        tvPhoneNumber.setText("Phone: " + deliveryResponse.getOrder().getPhoneNumber());


        String status = deliveryResponse.getOrder().getOrderStatus();
        if ("Assigned".equalsIgnoreCase(status)) {
            btnUpdateOrder.setText("Make Delivery Shipping");
        } else if ("Shipping".equalsIgnoreCase(status)) {
            btnUpdateOrder.setText("Delivered Order");
        }

    }


    private void setupButtonAction() {
        btnUpdateOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String status = deliveryResponse.getOrder().getOrderStatus();
                if ("Assigned".equalsIgnoreCase(status)) {
                    updateOrderStatus("Shipping");
                } else if ("Shipping".equalsIgnoreCase(status)) {
                    updateOrderStatus("Delivered");
                }
            }
        });
    }


    private void updateOrderStatus(String status) {
        if (deliveryResponse == null) {
            return;
        }

        int orderStatusCode;
        switch (status) {
            case "Shipping":
                orderStatusCode = 4; // Shipping status code
                break;
            case "Delivered":
                orderStatusCode = 5; // Delivered status code
                break;
            default:
                Toast.makeText(this, "Invalid status", Toast.LENGTH_SHORT).show();
                return;
        }

        // Create request with updated status
        OrderUpdateRequest request = OrderUpdateRequest.builder()
                .orderStatus(orderStatusCode)
                .address(deliveryResponse.getOrder().getAddress()) // Use existing address
                .phoneNumber(deliveryResponse.getOrder().getPhoneNumber()) // Use existing phone number
                .build();

        Call<OrderResponse> call = orderServices.updateOrder(deliveryResponse.getOrderId().toString(), request);
        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.isSuccessful()) {
                    // Update was successful, get updated delivery details
                    fetchUpdatedDeliveryDetails();
                } else {
                    Toast.makeText(OrderDeliveryDetailActivity.this, "Failed to update order status", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                Toast.makeText(OrderDeliveryDetailActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchUpdatedDeliveryDetails() {
        Call<DeliveryResponse> call = deliveryServices.getDeliveryById(deliveryResponse.getId().toString());
        call.enqueue(new Callback<DeliveryResponse>() {
            @Override
            public void onResponse(Call<DeliveryResponse> call, Response<DeliveryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Update the UI with new data
                    deliveryResponse = response.body();
                    updateUI();
                } else {
                    Toast.makeText(OrderDeliveryDetailActivity.this, "Failed to retrieve updated delivery details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DeliveryResponse> call, Throwable t) {
                Toast.makeText(OrderDeliveryDetailActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
