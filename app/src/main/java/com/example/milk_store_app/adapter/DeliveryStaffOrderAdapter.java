package com.example.milk_store_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.milk_store_app.OrderDeliveryDetailActivity;
import com.example.milk_store_app.R;
import com.example.milk_store_app.models.response.DeliveryResponse;
import com.example.milk_store_app.models.response.OrderDeliveryResponse;
import com.example.milk_store_app.untils.DateTimeHelper;
import com.example.milk_store_app.untils.NumberHelper;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DeliveryStaffOrderAdapter extends RecyclerView.Adapter<DeliveryStaffOrderAdapter.DeliveryStaffOrderViewHolder> {
    private List<DeliveryResponse> deliveryOrderList; // Accept DeliveryResponse list
    private Context context;
    private int layout;

    @NonNull
    @Override
    public DeliveryStaffOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(layout, parent, false);
        return new DeliveryStaffOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeliveryStaffOrderViewHolder holder, int position) {
        DeliveryResponse deliveryResponse = deliveryOrderList.get(position);
        OrderDeliveryResponse order = deliveryResponse.getOrder();

        // Update views with the response data
        holder.tvOrderNumber.setText("Order: " + deliveryResponse.getOrderId().toString());
        holder.tvCustomerName.setText("Customer: " + order.getCustomerName());

        LocalDateTime orderDate = DateTimeHelper.parseStringToLocalDateTime(order.getOrderDate());
        String formattedDate = DateTimeHelper.formatLocalDateTimeToString(orderDate);
        holder.tvOrderDate.setText("Order Date: " + formattedDate);

        holder.tvOrderAddress.setText("Address: " + order.getAddress());
        holder.tvOrderTotal.setText("Total: " + NumberHelper.formatNumber(order.getTotalPrice()));

        // Set onClickListener to open the details activity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, OrderDeliveryDetailActivity.class);
            intent.putExtra("orderDetails", deliveryResponse); // Pass the DeliveryResponse to the next activity
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return deliveryOrderList.size();
    }

    public static class DeliveryStaffOrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderNumber, tvCustomerName, tvOrderDate, tvOrderAddress, tvOrderTotal;

        public DeliveryStaffOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderNumber = itemView.findViewById(R.id.textDeliveryOrderNumber);
            tvCustomerName = itemView.findViewById(R.id.textDeliveryCustomerName);
            tvOrderDate = itemView.findViewById(R.id.textDeliveryOrderDate);
            tvOrderAddress = itemView.findViewById(R.id.textDeliveryOrderAddress);
            tvOrderTotal = itemView.findViewById(R.id.textDeliveryOrderTotal);
        }
    }

}



