package com.example.doan_ltmb.ui.product;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.doan_ltmb.data.model.Product;
import com.example.doan_ltmb.databinding.ActivityCheckoutBinding;
import com.google.gson.Gson;

import java.text.NumberFormat;
import java.util.Locale;

public class CheckoutActivity extends AppCompatActivity {

    public static final String EXTRA_PRODUCT_JSON = "extra_product_json";
    private ActivityCheckoutBinding binding;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String productJson = getIntent().getStringExtra(EXTRA_PRODUCT_JSON);
        if (productJson != null) {
            product = new Gson().fromJson(productJson, Product.class);
            displayProductInfo();
        }

        binding.toolbar.setNavigationOnClickListener(v -> finish());

        binding.btnPlaceOrder.setOnClickListener(v -> {
            Toast.makeText(this, "Đặt hàng thành công!", Toast.LENGTH_LONG).show();
            finish();
        });
    }

    private void displayProductInfo() {
        if (product == null) return;

        binding.tvProductNameCheckout.setText(product.getName());
        NumberFormat vnFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String priceStr = vnFormat.format(product.getPrice());
        
        binding.tvProductPriceCheckout.setText(priceStr);
        binding.tvTotalCheckout.setText(priceStr);
        
        Glide.with(this)
                .load(product.getImage())
                .centerCrop()
                .into(binding.ivProductCheckout);
    }
}
