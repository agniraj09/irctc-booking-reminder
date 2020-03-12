package com.arc.agni.irctcbookingreminder.bean;

import java.util.ArrayList;
import java.util.List;

public class CalendarResponse {

    private List<Items> items = new ArrayList<>();

    public List<Items> getItems() {
        return items;
    }

    public void setItems(List<Items> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return items.toString();
    }

}
