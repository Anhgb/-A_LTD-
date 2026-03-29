package com.example.doan_ltmb.ui.cart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.doan_ltmb.data.CartManager;
import com.example.doan_ltmb.databinding.FragmentCartBinding;
import com.example.doan_ltmb.ui.adapter.CartAdapter;

import java.text.NumberFormat;
import java.util.Locale;

public class CartFragment extends Fragment {

    private FragmentCartBinding binding;
    private CartAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView();
        updateUI();

        binding.btnCheckout.setOnClickListener(v -> {
            if (CartManager.getInstance().getCartItems().isEmpty()) {
                Toast.makeText(getContext(), "Giỏ hàng của bạn đang trống!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Đặt hàng thành công! Cảm ơn bạn.", Toast.LENGTH_LONG).show();
                CartManager.getInstance().clearCart();
                updateUI();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void setupRecyclerView() {
        binding.rvCart.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CartAdapter(CartManager.getInstance().getCartItems());
        adapter.setOnCartChangeListener(this::updateUI);
        binding.rvCart.setAdapter(adapter);
    }

    private void updateUI() {
        if (binding == null) return;
        
        if (CartManager.getInstance().getCartItems().isEmpty()) {
            binding.rvCart.setVisibility(View.GONE);
            binding.bottomCard.setVisibility(View.GONE);
            binding.tvEmptyCart.setVisibility(View.VISIBLE);
        } else {
            binding.rvCart.setVisibility(View.VISIBLE);
            binding.bottomCard.setVisibility(View.VISIBLE);
            binding.tvEmptyCart.setVisibility(View.GONE);
            
            double total = CartManager.getInstance().getTotalPrice();
            NumberFormat vnFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            binding.tvTotalPrice.setText(vnFormat.format(total));
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
