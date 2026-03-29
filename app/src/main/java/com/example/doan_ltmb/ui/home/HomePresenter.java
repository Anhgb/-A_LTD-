package com.example.doan_ltmb.ui.home;

import com.example.doan_ltmb.data.model.Category;
import com.example.doan_ltmb.data.model.Product;
import com.example.doan_ltmb.data.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HomePresenter implements HomeContract.Presenter {

    private HomeContract.View view;
    private ProductRepository repository;
    private List<Product> allProducts = new ArrayList<>();

    public HomePresenter(HomeContract.View view, ProductRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void loadData() {
        if (view != null) view.showProgressBar();

        // Load Banners
        List<String> banners = new ArrayList<>();
        banners.add("https://zerdio.com.vn/wp-content/uploads/2021/04/mu-snapback-nam-sn36-1.jpg");
        banners.add("https://zerdio.com.vn/wp-content/uploads/2021/04/non-snapback-SN50.jpg");
        banners.add("https://zerdio.com.vn/wp-content/uploads/2021/05/Mu-Snapback-World-Wide-SN64-1.jpg");
        banners.add("https://zerdio.com.vn/wp-content/uploads/2021/12/mu-len-beanie-ML021.jpg");
        banners.add("https://zerdio.com.vn/wp-content/uploads/2021/04/Mu-Snapback-CASH-Phong-Cach-Hiphop-Ca-Tinh-1-1.jpg");

        if (view != null) view.showBanners(banners);

        // Load Categories
        List<Category> categories = new ArrayList<>();
        categories.add(createCategory("0", "Tất cả"));
        categories.add(createCategory("1", "Snapback"));
        categories.add(createCategory("2", "Mũ Lưỡi Trai"));
        categories.add(createCategory("3", "Mũ Len"));
        
        if (view != null) view.showCategories(categories);

        // Load Products
        allProducts = repository.getAllProducts();
        
        if (view != null) {
            view.showProducts(allProducts);
            
            List<String> productNames = allProducts.stream()
                    .map(Product::getName)
                    .collect(Collectors.toList());
            view.updateSearchSuggestions(productNames);
            
            view.hideProgressBar();
        }
    }

    @Override
    public void filterByCategory(String categoryId) {
        if (allProducts == null || allProducts.isEmpty()) return;

        if (categoryId.equals("0")) {
            if (view != null) view.showProducts(allProducts);
        } else {
            List<Product> filtered = allProducts.stream()
                    .filter(p -> p.getCategoryId() != null && p.getCategoryId().equals(categoryId))
                    .collect(Collectors.toList());
            if (view != null) view.showProducts(filtered);
        }
    }

    @Override
    public void searchProducts(String query) {
        if (allProducts == null) return;
        
        List<Product> filtered = allProducts.stream()
                .filter(p -> p.getName().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
        if (view != null) view.showProducts(filtered);
    }

    @Override
    public void onDestroy() {
        view = null;
    }

    private Category createCategory(String id, String name) {
        Category c = new Category();
        c.setId(id);
        c.setName(name);
        return c;
    }
}
