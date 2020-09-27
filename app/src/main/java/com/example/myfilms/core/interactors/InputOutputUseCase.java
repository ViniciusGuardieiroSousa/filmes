package com.example.myfilms.core.interactors;

public abstract class InputOutputUseCase<Input, Output>implements UseCase{

    public abstract Output invoke(Input input);
}
