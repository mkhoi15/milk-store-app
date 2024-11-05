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
import com.example.milk_store_app.untils.NumberHelper;

import java.util.ArrayList;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ProductAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ProductResponse> productList;
    private int layout;

    @Override
    public int getCount() {
        return (productList.size() + 1) / 2; // Two products per row
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position * 2); // Return the first item in the row pair
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private static class ViewHolder {
        // Left product views
        ImageView imgProductLeft;
        TextView tvProductNameLeft, tvStockLeft, tvPriceLeft, tvDescriptionLeft;

        // Right product views
        ImageView imgProductRight;
        TextView tvProductNameRight, tvStockRight, tvPriceRight, tvDescriptionRight;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, layout, null);
            holder = new ViewHolder();

            // Left product views
            holder.imgProductLeft = convertView.findViewById(R.id.imgProductImgLeft);
            holder.tvProductNameLeft = convertView.findViewById(R.id.product_name_left);
            holder.tvStockLeft = convertView.findViewById(R.id.product_stock_left);
            holder.tvPriceLeft = convertView.findViewById(R.id.product_price_left);

            // Right product views
//            holder.imgProductRight = convertView.findViewById(R.id.imgProductImgRight);
//            holder.tvProductNameRight = convertView.findViewById(R.id.product_name_right);
//            holder.tvStockRight = convertView.findViewById(R.id.product_stock_right);
//            holder.tvPriceRight = convertView.findViewById(R.id.product_price_right);
//            holder.tvDescriptionRight = convertView.findViewById(R.id.product_description_right);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Bind the left product in the row
        ProductResponse leftProduct = productList.get(position);
        bindProduct(holder.imgProductLeft, holder.tvProductNameLeft, holder.tvStockLeft,
                holder.tvPriceLeft, leftProduct);

        // Bind the right product if it exists
//        if (position * 2 + 1 < productList.size()) {
//            ProductResponse rightProduct = productList.get(position * 2 + 1);
//            bindProduct(holder.imgProductRight, holder.tvProductNameRight, holder.tvStockRight,
//                    holder.tvPriceRight, holder.tvDescriptionRight, rightProduct);
//
//            holder.imgProductRight.setVisibility(View.VISIBLE);
//            holder.tvProductNameRight.setVisibility(View.VISIBLE);
//            holder.tvStockRight.setVisibility(View.VISIBLE);
//            holder.tvPriceRight.setVisibility(View.VISIBLE);
//            holder.tvDescriptionRight.setVisibility(View.VISIBLE);
//        } else {
//            // Hide the right product views if no right product exists
//            holder.imgProductRight.setVisibility(View.INVISIBLE);
//            holder.tvProductNameRight.setVisibility(View.INVISIBLE);
//            holder.tvStockRight.setVisibility(View.INVISIBLE);
//            holder.tvPriceRight.setVisibility(View.INVISIBLE);
//            holder.tvDescriptionRight.setVisibility(View.INVISIBLE);
//        }

        return convertView;
    }

    private void bindProduct(ImageView imgProduct, TextView tvProductName, TextView tvStock,
                             TextView tvPrice, ProductResponse product) {
        if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(product.getImageUrl())
                    .into(imgProduct);
        } else {
            imgProduct.setImageResource(R.drawable.ic_launcher_background);
        }

        tvProductName.setText(product.getName());
        String stockFormatted = String.format(context.getResources().getString(R.string.product_stock), product.getStock());
        tvStock.setText(stockFormatted);

        String priceFormatted = String.format(context.getResources().getString(R.string.product_price), NumberHelper.formatNumber(product.getPrice()));
        tvPrice.setText(priceFormatted);
    }
}
