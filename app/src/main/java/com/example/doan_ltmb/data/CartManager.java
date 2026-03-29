package com.example.doan_ltmb.data;

import com.example.doan_ltmb.data.model.CartItem;
import com.example.doan_ltmb.data.model.Product;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private List<CartItem> cartItems;

    private CartManager() {
        cartItems = new ArrayList<>();
    }

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void addProduct(Product product) {
        for (CartItem item : cartItems) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.setQuantity(item.getQuantity() + 1);
                return;
            }
        }
        cartItems.add(new CartItem(product, 1));
    }

    public void updateQuantity(String productId, int delta) {
        for (int i = 0; i < cartItems.size(); i++) {
            CartItem item = cartItems.get(i);
            if (item.getProduct().getId().equals(productId)) {
                int newQty = item.getQuantity() + delta;
                if (newQty <= 0) {
                    cartItems.remove(i);
                } else {
                    item.setQuantity(newQty);
                }
                return;
            }
        }
    }

    public void removeItem(String productId) {
        cartItems.removeIf(item -> item.getProduct().getId().equals(productId));
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public double getTotalPrice() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }
        return total;
    }

    public void clearCart() {
        cartItems.clear();
    }
}
