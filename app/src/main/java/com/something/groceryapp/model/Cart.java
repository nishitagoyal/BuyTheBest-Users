package com.something.groceryapp.model;

public class Cart {

    private String ItemDetail;
    private String ItemPrice;
    private String ItemTotalPrice;

    public Cart(String itemDetail, String itemPrice, String itemTotalPrice) {
        ItemDetail = itemDetail;
        ItemPrice = itemPrice;
        ItemTotalPrice = itemTotalPrice;
    }

    public String getItemPrice() {
        return ItemPrice;
    }

    public void setItemPrice(String itemPrice) {
        ItemPrice = itemPrice;
    }

    public String getItemDetail() {
        return ItemDetail;
    }

    public void setItemDetail(String itemDetail) {
        ItemDetail = itemDetail;
    }

    public String getItemTotalPrice() {
        return ItemTotalPrice;
    }

    public void setItemTotalPrice(String itemTotalPrice) {
        ItemTotalPrice = itemTotalPrice;
    }
}

