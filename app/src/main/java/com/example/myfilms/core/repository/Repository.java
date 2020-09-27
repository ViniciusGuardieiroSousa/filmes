package com.example.myfilms.core.repository;

import java.util.List;

public interface Repository <T> {

    void insertItem(T item) throws Exception;

    List<T> getSavedItems() throws Exception;

    void searchItem(String itemName) throws Exception;

    void setEnqueueListener(EnqueueListener<T> enqueueListener);

}
