package com.example.doan_ltmb.ui.home;

import com.example.doan_ltmb.data.model.Category;
import com.example.doan_ltmb.data.model.Product;
import java.util.List;

public interface HomeContract {
    interface View {
        void showProgressBar();
        void hideProgressBar();
        void showCategories(List<Category> categories);
        void showProducts(List<Product> products);
        void showBanners(List<String> bannerImages);
        void updateSearchSuggestions(List<String> productNames);
        void showErrorMessage(String message);
    }

    interface Presenter {
        void loadData();
        void filterByCategory(String categoryId);
        void searchProducts(String query);
        void onDestroy();
    }
}
