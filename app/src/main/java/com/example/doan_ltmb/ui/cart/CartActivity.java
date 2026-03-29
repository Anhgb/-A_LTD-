package com.example.doan_ltmb.ui.cart;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.doan_ltmb.data.CartManager;
import com.example.doan_ltmb.databinding.ActivityCartBinding;
import com.example.doan_ltmb.ui.adapter.CartAdapter;

import java.text.NumberFormat;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {

    private ActivityCartBinding binding;
    private CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Giỏ hàng");
        }

        setupRecyclerView();
        updateUI();

        binding.btnCheckout.setOnClickListener(v -> {
            if (CartManager.getInstance().getCartItems().isEmpty()) {
                Toast.makeText(this, "Giỏ hàng đang trống", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Thanh toán thành công!", Toast.LENGTH_LONG).show();
                CartManager.getInstance().clearCart();
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void setupRecyclerView() {
        adapter = new CartAdapter(CartManager.getInstance().getCartItems());
        adapter.setOnCartChangeListener(this::updateUI);
        binding.rvCart.setLayoutManager(new LinearLayoutManager(this));
        binding.rvCart.setAdapter(adapter);
    }

    private void updateUI() {
        if (CartManager.getInstance().getCartItems().isEmpty()) {
            binding.rvCart.setVisibility(View.GONE);
            binding.bottomCard.setVisibility(View.GONE);
            // Có thể thêm một view "Giỏ hàng trống" ở đây
        } else {
            binding.rvCart.setVisibility(View.VISIBLE);
            binding.bottomCard.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
            updateTotalPrice();
        }
    }

    private void updateTotalPrice() {
        NumberFormat vnFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        binding.tvTotalPrice.setText(vnFormat.format(CartManager.getInstance().getTotalPrice()));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
