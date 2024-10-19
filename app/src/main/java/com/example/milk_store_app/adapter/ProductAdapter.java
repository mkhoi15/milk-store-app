package com.example.milk_store_app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.milk_store_app.R;
import com.example.milk_store_app.models.response.ProductResponse;

import java.util.ArrayList;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ProductAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ProductResponse> productList;
    private int layout;

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private static class ViewHolder {
        ImageView imgProduct;
        TextView tvProductName, tvStock, tvPrice, tvDescription;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, layout, null);
            holder = new ViewHolder();
            holder.imgProduct = convertView.findViewById(R.id.imgProductImg);
            holder.tvProductName = convertView.findViewById(R.id.product_name);
            holder.tvStock = convertView.findViewById(R.id.product_stock);
//            holder.tvPrice = convertView.findViewById(R.id.product_price);
            holder.tvDescription = convertView.findViewById(R.id.product_description);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ProductResponse product = productList.get(position);
        holder.imgProduct.setImageResource(R.drawable.ic_launcher_background);
        holder.tvProductName.setText(product.getName());
        holder.tvStock.setText(String.valueOf(product.getStock()));
//        holder.tvPrice.setText(String.valueOf(product.getPrice()));
        holder.tvDescription.setText(product.getDescription());
        return convertView;
    }
}
