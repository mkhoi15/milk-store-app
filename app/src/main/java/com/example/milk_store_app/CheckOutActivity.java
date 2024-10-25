package com.example.milk_store_app;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.milk_store_app.client.ApiClient;
import com.example.milk_store_app.client.ZalopayLibary;
import com.example.milk_store_app.database.CartDatabase;
import com.example.milk_store_app.models.entities.CartItems;
import com.example.milk_store_app.models.entities.OrderItem;
import com.example.milk_store_app.models.request.PostOrderRequest;
import com.example.milk_store_app.models.response.OrderResponse;
import com.example.milk_store_app.models.response.UserReponse;
import com.example.milk_store_app.services.OrderServices;
import com.example.milk_store_app.services.ProductServices;
import com.example.milk_store_app.services.UserService;
import com.example.milk_store_app.session.CartManager;
import com.example.milk_store_app.session.SessionManager;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class CheckOutActivity extends AppCompatActivity {
    Button btnConfirm;
    TextView txtTotal;
    private SessionManager sessionManager;
    private ApiClient apiClient;
    private String userId;
    private Double total;
    private List<OrderItem> orderItems; // Store order items
    private UserReponse currentUser;
    // Khai báo các TextView
    TextView addressTextView;
    TextView phoneTextView;
    TextView nameTextView;
    private CartManager cartManager; // Initialize CartManager

    /*    private string userId;
    private TextView userRoleTextView;*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out); // Chuyển lên trên

        //get Textview
        addressTextView = findViewById(R.id.customer_address);
        phoneTextView = findViewById(R.id.customer_phone);
        nameTextView = findViewById(R.id.customer_name);
        btnConfirm = findViewById(R.id.buttonCheckOut);

// lay data user
        loadUserData();



        EdgeToEdge.enable(this);
        cartManager = new CartManager(this); // Ensure your CartManager constructor accepts a context
        total = cartManager.getTotalPrice(); // Assuming this method calculates the total price from the cart
        Log.d("CheckoutProcess", "Total price from cart: " + total); // In ra giá trị total

        txtTotal = findViewById(R.id.total);
        Log.d("CheckoutProcess", "Total price from cart: " + total);
        txtTotal.setText(Double.toString(total));
        Log.d("CheckoutProcess", "Total price from cart: " + total);
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Log.d("CheckoutProcess", "Total price from cart: " + total);
        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX);
        Log.d("CheckoutProcess", "Total price from cart: " + total);
        Intent intent = getIntent();
        Log.d("CheckoutProcess", "Total price from cart: " + total);
        intent.putExtra("total", total);

       // String soluong = intent.getStringExtra("soluong");
         total = intent.getDoubleExtra("total", 0.0);

        // txtSoluong.setText(soluong);
        String totalString = String.format("%.0f",total);
        Log.d("CheckoutProcess", "Total price from cart: " + total);
     //   txtTotal.setText(Double.toString(total));
    //    txtTotal.setText(totalString);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                Log.d("CheckoutProcess", "Value of total before creating order: " + total); // Log giá trị của total

                // day chinh la buoc order thanh toan dua no vao nguoi ta cho san
                ZalopayLibary orderApi = new ZalopayLibary();
                try {
                    //khi an thanh toan tong so tien se duoc gui vao trong create order, generate ra token return_code
                    Log.d("CheckoutProcess", "Attempting to create order with total: " + totalString);

                    JSONObject data = orderApi.createOrder(totalString);
                    Log.d("CheckoutProcess", "Order created successfully.");

                    String code = data.getString("return_code");
                    //neu return_code trong data la 1 => co the thanh toan duoc
                    if (code.equals("1")) {
                        //lay zp_trans_token trong data ra de thanh toan
                        String token = data.getString("zp_trans_token");
                        //mo nguyen cai zalopaySDK ra de thanh toan o trang web ://app co 3 loai: success, cancel error
                        ZaloPaySDK.getInstance().payOrder(CheckOutActivity.this, token, "demozpdk://app", new PayOrderListener() {
                            @Override
                            public void onPaymentSucceeded(String s, String s1, String s2) {
                                Log.d("PaymentStatus", "Payment succeeded. Transaction ID: " + s);
                                createOrder(s);
                                navigateToHome("Order placed successfully! Total: " + total);

                            }

                            @Override
                            public void onPaymentCanceled(String s, String s1) {
                                Intent intent1 = new Intent(CheckOutActivity.this,HomeViewActivity.class);
                                intent1.putExtra("result","hủy thanh toán thành công");
                                startActivity(intent1);
                            }

                            @Override
                            public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {
                                Intent intent1 = new Intent(CheckOutActivity.this,HomeViewActivity.class);
                                intent1.putExtra("result","lỗi, thanh toán thất bại");
                                startActivity(intent1);
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.checkoutlayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }

    private void createOrder(String transactionId) {
       // CartManager = new CartManager(this); // Ensure your CartManager constructor accepts a context
        total = cartManager.getTotalPrice(); // Assuming this method calculates the total price from the cart

        // Create a unique order code
        String orderCode = "ORDER-" + UUID.randomUUID().toString().substring(0, 8);

        initializeOrderItems();
        //  private String userId;
        //    private String orderCode;
        //    private double totalPrice;
        //    private String address;
        //    private String phoneNumber;
        //    private List<OrderItem> orderDetails;
        PostOrderRequest orderRequest = new PostOrderRequest(
                userId,
                orderCode,
                total,
                addressTextView.getText().toString(),
                phoneTextView.getText().toString(),
                orderItems
        );

        OrderServices orderServices = apiClient.getOrderServices(this);
        Call<OrderResponse> call = orderServices.createOrdertest(orderRequest);


        call.enqueue(new Callback<OrderResponse>() {  // Changed from OrderResponse to Void

           @Override
           public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                   if (response.code() == 201) {
                       // Trường hợp mã phản hồi là 204
                       cartManager.clearCart();
                       navigateToHome("Order " + orderCode + " placed successfully!  total: " + total);
                   }
               }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                // Log the network failure
                Log.e(TAG, "Network error during order creation", t);

                // Show error to user
                runOnUiThread(() -> {
                    Toast.makeText(CheckOutActivity.this,
                            "Network error: " + t.getMessage(),
                            Toast.LENGTH_LONG).show();
                });

                navigateToHome("Payment successful but order creation failed (Network error)");
            }
        });
    }
    private void loadUserData() {
        sessionManager = new SessionManager(this);
        userId = sessionManager.fetchUserId();
        apiClient = new ApiClient();

        UserService userService = apiClient.getUserServices(this);
        Call<UserReponse> call = userService.getUserById(userId);

        call.enqueue(new Callback<UserReponse>() {
            @Override
            public void onResponse(Call<UserReponse> call, Response<UserReponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentUser = response.body();
                    updateUserInterface(currentUser);
                } else {
                    Toast.makeText(CheckOutActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserReponse> call, Throwable t) {
                Toast.makeText(CheckOutActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserInterface(UserReponse userResponse) {
//        addressTextView.setText(userResponse.getAddress());
        //addressTextView.setText("áda");
        addressTextView.setText("FPT U");

        phoneTextView.setText(userResponse.getPhoneNumber());
        nameTextView.setText(userResponse.getFullName());

    }
    private void navigateToHome(String message) {
        Intent intent = new Intent(CheckOutActivity.this, HomeViewActivity.class);
        intent.putExtra("result", message);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void initializeOrderItems() {
        orderItems = new ArrayList<>();
        List<CartItems> cartItems = cartManager.getCart();
        for (CartItems cartItem : cartItems) {
            OrderItem orderItem = new OrderItem(cartItem.getProductId(), cartItem.getQuantity(),cartItem.getProductPrice());
            orderItems.add(orderItem);
        }
    }

}