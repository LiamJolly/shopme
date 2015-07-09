package com.liamdjolly.shopme.entities;

import java.util.ArrayList;
import java.util.List;

public class ShoppingList {
    private int id;
    private String name;
    private List<Item> items = new ArrayList<Item>();

    public ShoppingList(){

    }

    public ShoppingList(final int id, final String name, final List<Item> items) {
        this.id = id;
        this.name = name;
        this.items = items;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Item> getItems() {
        return items;
    }
}
