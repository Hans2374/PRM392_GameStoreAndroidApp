package com.example.gamestore.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamestore.R;
import com.example.gamestore.models.CartItem;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartItems;
    private Context context;
    private OnCartItemListener listener;

    public interface OnCartItemListener {
        void onQuantityChanged(CartItem item, int newQuantity);
        void onDeleteClick(CartItem item);
    }

    public CartAdapter(Context context, List<CartItem> cartItems, OnCartItemListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);

        holder.imgItem.setImageResource(item.getImageResource());
        holder.tvName.setText(item.getName());
        holder.tvDescription.setText(item.getDescription());
        holder.tvPrice.setText("$" + item.getPrice());
        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));
        holder.tvTotal.setText("Total: $" + item.getTotalPrice());

        // Quantity controls
        holder.btnDecrease.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                listener.onQuantityChanged(item, item.getQuantity() - 1);
            }
        });

        holder.btnIncrease.setOnClickListener(v -> {
            listener.onQuantityChanged(item, item.getQuantity() + 1);
        });

        holder.btnDelete.setOnClickListener(v -> {
            listener.onDeleteClick(item);
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public void updateData(List<CartItem> newItems) {
        this.cartItems = newItems;
        notifyDataSetChanged();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imgItem;
        TextView tvName, tvDescription, tvPrice, tvQuantity, tvTotal;
        Button btnDecrease, btnIncrease, btnDelete;

        CartViewHolder(View itemView) {
            super(itemView);
            imgItem = itemView.findViewById(R.id.imgCartItem);
            tvName = itemView.findViewById(R.id.tvCartItemName);
            tvDescription = itemView.findViewById(R.id.tvCartItemDescription);
            tvPrice = itemView.findViewById(R.id.tvCartItemPrice);
            tvQuantity = itemView.findViewById(R.id.tvCartQuantity);
            tvTotal = itemView.findViewById(R.id.tvCartTotal);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}