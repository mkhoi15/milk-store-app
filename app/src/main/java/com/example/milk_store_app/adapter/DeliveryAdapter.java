package com.example.milk_store_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.milk_store_app.R;
import com.example.milk_store_app.models.response.DeliveryResponse;
import com.example.milk_store_app.models.response.UserResponse;

import java.util.List;

public class DeliveryAdapter extends RecyclerView.Adapter<DeliveryAdapter.DeliveryViewHolder> {

    private List<UserResponse> deliveryList;
    private Context context;
    private OnDeliverySelectedListener listener;

    public interface OnDeliverySelectedListener {
        void onDeliverySelected(String deliveryStaffId); // Interface truyền deliveryStaffId
    }
    public DeliveryAdapter(List<UserResponse> deliveryList, Context context, OnDeliverySelectedListener listener) {
        this.deliveryList = deliveryList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DeliveryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_staff_delivery, parent, false);
        return new DeliveryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeliveryViewHolder holder, int position) {
        UserResponse delivery = deliveryList.get(position);
        holder.textViewDeliveryName.setText(delivery.getFullName()); // Cập nhật tên người giao hàng
        holder.buttonSelectDelivery.setOnClickListener(v -> {
            // Xử lý sự kiện chọn người giao hàng ở đây
            // Ví dụ: Lưu hoặc trả về lựa chọn
       //     Lisn.onDeliverySelected(delivery.getId());
         //   Toast.makeText(context, "Selected: " + delivery.getName(), Toast.LENGTH_SHORT)
            listener.onDeliverySelected(String.valueOf(delivery.getId()));
            Toast.makeText(context, "Selected: " + delivery.getFullName(), Toast.LENGTH_SHORT).show();

        });
    }

    @Override
    public int getItemCount() {
        return deliveryList.size();
    }

    public static class DeliveryViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDeliveryName;
        Button buttonSelectDelivery;

        public DeliveryViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDeliveryName = itemView.findViewById(R.id.textViewDeliveryName);
            buttonSelectDelivery = itemView.findViewById(R.id.buttonSelectDelivery);
        }
    }
}