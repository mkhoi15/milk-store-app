package com.example.milk_store_app;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.milk_store_app.adapter.OrderAdapter;
import com.example.milk_store_app.adapter.OrderStaffAdapter;
import com.example.milk_store_app.models.response.OrderResponse;
import com.example.milk_store_app.repository.OrderRepository;
import com.example.milk_store_app.services.OrderServices;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StaffViewOrderActivity extends AppCompatActivity {

    OrderServices orderServices;
    List<OrderResponse> orderList;
    OrderStaffAdapter adapter;
    RecyclerView recyclerOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_staff_view_order);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        projectData();
    }

    private void projectData() {
        recyclerOrder = findViewById(R.id.recyclerViewStaffOrders);
        orderServices = OrderRepository.getOrderServices(this);
        orderList = new ArrayList<>();
        adapter = new OrderStaffAdapter(orderList, this, R.layout.activity_staff_view_order_item);
        recyclerOrder.setLayoutManager(new LinearLayoutManager(this));
        recyclerOrder.setAdapter(adapter);

        loadOrders();
    }

    private void loadOrders() {
        orderServices.getOrders(
                1,
                10,
                "",
                ""
        ).enqueue(new Callback<List<OrderResponse>>() {
            @Override
            public void onResponse(Call<List<OrderResponse>> call, Response<List<OrderResponse>> response) {
                if (response.isSuccessful()) {
                    orderList.clear();
                    orderList.addAll(response.body());
                    adapter.notifyItemRangeInserted(0, orderList.size());
                }
            }

            @Override
            public void onFailure(Call<List<OrderResponse>> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(StaffViewOrderActivity.this);
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