package ru.evotorapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by sergey-rush on 28.11.2017.
 */

public class ProductAdapter extends ArrayAdapter<String> {

    Context context;
    List<Product> productList;
    final NumberFormat currencyInstance = NumberFormat.getCurrencyInstance(new Locale("ru", "RU"));

    public ProductAdapter(Context context, List<Product> productList) {
        super(context, R.layout.product_item);
        this.context = context;
        this.productList = productList;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @NonNull
    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder holder = new ViewHolder();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null) {
            view = inflater.inflate(R.layout.product_item, parent, false);
            holder.tvName = (TextView) view.findViewById(R.id.tvName);

            holder.tvCalc = (TextView) view.findViewById(R.id.tvCalc);
            holder.ivBadge = (ImageView) view.findViewById(R.id.ivBadge);
            holder.tvDiscount = (TextView) view.findViewById(R.id.tvDiscount);
            holder.tvPrice = (TextView) view.findViewById(R.id.tvPrice);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Product product = productList.get(position);
        holder.tvName.setText(product.Name);
        holder.tvName.setTag(product.Id);
        String price = currencyInstance.format(product.Price);
        String quantity = Integer.toString(product.Quantity);
        String label = String.format("%s x %s шт", price, quantity);
        holder.tvCalc.setText(label);
        holder.tvPrice.setText(price);

        if(product.Discount != 0){
            holder.ivBadge.setVisibility(View.VISIBLE);
            holder.ivBadge.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_ATOP);
            String discount = String.format("%s %%", Double.toString(product.Discount));
            holder.tvDiscount.setText(discount);
            holder.tvDiscount.setVisibility(View.VISIBLE);
        }
        return view;
    }

    static class ViewHolder {
        TextView tvName;
        TextView tvCalc;
        ImageView ivBadge;
        TextView tvPrice;
        TextView tvDiscount;
    }
}