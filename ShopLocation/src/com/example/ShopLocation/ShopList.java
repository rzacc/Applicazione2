package com.example.ShopLocation;

import android.widget.Adapter;

import java.util.List;

public interface ShopList {
    Adapter createList();

    List<Shop> getList();

    void add(Shop shop);

    void notifyDataSetChanged();
}
