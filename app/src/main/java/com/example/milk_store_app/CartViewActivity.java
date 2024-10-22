package com.example.milk_store_app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.milk_store_app.adapter.CartAdapter;
import com.example.milk_store_app.models.entities.CartItems;
import com.example.milk_store_app.models.entities.OrderItem;
import com.example.milk_store_app.models.request.PostOrderRequest;
import com.example.milk_store_app.repository.OrderRepository;
import com.example.milk_store_app.services.OrderServices;
import com.example.milk_store_app.session.CartManager;
import com.example.milk_store_app.session.SessionManager;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CartViewActivity extends AppCompatActivity {
    ArrayList<CartItems> cartList;
    CartAdapter adapter;
    ListView listView;
    Button btnGoBack, btnPay;
    CartManager cartManager;
    SessionManager sessionManager;
    OrderServices orderServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ProjectData();

        btnGoBack.setOnClickListener(v -> {
            finish();
        });

        btnPay.setOnClickListener(v -> {
            // TODO: Implement payment logic here
            HandlePayment();
        });


    }

    private void HandlePayment() {
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItems cartItem : cartList) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(cartItem.getProductId());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItems.add(orderItem);
            }
        orderServices.createOrder(
                //Create a request object
                PostOrderRequest.builder()
                        .userId(sessionManager.fetchUserId())
                        .orderCode("")
                        .totalPrice(cartManager.getTotalPrice())
                        .address("FPTU")
                        .phoneNumber("0394456764")
                        .orderDetails(orderItems)
                        .build()
                )
                .enqueue(new Callback<PostOrderRequest>() {
            @Override
            public void onResponse(Call<PostOrderRequest> call, Response<PostOrderRequest> response) {
                // TODO: Handle the response better, this is just a placeholder
                if (response.isSuccessful()) {
                    cartManager.clearCart();
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<PostOrderRequest> call, Throwable t) {
                Toast.makeText(CartViewActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ProjectData() {
        cartManager = new CartManager(this);
        sessionManager = new SessionManager(this);
        orderServices = OrderRepository.getOrderServices(this);
        cartList = new ArrayList<>();
        cartList.addAll(cartManager.getCart());
        adapter = new CartAdapter(this, cartList, R.layout.cart_item_list, cartManager);

        listView = (ListView) findViewById(R.id.cart_list);
        btnGoBack = (Button) findViewById(R.id.btn_go_back);
        btnPay = (Button) findViewById(R.id.btn_pay);
        listView.setAdapter(adapter);
    }
}