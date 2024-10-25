package com.example.milk_store_app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.milk_store_app.adapter.ProductAdapter;
import com.example.milk_store_app.models.response.ProductResponse;
import com.example.milk_store_app.repository.ProductRepository;
import com.example.milk_store_app.services.ProductServices;
import com.example.milk_store_app.session.CartManager;
import com.example.milk_store_app.session.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerHomeFragment extends Fragment {

    ArrayList<ProductResponse> productsList;
    ProductServices productServices;
    ProductAdapter adapter;
    ListView listView;
    EditText search;
    Button btnSearch, btnCart, btnOrderHistory;
    CartManager cartManager;
    SessionManager sessionManager;

    private static final String CHANNEL_ID = "cart_notification_channel";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_home, container, false);

        productsList = new ArrayList<>();
        adapter = new ProductAdapter(getContext(), productsList, R.layout.product_item_list);
        listView = view.findViewById(R.id.product_list);
        search = view.findViewById(R.id.search_bar);
        btnSearch = view.findViewById(R.id.btn_search);
        btnCart = view.findViewById(R.id.btn_cart);
        listView.setAdapter(adapter);

        ImageSlider imageSlider = view.findViewById(R.id.image_slider);
        ArrayList<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.th_truemilk_banner, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.vinamilk_banner, ScaleTypes.FIT));

        imageSlider.setImageList(slideModels, ScaleTypes.FIT);

        productServices = ProductRepository.getProductServices(getContext());
        cartManager = new CartManager(getContext());
        sessionManager = new SessionManager(getContext());

        if (sessionManager.isAdmin()) {
            btnCart.setVisibility(View.GONE);
            btnOrderHistory.setVisibility(View.GONE);
        }

        loadProducts("");

        createNotificationChannel();
        showCartNotification();

        btnSearch.setOnClickListener(v -> loadProducts(search.getText().toString()));

        btnCart.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CartViewActivity.class);
            startActivity(intent);
        });

        listView.setOnItemClickListener((parent, view1, position, id) -> {
            ProductResponse product = productsList.get(position);
            Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
            intent.putExtra("productId", product.getId().toString());
            startActivity(intent);
        });

        return view;
    }

    private void loadProducts(String search) {
        productServices.getProducts(1, 10, search, "").enqueue(new Callback<List<ProductResponse>>() {
            @Override
            public void onResponse(Call<List<ProductResponse>> call, Response<List<ProductResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productsList.clear();
                    productsList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<ProductResponse>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Cart Notification";
            String description = "Notifications for cart items";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = requireContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showCartNotification() {
        if (cartManager.getCart().isEmpty()) {
            return;
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Cart Reminder")
                .setContentText("You have items in your cart. Please checkout.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(requireContext());
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify(1, builder.build());
        }
    }
}
