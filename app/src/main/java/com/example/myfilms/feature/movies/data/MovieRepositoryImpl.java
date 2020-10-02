package com.example.myfilms.feature.movies.data;

import com.example.myfilms.core.repository.DataSource;
import com.example.myfilms.core.repository.EnqueueListener;
import com.example.myfilms.core.repository.Repository;
import com.example.myfilms.feature.movies.domain.entity.Movie;

import java.util.List;

public class MovieRepositoryImpl implements Repository<Movie> {

    private DataSource<Movie> dataSource;

    public MovieRepositoryImpl(DataSource<Movie> dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public void insertItem(Movie item) throws Exception {
        dataSource.insertItem(item);
    }

    @Override
    public List<Movie> getSavedItems() throws Exception {
        return dataSource.getSavedItems();
    }

    @Override
    public void searchItem(String itemName) throws Exception{
        dataSource.searchItem(itemName);
    }

    @Override
    public void setEnqueueListener(EnqueueListener<Movie> enqueueListener) {
        dataSource.insertEnqueueListener(enqueueListener);
    }

}
