package com.example.milk_store_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.AlertDialog;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.milk_store_app.adapter.OrderAdapter;
import com.example.milk_store_app.models.response.OrderResponse;
import com.example.milk_store_app.repository.OrderRepository;
import com.example.milk_store_app.services.OrderServices;
import com.example.milk_store_app.session.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerOrder extends Fragment {

    OrderServices orderServices;
    List<OrderResponse> orderList;
    OrderAdapter adapter;
    RecyclerView recyclerOrder;
    SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_order, container, false);
        projectData(view);
        return view;
    }

    private void projectData(View view) {
        recyclerOrder = view.findViewById(R.id.recyclerViewOrders);
        orderServices = OrderRepository.getOrderServices(requireContext());
        orderList = new ArrayList<>();
        adapter = new OrderAdapter(orderList, requireContext(), R.layout.activity_history_order_customer);
        recyclerOrder.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerOrder.setAdapter(adapter);

        sessionManager = new SessionManager(requireContext());

        loadOrders();
    }

    private void loadOrders() {
        orderServices.getOrdersByUserId(
                "de3cbdbd-9142-4c9f-aeae-de1f286197af",
                1,
                10,
                "",
                ""
        ).enqueue(new Callback<List<OrderResponse>>() {
            @Override
            public void onResponse(Call<List<OrderResponse>> call, Response<List<OrderResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    orderList.clear();
                    orderList.addAll(response.body());
                    adapter.notifyItemRangeInserted(0, orderList.size());
                }
            }

            @Override
            public void onFailure(Call<List<OrderResponse>> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Error")
                        .setMessage("Error loading orders")
                        .setPositiveButton("OK", (dialog, which) -> {
                            dialog.dismiss();
                        });
                builder.show();
            }
        });
    }
}
