package com.example.milk_store_app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.milk_store_app.adapter.ProductAdapter;
import com.example.milk_store_app.models.response.ProductResponse;
import com.example.milk_store_app.repository.ProductRepository;
import com.example.milk_store_app.services.ProductServices;
import com.example.milk_store_app.session.CartManager;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {
    ProductServices productServices;
    TextView productName, productPrice, productDescription;
    TextView productStock;
    ImageView productImage;
    Button btnAddToCart, btnGoBack;
    private String productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        projectData();
        addListeners();
    }

    private void projectData() {
        productServices = ProductRepository.getProductServices(this);
        productName = findViewById(R.id.product_name);
        productPrice = findViewById(R.id.product_price);
        productDescription = findViewById(R.id.product_description);
        productStock = findViewById(R.id.product_stock);
        productImage = findViewById(R.id.product_image);

        btnAddToCart = findViewById(R.id.btn_add_to_cart);
        btnGoBack = findViewById(R.id.btn_go_back);

        String productId = getIntent().getStringExtra("productId");
        if (productId != null) {
            this.productId = productId;
            loadProduct(productId);
        }
    }

    private void addListeners() {
        btnAddToCart.setOnClickListener(v -> {

        });

        btnGoBack.setOnClickListener(v -> {
            finish();
        });
    }

    private void loadProduct(String productId) {
        productServices.getProductById(UUID.fromString(productId)).enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful()) {
                    ProductResponse product = response.body();
                    if (product == null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetailActivity.this);
                        builder.setMessage("Product not found");
                        builder.setPositiveButton("OK", (dialog, which) -> {
                            dialog.dismiss();
                            finish();
                        });
                        builder.show();
                        return;
                    }
                    if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
                        Glide.with(ProductDetailActivity.this)
                                .load(product.getImageUrl())
                                .into(productImage);
                    } else {
                        productImage.setImageResource(R.drawable.ic_launcher_background);
                    }
                    productName.setText(product.getName());
                    String priceFormatted = String.format(getString(R.string.product_price), product.getPrice());
                    productPrice.setText(priceFormatted);
                    String stockFormatted = String.format(getString(R.string.product_stock), product.getStock());
                    productStock.setText(stockFormatted);
                    productDescription.setText(product.getDescription());
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetailActivity.this);
                builder.setMessage("Failed to load product");
                builder.setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                });
                builder.show();
            }
        });
    }
}