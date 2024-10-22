package com.example.milk_store_app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.milk_store_app.R;
import com.example.milk_store_app.models.entities.CartItems;
import com.example.milk_store_app.session.CartManager;

import java.util.ArrayList;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CartAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<CartItems> cartList;
    private int layout;
    private CartManager cartManager;

    @Override
    public int getCount() {
        return cartList.size();
    }

    @Override
    public Object getItem(int i) {
        return cartList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private static class ViewHolder {
        TextView tvProductName, tvPrice, txProductAmount;
        Button btnIncrease, btnDecrease, btnDelete;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, layout, null);
            holder = new ViewHolder();
            holder.tvProductName = convertView.findViewById(R.id.product_name);
            holder.tvPrice = convertView.findViewById(R.id.product_price);
            holder.txProductAmount = convertView.findViewById(R.id.product_quantity);
            holder.btnIncrease = convertView.findViewById(R.id.increase_quantity);
            holder.btnDecrease = convertView.findViewById(R.id.decrease_quantity);
            holder.btnDelete = convertView.findViewById(R.id.delete_button);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CartItems cartItem = cartList.get(i);
        holder.tvProductName.setText(cartItem.getProductName());
        holder.tvPrice.setText(String.format(
                context.getResources().getString(R.string.product_price),
                String.valueOf(cartItem.getProductPrice())));
        holder.txProductAmount.setText(String.valueOf(cartItem.getQuantity()));

        holder.btnIncrease.setOnClickListener(v -> {
            int quantity = Integer.parseInt(holder.txProductAmount.getText().toString());
            quantity++;
            cartManager.setItemQuantity(cartItem.getProductId(), quantity);
            holder.txProductAmount.setText(String.valueOf(quantity));
        });

        holder.btnDecrease.setOnClickListener(v -> {
            int quantity = Integer.parseInt(holder.txProductAmount.getText().toString());
            if (quantity > 0) {
                quantity--;
                cartManager.setItemQuantity(cartItem.getProductId(), quantity);
                holder.txProductAmount.setText(String.valueOf(quantity));
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            cartManager.removeItemFromCart(cartItem.getProductId());
            notifyDataSetChanged();
        });

        return convertView;
    }
}
