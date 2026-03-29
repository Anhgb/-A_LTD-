package com.example.doan_ltmb.data.model;

public class ColorOption {
    private String colorCode;
    private String imageUrl;

    public ColorOption(String colorCode, String imageUrl) {
        this.colorCode = colorCode;
        this.imageUrl = imageUrl;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
