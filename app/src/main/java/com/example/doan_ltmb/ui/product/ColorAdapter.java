package com.example.doan_ltmb.ui.product;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.doan_ltmb.data.model.ColorOption;
import com.example.doan_ltmb.databinding.ItemColorSelectorBinding;
import java.util.List;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorViewHolder> {

    private final List<ColorOption> colors;
    private int selectedPosition = 0;
    private OnColorSelectedListener listener;

    public interface OnColorSelectedListener {
        void onColorSelected(ColorOption colorOption);
    }

    public ColorAdapter(List<ColorOption> colors, OnColorSelectedListener listener) {
        this.colors = colors;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ColorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemColorSelectorBinding binding = ItemColorSelectorBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ColorViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorViewHolder holder, int position) {
        holder.bind(colors.get(position), position == selectedPosition);
    }

    @Override
    public int getItemCount() {
        return colors != null ? colors.size() : 0;
    }

    class ColorViewHolder extends RecyclerView.ViewHolder {
        private final ItemColorSelectorBinding binding;

        public ColorViewHolder(ItemColorSelectorBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ColorOption option, boolean isSelected) {
            try {
                binding.viewColor.getBackground().setColorFilter(Color.parseColor(option.getColorCode()), PorterDuff.Mode.SRC_IN);
            } catch (Exception e) {
                binding.viewColor.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
            }
            
            binding.ivSelected.setVisibility(isSelected ? View.VISIBLE : View.GONE);
            
            binding.getRoot().setOnClickListener(v -> {
                int oldPosition = selectedPosition;
                selectedPosition = getAdapterPosition();
                notifyItemChanged(oldPosition);
                notifyItemChanged(selectedPosition);
                if (listener != null) {
                    listener.onColorSelected(option);
                }
            });
        }
    }
}
