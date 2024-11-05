package com.example.milk_store_app;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.milk_store_app.models.request.PutOrderRequest;
import com.example.milk_store_app.models.response.OrderDetailResponse;
import com.example.milk_store_app.models.response.OrderItemListResponse;
import com.example.milk_store_app.models.response.OrderResponse;
import com.example.milk_store_app.repository.OrderRepository;
import com.example.milk_store_app.services.OrderServices;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerOrderDetailActivity extends AppCompatActivity {
    TextView orderStatus, orderAddress;
    ListView orderItemList;
    Button btnAction1, btnAction2;
    ImageButton btnGoBack;

    OrderServices orderServices;
    ArrayAdapter<OrderItemListResponse> orderItemsAdapter;

    OrderDetailResponse orderDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_order_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ProjectData();
    }

    private void ProjectData(){
        orderStatus = findViewById(R.id.order_status);
        orderAddress = findViewById(R.id.order_address);
        orderItemList = findViewById(R.id.order_item_list);
        btnAction1 = findViewById(R.id.btn_action1);
        btnAction2 = findViewById(R.id.btn_action2);
        btnGoBack = findViewById(R.id.btn_go_back);

        btnGoBack.setOnClickListener(v -> finish());

        orderServices = OrderRepository.getOrderServices(this);
        orderItemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        orderItemList.setAdapter(orderItemsAdapter);
        orderDetail = new OrderDetailResponse();
        FetchOrderDetail();
    }

    private void FetchOrderDetail() {
        String orderId = getIntent().getStringExtra("orderId");
        orderServices.getOrderById(orderId).enqueue(
                new Callback<OrderDetailResponse>() {
                    @Override
                    public void onResponse(Call<OrderDetailResponse> call, Response<OrderDetailResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            orderDetail = response.body();

                            orderItemsAdapter.addAll(orderDetail.getOrderDetails());
                            orderItemsAdapter.notifyDataSetChanged();

                            HandleStatus(orderDetail.getOrderStatus());
                            orderAddress.setText(orderDetail.getAddress());

                            HandleButton(orderDetail.getOrderStatus());
                        }
                    }

                    @Override
                    public void onFailure(Call<OrderDetailResponse> call, Throwable t) {

                    }
                }
        );
    }

    private void HandleButton(String status) {
        if (!status.equals("Delivered")){
            btnAction2.setVisibility(View.GONE);
            btnAction1.setEnabled(true);

            btnAction1.setText("Mua thêm");
            btnAction1.setOnClickListener(v -> {
                // Handle button click for "Mua thêm"
//                Intent intent = new Intent(CustomerOrderDetailActivity.this, HomeViewActivity.class);
//                startActivity(intent);
                finish();
            });
        } else {
            btnAction1.setEnabled(true);
            btnAction2.setEnabled(true);

            btnAction1.setText("Đã nhận");
            btnAction1.setOnClickListener(v -> HandleConfirmReceive());
            btnAction2.setOnClickListener(v ->
                Toast.makeText(this, "Not implemented yet!", Toast.LENGTH_SHORT).show()
            );
        }
    }

    private void HandleConfirmReceive() {
        PutOrderRequest request = new PutOrderRequest(
                3, // Received
                orderDetail.getOrderCode(),
                orderDetail.getAddress(),
                orderDetail.getPhoneNumber()
        );
        orderServices.updateOrder(orderDetail.getId().toString(), request).enqueue(
                new Callback<OrderResponse>() {
                    @Override
                    public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(CustomerOrderDetailActivity.this, "Đã nhận đơn hàng", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(CustomerOrderDetailActivity.this, CustomerOrderHistoryActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<OrderResponse> call, Throwable t) {

                    }
                }
        );
    }

    private void HandleStatus(String status) {
        Drawable drawable;
        switch (status){
            case "Ordered":
                orderStatus.setText("Đơn hàng đang được xử lý");
                drawable = getResources().getDrawable(R.drawable.rounded_corner_top_bg_gray, null);
                orderStatus.setBackground(drawable);
                break;
            case "Assigned":
                orderStatus.setText("Đơn hàng đã được giao cho shipper");
                drawable = getResources().getDrawable(R.drawable.rounded_corner_top_bg_yellow, null);
                orderStatus.setBackground(drawable);
                break;
            case "Shipping":
                orderStatus.setText("Đơn hàng đang được vận chuyển");
                drawable = getResources().getDrawable(R.drawable.rounded_corner_top_bg_yellow, null);
                orderStatus.setBackground(drawable);
                break;
            case "Delivered":
                orderStatus.setText("Đơn hàng đã được giao");
                drawable = getResources().getDrawable(R.drawable.rounded_corner_top_bg_lg_green, null);
                orderStatus.setBackground(drawable);
                break;
            case "Received":
                orderStatus.setText("Đơn hàng đã hoàn thành");
                drawable = getResources().getDrawable(R.drawable.rounded_corner_top_bg_gray, null); // null for default theme
                orderStatus.setBackground(drawable);
                break;
            default:
                orderStatus.setText("Đơn hàng đã bị hủy");
                drawable = getResources().getDrawable(R.drawable.rounded_corner_top_bg_yellow, null);
                orderStatus.setBackground(drawable);
        }
    }


}