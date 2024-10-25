package com.example.milk_store_app;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.milk_store_app.models.response.DeliveryResponse;

public class OrderDeliveryDetailActivity extends AppCompatActivity {

    TextView tvDeliveryManName, tvCustomerName, tvOrderDate, tvOrderStatus, tvTotalPrice, tvAddress, tvPhoneNumber, tvOrderId;
    DeliveryResponse deliveryResponse;

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

        // Get the passed data
        deliveryResponse = (DeliveryResponse) getIntent().getSerializableExtra("orderDetails");

        if (deliveryResponse != null) {
            updateUI();
        }
    }

    private void updateUI() {
        tvOrderId.setText("Order: " + deliveryResponse.getOrderId());
        tvDeliveryManName.setText("Delivery Man: " + deliveryResponse.getDeliveryStaffName());
        tvCustomerName.setText("Customer: " + deliveryResponse.getOrder().getCustomerName());
        tvOrderDate.setText("Order Date: " + deliveryResponse.getOrder().getOrderDate());
        tvOrderStatus.setText("Status: " + deliveryResponse.getOrder().getOrderStatus());
        tvTotalPrice.setText("Total: " + deliveryResponse.getOrder().getTotalPrice());
        tvAddress.setText("Address: " + deliveryResponse.getOrder().getAddress());
        tvPhoneNumber.setText("Phone: " + deliveryResponse.getOrder().getPhoneNumber());
    }
}
