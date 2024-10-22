package com.example.milk_store_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.milk_store_app.R;
import com.example.milk_store_app.models.response.OrderResponse;
import com.example.milk_store_app.untils.DateTimeHelper;
import com.example.milk_store_app.untils.NumberHelper;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OrderStaffAdapter extends RecyclerView.Adapter<OrderStaffAdapter.OrderStaffViewHolder> {
    private List<OrderResponse> orderList;
    private Context context;
    private int layout;

    @NonNull
    @Override
    public OrderStaffAdapter.OrderStaffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(layout, parent, false);
        return new OrderStaffAdapter.OrderStaffViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderStaffAdapter.OrderStaffViewHolder holder, int position) {
        OrderResponse order = orderList.get(position);
        holder.tvOrderId.setText("Order: " + order.getOrderCode());
        LocalDateTime orderDate = DateTimeHelper.parseStringToLocalDateTime(order.getOrderDate());
        String readableDate = DateTimeHelper.formatLocalDateTimeToString(orderDate);
        holder.tvOrderDate.setText("Order Date: " + readableDate);
        holder.tvOrderTotal.setText("Order Total: " + NumberHelper.formatNumber(order.getTotalPrice()));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderStaffViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvOrderDate, tvOrderTotal;
        Button btnUpdateStatus;

        public OrderStaffViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.textStaffOrderNumber);
            tvOrderDate = itemView.findViewById(R.id.textStaffOrderDate);
            tvOrderTotal = itemView.findViewById(R.id.textStaffOrderTotal);

            btnUpdateStatus = itemView.findViewById(R.id.buttonUpdateStatus);
        }
    }
}
