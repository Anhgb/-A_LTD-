package com.example.doan_ltmb.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.doan_ltmb.MainActivity;
import com.example.doan_ltmb.R;
import com.example.doan_ltmb.data.model.Category;
import com.example.doan_ltmb.data.model.Product;
import com.example.doan_ltmb.databinding.FragmentHomeBinding;
import com.example.doan_ltmb.ui.adapter.BannerAdapter;
import com.example.doan_ltmb.ui.adapter.CategoryAdapter;
import com.example.doan_ltmb.ui.adapter.ProductAdapter;
import com.example.doan_ltmb.ui.product.ProductDetailActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ProductAdapter productAdapter;
    private CategoryAdapter categoryAdapter;
    private List<Product> allProducts = new ArrayList<>();
    private List<String> bannerImages = new ArrayList<>();
    private Handler bannerHandler = new Handler(Looper.getMainLooper());
    private Runnable bannerRunnable;
    private AutoCompleteTextView etSearchMain;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        setupRecyclerViews();
        setupBanner();
        setupSearch();
        setupSwipeRefresh();
        loadData();
    }

    private void setupRecyclerViews() {
        productAdapter = new ProductAdapter();
        binding.rvProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.rvProducts.setAdapter(productAdapter);
        productAdapter.setOnProductClickListener(product -> {
            Intent intent = new Intent(getContext(), ProductDetailActivity.class);
            intent.putExtra(ProductDetailActivity.EXTRA_PRODUCT_ID, product.getId());
            startActivity(intent);
        });

        categoryAdapter = new CategoryAdapter();
        binding.rvCategories.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvCategories.setAdapter(categoryAdapter);
        categoryAdapter.setOnCategoryClickListener(category -> {
            filterProductsByCategory(category.getId());
        });
    }

    private void setupBanner() {
        bannerImages.clear();
        // SỬ DỤNG LINK BANNER THẬT VÀ ỔN ĐỊNH
        bannerImages.add("https://zerdio.com.vn/wp-content/uploads/2021/04/mu-snapback-nam-sn36-1.jpg");
        bannerImages.add("https://zerdio.com.vn/wp-content/uploads/2021/04/non-snapback-SN50.jpgkl");
        bannerImages.add("https://zerdio.com.vn/wp-content/uploads/2021/05/Mu-Snapback-World-Wide-SN64-1.jpg");
        bannerImages.add("https://zerdio.com.vn/wp-content/uploads/2021/12/mu-len-beanie-ML021.jpg");
        bannerImages.add("https://zerdio.com.vn/wp-content/uploads/2021/04/Mu-Snapback-CASH-Phong-Cach-Hiphop-Ca-Tinh-1-1.jpg");
        BannerAdapter bannerAdapter = new BannerAdapter(bannerImages);
        binding.bannerViewPager.setAdapter(bannerAdapter);

        if (bannerRunnable != null) bannerHandler.removeCallbacks(bannerRunnable);

        bannerRunnable = () -> {
            if (binding == null || bannerImages.isEmpty()) return;
            int currentItem = binding.bannerViewPager.getCurrentItem();
            int nextItem = (currentItem + 1) % bannerImages.size();
            binding.bannerViewPager.setCurrentItem(nextItem, true);
            bannerHandler.postDelayed(bannerRunnable, 4000);
        };
        bannerHandler.postDelayed(bannerRunnable, 4000);
    }

    private void setupSearch() {
        if (getActivity() instanceof MainActivity) {
            etSearchMain = getActivity().findViewById(R.id.etSearchMain);
        }

        if (etSearchMain != null) {
            etSearchMain.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    filterProducts(s.toString());
                }
                @Override
                public void afterTextChanged(Editable s) {}
            });

            etSearchMain.setOnItemClickListener((parent, v, position, id) -> {
                String selected = (String) parent.getItemAtPosition(position);
                filterProducts(selected);
            });
        }
    }

    private void setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener(this::loadData);
    }

    private void loadData() {
        binding.progressBar.setVisibility(View.VISIBLE);

        // 1. Load Categories
        List<Category> categories = new ArrayList<>();
        categories.add(createCategory("0", "Tất cả"));
        categories.add(createCategory("1", "Snapback"));
        categories.add(createCategory("2", "Mũ Lưỡi Trai"));
        categories.add(createCategory("3", "Mũ Len"));
        categoryAdapter.setCategories(categories);

        // 2. Load Products từ JSON
        allProducts.clear();
        String json = loadJSONFromAsset("products.json");
        if (json != null) {
            try {
                Gson gson = new Gson();
                Type listType = new TypeToken<List<Product>>(){}.getType();
                List<Product> products = gson.fromJson(json, listType);
                if (products != null) {
                    allProducts.addAll(products);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (allProducts.isEmpty()) {
            // Dữ liệu fallback nếu JSON lỗi
            Product p = new Product();
            p.setId("0");
            p.setName("Đang cập nhật...");
            p.setPrice(0);
            allProducts.add(p);
        }

        productAdapter.setProducts(allProducts);
        updateSearchSuggestions();

        binding.progressBar.setVisibility(View.GONE);
        binding.swipeRefresh.setRefreshing(false);
    }

    private String loadJSONFromAsset(String fileName) {
        try {
            InputStream is = requireContext().getAssets().open(fileName);
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

    private Category createCategory(String id, String name) {
        Category c = new Category();
        c.setId(id);
        c.setName(name);
        return c;
    }

    private void updateSearchSuggestions() {
        if (getContext() == null || etSearchMain == null) return;
        List<String> productNames = allProducts.stream()
                .map(Product::getName)
                .collect(Collectors.toList());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, productNames);
        etSearchMain.setAdapter(adapter);
    }

    private void filterProducts(String query) {
        List<Product> filtered = allProducts.stream()
                .filter(p -> p.getName().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
        productAdapter.setProducts(filtered);
    }

    private void filterProductsByCategory(String categoryId) {
        if (categoryId.equals("0")) {
            productAdapter.setProducts(allProducts);
        } else {
            List<Product> filtered = allProducts.stream()
                    .filter(p -> p.getCategoryId() != null && p.getCategoryId().equals(categoryId))
                    .collect(Collectors.toList());
            productAdapter.setProducts(filtered);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (bannerRunnable != null) bannerHandler.postDelayed(bannerRunnable, 4000);
    }

    @Override
    public void onPause() {
        super.onPause();
        bannerHandler.removeCallbacks(bannerRunnable);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
