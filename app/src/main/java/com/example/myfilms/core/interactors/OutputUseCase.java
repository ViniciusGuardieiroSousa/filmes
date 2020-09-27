package com.example.myfilms.core.interactors;

public abstract class OutputUseCase<Output> implements UseCase{

    public abstract Output invoke() throws Exception;
}
