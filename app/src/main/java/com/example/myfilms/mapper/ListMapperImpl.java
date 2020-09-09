package com.example.myfilms.mapper;

import java.util.ArrayList;
import java.util.List;

public class ListMapperImpl <Input, Output> implements ListMapper<Input, Output> {

    private Mapper<Input, Output> mapper;

    public void setMapper(Mapper<Input, Output> mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<Output> map(List<Input> inputs) {
        List<Output> listMapped = new ArrayList<Output>();
        for(Input element : inputs){
            listMapped.add(mapper.map(element));
        }
        return listMapped;
    }
}
