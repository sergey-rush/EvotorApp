package ru.evotorapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sergey-rush on 13.12.2017.
 */
public class SlipAdapter extends ArrayAdapter<String> {

    Context context;
    List<Item> itemList;

    public SlipAdapter(Context context, List<Item> itemList) {
        super(context, R.layout.slip_item);
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @NonNull
    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder holder = new ViewHolder();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null) {
            view = inflater.inflate(R.layout.slip_item, parent, false);
            holder.tvId = (TextView) view.findViewById(R.id.tvId);
            holder.tvName = (TextView) view.findViewById(R.id.tvName);
            holder.tvQuantity = (TextView) view.findViewById(R.id.tvQuantity);
            holder.tvPrice = (TextView) view.findViewById(R.id.tvPrice);
            holder.tvDiscountPercents = (TextView) view.findViewById(R.id.tvDiscountPercents);
            holder.tvDiscountPositionSum = (TextView) view.findViewById(R.id.tvDiscountPositionSum);
            holder.tvPriceWithDiscountPosition = (TextView) view.findViewById(R.id.tvPriceWithDiscountPosition);
            holder.tvTotalWithoutDiscounts = (TextView) view.findViewById(R.id.tvTotalWithoutDiscounts);
            holder.tvTotal = (TextView) view.findViewById(R.id.tvTotal);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Item item = itemList.get(position);
        holder.tvId.setText(item.Id);
        holder.tvName.setText(item.Name);
        holder.tvQuantity.setText(item.Quantity.toString());
        holder.tvPrice.setText(item.Price.toString());
        holder.tvDiscountPercents.setText(item.DiscountPercents.toString());
        holder.tvDiscountPositionSum.setText(item.DiscountPositionSum.toString());
        holder.tvPriceWithDiscountPosition.setText(item.PriceWithDiscountPosition.toString());
        holder.tvTotalWithoutDiscounts.setText(item.TotalWithoutDiscounts.toString());
        holder.tvTotal.setText(item.Total.toString());
        return view;
    }

    static class ViewHolder {
        TextView tvId;
        TextView tvName;
        TextView tvQuantity;
        TextView tvPrice;
        TextView tvDiscountPercents;
        TextView tvDiscountPositionSum;
        TextView tvPriceWithDiscountPosition;
        TextView tvTotalWithoutDiscounts;
        TextView tvTotal;
    }
}