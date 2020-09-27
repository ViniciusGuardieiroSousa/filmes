package com.example.myfilms.core.repository;

import java.util.List;

public interface DataSource <Type> {

    void insertItem(Type item) throws Exception;

    List<Type> getSavedItems() throws Exception;

    void searchItem(String itemName) throws Exception;

    void insertEnqueueListener(EnqueueListener enqueueListener);
}
