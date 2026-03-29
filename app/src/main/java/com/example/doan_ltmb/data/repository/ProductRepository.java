package com.example.doan_ltmb.data.repository;

import android.content.Context;
import com.example.doan_ltmb.data.model.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {
    private Context context;

    public ProductRepository(Context context) {
        this.context = context;
    }

    public List<Product> getAllProducts() {
        String json = null;
        try {
            InputStream is = context.getAssets().open("products.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }

        Gson gson = new Gson();
        Type listType = new TypeToken<List<Product>>() {}.getType();
        return gson.fromJson(json, listType);
    }
}
