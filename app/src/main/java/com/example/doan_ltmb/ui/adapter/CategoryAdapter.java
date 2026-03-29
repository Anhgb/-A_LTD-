package com.example.doan_ltmb.ui.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_ltmb.data.model.Category;
import com.example.doan_ltmb.databinding.ItemCategoryBinding;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<Category> categories = new ArrayList<>();
    private OnCategoryClickListener listener;
    private int selectedPosition = 0; // Mặc định chọn "Tất cả" (vị trí 0)

    public interface OnCategoryClickListener {
        void onCategoryClick(Category category);
    }

    public void setOnCategoryClickListener(OnCategoryClickListener listener) {
        this.listener = listener;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCategoryBinding binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.bind(categories.get(position), position == selectedPosition);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        private final ItemCategoryBinding binding;

        public CategoryViewHolder(ItemCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Category category, boolean isSelected) {
            binding.tvCategoryName.setText(category.getName());

            if (isSelected) {
                // MÀU KHI ĐƯỢC CHỌN (Ví dụ: Nền hồng tím, chữ trắng)
                binding.getRoot().setCardBackgroundColor(Color.parseColor("#E1BEE7")); // Màu tím nhạt
                binding.tvCategoryName.setTextColor(Color.parseColor("#7B1FA2")); // Màu tím đậm
                binding.getRoot().setStrokeWidth(2);
                binding.getRoot().setStrokeColor(Color.parseColor("#7B1FA2"));
            } else {
                // MÀU MẶC ĐỊNH (Nền trắng, chữ đen)
                binding.getRoot().setCardBackgroundColor(Color.WHITE);
                binding.tvCategoryName.setTextColor(Color.BLACK);
                binding.getRoot().setStrokeWidth(0);
            }

            binding.getRoot().setOnClickListener(v -> {
                int previousSelected = selectedPosition;
                selectedPosition = getAdapterPosition();
                
                // Cập nhật lại giao diện cho 2 item cũ và mới
                notifyItemChanged(previousSelected);
                notifyItemChanged(selectedPosition);

                if (listener != null) {
                    listener.onCategoryClick(category);
                }
            });
        }
    }
}
