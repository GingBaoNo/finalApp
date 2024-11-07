package com.example.firstproject.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.firstproject.Domain.Order;
import com.example.firstproject.R;

import java.util.ArrayList;
import java.util.Map;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.OrderViewHolder> {
    private ArrayList<Order> orderList;

    public OrderListAdapter(ArrayList<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order2, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.nameTextView.setText(order.getName());
        holder.phoneTextView.setText(order.getPhone());
        holder.addressTextView.setText("Địa chỉ:"+order.getAddress());
        holder.totalTextView.setText(order.getTotal());

        StringBuilder foodDetails = new StringBuilder();
        for (Map.Entry<String, String> entry : order.getFoodNames().entrySet()) {
            String foodId = entry.getKey();
            String foodName = entry.getValue();
            Double price = order.getFoodPrices().get(foodId);
            Integer quantity = order.getFoodQuantities().get(foodId);

            foodDetails.append(foodName)
                    .append(" (Giá: ")
                    .append(price)
                    .append("đ, Số lượng: ")
                    .append(quantity)
                    .append(")\n");
        }

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView phoneTextView;
        TextView addressTextView;
        TextView totalTextView;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            phoneTextView = itemView.findViewById(R.id.phoneTextView);
            addressTextView = itemView.findViewById(R.id.addressTextView);
            totalTextView = itemView.findViewById(R.id.totalTextView);// Khởi tạo TextView mới
        }
    }
}