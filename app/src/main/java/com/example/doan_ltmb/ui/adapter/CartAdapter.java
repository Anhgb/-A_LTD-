package com.example.doan_ltmb.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doan_ltmb.data.CartManager;
import com.example.doan_ltmb.data.model.CartItem;
import com.example.doan_ltmb.databinding.ItemCartBinding;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> items;
    private OnCartChangeListener listener;

    public interface OnCartChangeListener {
        void onCartChanged();
    }

    public CartAdapter(List<CartItem> items) {
        this.items = items;
    }

    public void setOnCartChangeListener(OnCartChangeListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCartBinding binding = ItemCartBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CartViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        private final ItemCartBinding binding;

        public CartViewHolder(ItemCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(CartItem item) {
            binding.tvProductNameCart.setText(item.getProduct().getName());

            NumberFormat vnFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            binding.tvProductPriceCart.setText(vnFormat.format(item.getProduct().getPrice()));

            binding.tvQuantity.setText(String.valueOf(item.getQuantity()));

            Glide.with(binding.ivProductCart.getContext())
                    .load(item.getProduct().getImage())
                    .into(binding.ivProductCart);

            binding.btnPlus.setOnClickListener(v -> {
                CartManager.getInstance().updateQuantity(item.getProduct().getId(), 1);
                notifyDataSetChanged();
                if (listener != null) listener.onCartChanged();
            });

            binding.btnMinus.setOnClickListener(v -> {
                CartManager.getInstance().updateQuantity(item.getProduct().getId(), -1);
                notifyDataSetChanged();
                if (listener != null) listener.onCartChanged();
            });

            binding.btnRemove.setOnClickListener(v -> {
                CartManager.getInstance().removeItem(item.getProduct().getId());
                notifyDataSetChanged();
                if (listener != null) listener.onCartChanged();
            });
        }
    }
}
