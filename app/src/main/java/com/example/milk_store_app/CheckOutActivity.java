package com.example.milk_store_app;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.milk_store_app.client.ZalopayLibary;

import org.json.JSONObject;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class CheckOutActivity extends AppCompatActivity {
    Button btnConfirm;
    TextView txtTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_check_out);
        btnConfirm = findViewById(R.id.buttonCheckOut);
        txtTotal = findViewById(R.id.total);

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX);

        Intent intent = getIntent();

       // String soluong = intent.getStringExtra("soluong");
        Double total = intent.getDoubleExtra("total", 0.0);
       // txtSoluong.setText(soluong);
        String totalString = String.format("%.0f",total);
        txtTotal.setText(Double.toString(total));

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                // day chinh la buoc order thanh toan dua no vao nguoi ta cho san
                ZalopayLibary orderApi = new ZalopayLibary();
                try {
                    //khi an thanh toan tong so tien se duoc gui vao trong create order, generate ra token return_code
                    JSONObject data = orderApi.createOrder(totalString);
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

                                Intent intent1 = new Intent(CheckOutActivity.this,HomeViewActivity.class);
                                intent1.putExtra("result",  "sản phẩm! Tổng số tiền:" + total);
                                startActivity(intent1);
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

}