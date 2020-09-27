package com.example.myfilms.core.repository;

import java.util.List;

public interface EnqueueListener<T> {

    void doOnResponse(List<T> listResponse);

    void doOnFailure(Throwable throwable);
}
