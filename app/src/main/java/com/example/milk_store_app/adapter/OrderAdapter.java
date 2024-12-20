package com.example.milk_store_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.milk_store_app.CustomerOrderDetailActivity;
import com.example.milk_store_app.R;
import com.example.milk_store_app.models.response.OrderResponse;
import com.example.milk_store_app.untils.DateTimeHelper;
import com.example.milk_store_app.untils.NumberHelper;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<OrderResponse> orderList;
    private Context context;
    private int layout;

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(layout, parent, false);
        return new OrderViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderResponse order = orderList.get(position);
        holder.tvOrderId.setText("Order: " + order.getId());
        LocalDateTime orderDate = DateTimeHelper.parseStringToLocalDateTime(order.getOrderDate());
        String readableDate = DateTimeHelper.formatLocalDateTimeToString(orderDate);
        holder.tvOrderDate.setText("Order Date: " + readableDate);
        holder.tvOrderTotal.setText("Order Total: " + NumberHelper.formatNumber(order.getTotalPrice()));
        holder.tvOrderStatus.setText("Order Status: " + order.getOrderStatus());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CustomerOrderDetailActivity.class);
            intent.putExtra("orderId", order.getId().toString());
            context.startActivity(intent);
        });


    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvOrderDate, tvOrderTotal, tvOrderStatus;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.OrderId);
            tvOrderDate = itemView.findViewById(R.id.textOrderDate);
            tvOrderTotal = itemView.findViewById(R.id.textOrderTotal);
            tvOrderStatus = itemView.findViewById(R.id.textOrderStatus);
        }
    }
}
