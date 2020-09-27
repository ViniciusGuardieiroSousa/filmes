package com.example.myfilms.core.interactors;

public abstract class InputUseCase<Input> implements UseCase{

    public abstract void invoke(Input input) throws Exception;
}
