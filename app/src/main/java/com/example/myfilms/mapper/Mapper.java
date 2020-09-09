package com.example.myfilms.mapper;

public interface Mapper<Input, Output> {
    public Output map(Input input);
}
