package com.konrad.task_manager.model.customs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomResponseImpl<T> implements CustomResponse {
    private String message;
    private T t;

    public CustomResponseImpl(String message, T t) {

        this.message = message;
        this.t = t;
    }
}