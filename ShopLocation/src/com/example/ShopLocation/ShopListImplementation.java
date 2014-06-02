package com.example.ShopLocation;

import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class ShopListImplementation implements ShopList {
    ArrayList<Shop> list;
    ArrayAdapter<Shop> arrayAdapter;

    private ShopListImplementation() {
    }

    public ArrayAdapter<Shop> createList() {
        //ArrayList to store list items
        list = new ArrayList<Shop>();
        //ArrayAdapter to link the ArrayList to the ListView
        arrayAdapter = new ArrayAdapter<Shop>(ShopLocationApp.getContext(), android.R.layout.simple_list_item_1, list);
        return arrayAdapter;
    }

    public ArrayList<Shop> getList() {
        return list;
    }

    public void add(Shop shop) {
        list.add(shop);
    }

    public void notifyDataSetChanged() {
        arrayAdapter.notifyDataSetChanged();
    }

    public static ShopListFactory factory = new ShopListFactory() {
        public ShopList getShopList() {
            return new ShopListImplementation();
        }
    };

}
