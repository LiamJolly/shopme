package com.liamdjolly.shopme.entities;

public class Item {

    private long id;
    private boolean isDone;
    private String value;

    public Item(final long id, final String value, final boolean isDone) {
        this.id = id;
        this.value = value;
        this.isDone = isDone;
    }

    public long getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean isDone) {
        this.isDone = isDone;
    }
}
