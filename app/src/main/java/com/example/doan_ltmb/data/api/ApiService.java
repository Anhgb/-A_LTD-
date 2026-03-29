package com.example.doan_ltmb.data.api;

import com.example.doan_ltmb.data.model.Category;
import com.example.doan_ltmb.data.model.Product;
import com.example.doan_ltmb.data.model.User;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @POST("auth/register")
    Call<RegisterResponse> register(@Body Map<String, String> user);

    @POST("auth/login")
    Call<LoginResponse> login(@Body Map<String, String> credentials);

    @GET("user/profile")
    Call<User> getProfile();

    @GET("categories")
    Call<List<Category>> getCategories();

    @GET("products")
    Call<List<Product>> getProducts(
            @Query("category_id") String categoryId,
            @Query("search") String search,
            @Query("sort") String sort,
            @Query("page") int page,
            @Query("limit") int limit
    );

    @GET("products/{id}")
    Call<Product> getProductDetail(@Path("id") String id);

    class LoginResponse {
        public String token;
        public User user;
    }

    class RegisterResponse {
        public String status;
        public String message;
        public String user_id;
    }
}
