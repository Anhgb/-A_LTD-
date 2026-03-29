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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.doan_ltmb.MainActivity;
import com.example.doan_ltmb.R;
import com.example.doan_ltmb.data.model.Category;
import com.example.doan_ltmb.data.model.Product;
import com.example.doan_ltmb.data.repository.ProductRepository;
import com.example.doan_ltmb.databinding.FragmentHomeBinding;
import com.example.doan_ltmb.ui.adapter.BannerAdapter;
import com.example.doan_ltmb.ui.adapter.CategoryAdapter;
import com.example.doan_ltmb.ui.adapter.ProductAdapter;
import com.example.doan_ltmb.ui.product.ProductDetailActivity;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements HomeContract.View {

    private FragmentHomeBinding binding;
    private ProductAdapter productAdapter;
    private CategoryAdapter categoryAdapter;
    private List<String> bannerImages = new ArrayList<>();
    private Handler bannerHandler = new Handler(Looper.getMainLooper());
    private Runnable bannerRunnable;
    private AutoCompleteTextView etSearchMain;
    
    private HomeContract.Presenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        presenter = new HomePresenter(this, new ProductRepository(requireContext()));
        
        setupRecyclerViews();
        setupBanner();
        setupSearch();
        setupSwipeRefresh();
        
        presenter.loadData();
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
            presenter.filterByCategory(category.getId());
        });
    }

    private void setupBanner() {
        BannerAdapter bannerAdapter = new BannerAdapter(bannerImages);
        binding.bannerViewPager.setAdapter(bannerAdapter);

        new TabLayoutMediator(binding.bannerIndicator, binding.bannerViewPager, (tab, position) -> {
        }).attach();
    }

    private void startAutoSlider() {
        if (bannerRunnable != null) bannerHandler.removeCallbacks(bannerRunnable);
        
        bannerRunnable = () -> {
            if (binding == null || bannerImages.isEmpty()) return;
            int currentItem = binding.bannerViewPager.getCurrentItem();
            int nextItem = (currentItem + 1) % bannerImages.size();
            binding.bannerViewPager.setCurrentItem(nextItem, true);
            bannerHandler.postDelayed(bannerRunnable, 5000);
        };
        bannerHandler.postDelayed(bannerRunnable, 5000);
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
                    presenter.searchProducts(s.toString());
                }
                @Override
                public void afterTextChanged(Editable s) {}
            });

            etSearchMain.setOnItemClickListener((parent, v, position, id) -> {
                String selected = (String) parent.getItemAtPosition(position);
                presenter.searchProducts(selected);
            });
        }
    }

    private void setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener(() -> presenter.loadData());
    }

    @Override
    public void showProgressBar() {
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        binding.progressBar.setVisibility(View.GONE);
        binding.swipeRefresh.setRefreshing(false);
    }

    @Override
    public void showCategories(List<Category> categories) {
        categoryAdapter.setCategories(categories);
    }

    @Override
    public void showProducts(List<Product> products) {
        productAdapter.setProducts(products);
    }

    @Override
    public void showBanners(List<String> bannerImages) {
        this.bannerImages.clear();
        this.bannerImages.addAll(bannerImages);
        if (binding.bannerViewPager.getAdapter() != null) {
            binding.bannerViewPager.getAdapter().notifyDataSetChanged();
        }
        startAutoSlider();
    }

    @Override
    public void updateSearchSuggestions(List<String> productNames) {
        if (getContext() == null || etSearchMain == null) return;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, productNames);
        etSearchMain.setAdapter(adapter);
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!bannerImages.isEmpty()) {
            startAutoSlider();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        bannerHandler.removeCallbacks(bannerRunnable);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (presenter != null) presenter.onDestroy();
        binding = null;
    }
}
