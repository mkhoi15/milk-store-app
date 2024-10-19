package com.example.milk_store_app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
            holder.tvPrice = convertView.findViewById(R.id.product_price);
            holder.tvDescription = convertView.findViewById(R.id.product_description);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ProductResponse product = productList.get(position);
        if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(product.getImageUrl())
                    .into(holder.imgProduct);
        } else {
            holder.imgProduct.setImageResource(R.drawable.ic_launcher_background);
        }
        holder.tvProductName.setText(product.getName());

        String stockFormatted = String.format(context.getResources().getString(R.string.product_stock), product.getStock());
        holder.tvStock.setText(stockFormatted);

        String priceFormatted = String.format(context.getResources().getString(R.string.product_price), product.getPrice().toString());
        holder.tvPrice.setText(priceFormatted);

        holder.tvDescription.setText(product.getDescription());
        return convertView;
    }
}
