package com.example.doan_ltmb.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Product {
    private String id;
    private String name;
    private String description;
    private double price;
    private String image;
    @SerializedName("category_id")
    private String categoryId;
    private List<ColorOption> colors;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }

    public List<ColorOption> getColors() { return colors; }
    public void setColors(List<ColorOption> colors) { this.colors = colors; }
}
