package com.example.myfilms.core.mapper;

public interface Mapper<Input, Output> {
    Output map(Input input);
}
