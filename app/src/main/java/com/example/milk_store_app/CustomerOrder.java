package com.example.milk_store_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        View view = inflater.inflate(R.layout.fragment_customer_order, container, false);
        projectData(view);

        // Define filter TextViews
        TextView statusOrdered = view.findViewById(R.id.filterOrdered);
        TextView statusAssigned = view.findViewById(R.id.filterAssigned);
        TextView statusReceived = view.findViewById(R.id.filterReceived);
        TextView statusShipping = view.findViewById(R.id.filterShipping);
        TextView statusDelivered = view.findViewById(R.id.filterDelivered);

        // Set up click listeners for status filters
        statusOrdered.setOnClickListener(v -> filterOrders("Ordered", statusOrdered));
        statusAssigned.setOnClickListener(v -> filterOrders("Assigned", statusAssigned));
        statusReceived.setOnClickListener(v -> filterOrders("Received", statusReceived));
        statusShipping.setOnClickListener(v -> filterOrders("Shipping", statusShipping));
        statusDelivered.setOnClickListener(v -> filterOrders("Delivered", statusDelivered));

        // Set default filter to "Ordered" on first login
        filterOrders("Ordered", statusOrdered);

        return view;
    }

    private void projectData(View view) {
        recyclerOrder = view.findViewById(R.id.recyclerViewOrders);
        orderServices = OrderRepository.getOrderServices(requireContext());
        orderList = new ArrayList<>();
        adapter = new OrderAdapter(orderList, requireContext(), R.layout.customer_order_item);
        recyclerOrder.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerOrder.setAdapter(adapter);

        sessionManager = new SessionManager(requireContext());

        // Load orders with an empty status initially to load all orders
        loadOrders("");
    }

    private void filterOrders(String status, TextView selectedStatusView) {
        loadOrders(status);

        // Reset all background colors and set the selected one to highlight
        resetFilterBackgrounds();
        selectedStatusView.setSelected(true);
    }

    private void resetFilterBackgrounds() {
        View view = getView();
        if (view != null) {
            view.findViewById(R.id.filterOrdered).setSelected(false);
            view.findViewById(R.id.filterAssigned).setSelected(false);
            view.findViewById(R.id.filterReceived).setSelected(false);
            view.findViewById(R.id.filterShipping).setSelected(false);
            view.findViewById(R.id.filterDelivered).setSelected(false);
        }
    }

    private void loadOrders(String orderStatus) {
        String userId = sessionManager.fetchUserId();
        orderServices.getOrdersByUserId("de3cbdbd-9142-4c9f-aeae-de1f286197af", 1, 10, orderStatus, "orderStatus")
                .enqueue(new Callback<List<OrderResponse>>() {
                    @Override
                    public void onResponse(Call<List<OrderResponse>> call, Response<List<OrderResponse>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            orderList.clear();
                            orderList.addAll(response.body());
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<OrderResponse>> call, Throwable t) {
                        new AlertDialog.Builder(requireContext())
                                .setTitle("Error")
                                .setMessage("Error loading orders")
                                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                                .show();
                    }
                });
    }
}