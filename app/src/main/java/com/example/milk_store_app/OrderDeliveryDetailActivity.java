package com.example.milk_store_app;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.milk_store_app.models.response.DeliveryResponse;
import com.example.milk_store_app.untils.DateTimeHelper;
import com.example.milk_store_app.untils.NumberHelper;

import java.time.LocalDateTime;

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
        tvDeliveryManName.setText("Delivery Man: " + deliveryResponse.getDeliveryManName());
        tvCustomerName.setText("Customer: " + deliveryResponse.getOrder().getCustomerName());
        LocalDateTime orderDate = DateTimeHelper.parseStringToLocalDateTime(deliveryResponse.getOrder().getOrderDate());
        tvOrderDate.setText("Order Date: " + DateTimeHelper.formatLocalDateTimeToString(orderDate));
        tvOrderStatus.setText("Status: " + deliveryResponse.getOrder().getOrderStatus());
        tvTotalPrice.setText("Total: " + NumberHelper.formatNumber(deliveryResponse.getOrder().getTotalPrice()));
        tvAddress.setText("Address: " + deliveryResponse.getOrder().getAddress());
        tvPhoneNumber.setText("Phone: " + deliveryResponse.getOrder().getPhoneNumber());
    }
}
