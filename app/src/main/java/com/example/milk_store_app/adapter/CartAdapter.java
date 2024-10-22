package com.example.milk_store_app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.milk_store_app.R;
import com.example.milk_store_app.models.entities.CartItems;
import com.example.milk_store_app.models.response.ProductResponse;

import java.util.ArrayList;

public class CartAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<CartItems> cartList;
    private int layout;
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
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CartItems cartItems = cartList.get(i);
        holder.tvProductName.setText(cartItems.getProductName());
        holder.tvPrice.setText(String.format(context.getResources().getString(R.string.product_price), String.valueOf(cartItems.getProductPrice())));
        holder.txProductAmount.setText(cartItems.getQuantity());
        return convertView;
    }
}
