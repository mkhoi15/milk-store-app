package com.example.milk_store_app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.milk_store_app.adapter.ProductAdapter;
import com.example.milk_store_app.models.response.ProductResponse;
import com.example.milk_store_app.repository.ProductRepository;
import com.example.milk_store_app.services.ProductServices;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewActivity extends AppCompatActivity {
    ArrayList<ProductResponse> productsList;
    ProductServices productServices;
    ProductAdapter adapter;
    ListView listView;
    EditText search;
    Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        projectData();

        btnSearch.setOnClickListener(v -> {
            loadProducts(search.getText().toString());
        });
    }

    private void projectData() {
        productsList = new ArrayList<>();
        adapter = new ProductAdapter(this, productsList, R.layout.product_item_list);
        listView = (ListView) findViewById(R.id.product_list);
        search = (EditText) findViewById(R.id.search_bar);
        btnSearch = (Button) findViewById(R.id.btn_search);
        listView.setAdapter(adapter);
        productServices = ProductRepository.getProductServices(this);
        loadProducts("");
    }

    private void loadProducts(String search) {
        productServices.getProducts(
                1,
                10,
                search,
                ""
        ).enqueue(new Callback<List<ProductResponse>>() {
            @Override
            public void onResponse(Call<List<ProductResponse>> call, Response<List<ProductResponse>> response) {
                if (response.isSuccessful()) {
                    productsList.clear();
                    productsList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<ProductResponse>> call, Throwable t) {
                Toast.makeText(HomeViewActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}