package com.memariyan.optimizer.utils;

import java.util.List;

public class SparseList<T extends SparseListModel> {

    public SparseList(List<T> models) {
        this.models = models;
    }

    private final List<T> models;

    //TODO: check the performance
    public T get(int index) {
        for (T v : models) {
            if (index >= v.getFromIndex() && index <= v.getToIndex()) {
                return v;
            }
        }
        return null;
    }

    public int size() {
        if (models.isEmpty()) {
            return 0;
        }
        return models.getLast().getToIndex();
    }

    public boolean isEmpty() {
        return size() == 0;
    }

}
