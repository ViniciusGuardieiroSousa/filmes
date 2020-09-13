package com.example.myfilms.repository;

import com.example.myfilms.exceptions.DatabaseException;

import java.util.List;

public interface Repository <T> {

    void insertItem(T item) throws DatabaseException;

    List<T> getSavedItems() throws DatabaseException;

}
