package com.example.bulbspos;

public class CartItem {
    int id;
    Product product;
    double qty;

    public CartItem(Product product, double qty) {
        this.id = 1;
        this.product = product;
        this.qty = qty;
    }

    public void setQty(double newQty) {
        qty = newQty;
    }

    public double getTotal() {
        return product.price * qty;
    }
}
