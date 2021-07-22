package com.something.groceryapp.model;

public class Cart {

    String itemName, itemPrice, item_cart_key;
    int itemQty, itemTotalPrice;

    public Cart() {}
    public Cart(String itemName, String itemPrice, int itemQty, String item_cart_key, int itemTotalPrice) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemQty = itemQty;
        this.item_cart_key = item_cart_key;
        this.itemTotalPrice = itemTotalPrice;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getItemQty() {
        return itemQty;
    }

    public void setItemQty(int itemQty) {
        this.itemQty = itemQty;
    }

    public String getItem_cart_key() {
        return item_cart_key;
    }

    public void setItem_cart_key(String item_cart_key) {
        this.item_cart_key = item_cart_key;
    }

    public int getItemTotalPrice() {
        return itemTotalPrice;
    }

    public void setItemTotalPrice(int itemTotalPrice) {
        this.itemTotalPrice = itemTotalPrice;
    }
}

