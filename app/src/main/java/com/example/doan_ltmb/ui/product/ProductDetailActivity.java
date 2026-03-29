package com.example.doan_ltmb.ui.product;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.doan_ltmb.MainActivity;
import com.example.doan_ltmb.data.CartManager;
import com.example.doan_ltmb.data.model.ColorOption;
import com.example.doan_ltmb.data.model.Product;
import com.example.doan_ltmb.databinding.ActivityProductDetailBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity {

    public static final String EXTRA_PRODUCT_ID = "extra_product_id";
    private ActivityProductDetailBinding binding;
    private Product currentProduct;
    private ColorOption selectedColorOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Transparent status bar
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        String productId = getIntent().getStringExtra(EXTRA_PRODUCT_ID);
        if (productId != null) {
            loadProductDetail(productId);
        }

        binding.btnBack.setOnClickListener(v -> finish());

        binding.btnCartTop.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("open_cart", true);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        binding.btnAddToCartBottom.setOnClickListener(v -> {
            if (currentProduct != null) {
                CartManager.getInstance().addProduct(currentProduct);
                String msg = "Đã thêm vào giỏ hàng!";
                if (selectedColorOption != null) {
                    msg += " (Màu: " + selectedColorOption.getColorCode() + ")";
                }
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnBuyNow.setOnClickListener(v -> {
            if (currentProduct != null) {
                Intent intent = new Intent(this, CheckoutActivity.class);
                intent.putExtra(CheckoutActivity.EXTRA_PRODUCT_JSON, new Gson().toJson(currentProduct));
                startActivity(intent);
            }
        });
    }

    private void loadProductDetail(String id) {
        try {
            String jsonStr = loadJSONFromAsset("products.json");
            if (jsonStr != null) {
                Gson gson = new Gson();
                Type listType = new TypeToken<List<Product>>(){}.getType();
                List<Product> products = gson.fromJson(jsonStr, listType);

                if (products != null) {
                    for (Product p : products) {
                        if (p.getId().equals(id)) {
                            currentProduct = p;
                            break;
                        }
                    }
                }
            }

            if (currentProduct != null) {
                displayProduct(currentProduct);
            } else {
                Toast.makeText(this, "Không tìm thấy sản phẩm!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String loadJSONFromAsset(String fileName) {
        try {
            InputStream is = getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private void displayProduct(Product product) {
        binding.tvProductNameDetail.setText(product.getName());
        NumberFormat vnFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        binding.tvPrice.setText(vnFormat.format(product.getPrice()));
        binding.tvDescription.setText(product.getDescription());
        
        // Load ảnh mặc định ban đầu
        updateProductImage(product.getImage());

        // Hiển thị danh sách màu sắc
        if (product.getColors() != null && !product.getColors().isEmpty()) {
            selectedColorOption = product.getColors().get(0);
            ColorAdapter colorAdapter = new ColorAdapter(product.getColors(), option -> {
                selectedColorOption = option;
                // CẬP NHẬT ẢNH KHI CHỌN MÀU
                updateProductImage(option.getImageUrl());
            });
            binding.rvColors.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            binding.rvColors.setAdapter(colorAdapter);
        } else {
            binding.rvColors.setVisibility(View.GONE);
        }
    }

    private void updateProductImage(String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this)
                    .load(imageUrl)
                    .centerCrop()
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .into(binding.ivProductLarge);
        }
    }
}
