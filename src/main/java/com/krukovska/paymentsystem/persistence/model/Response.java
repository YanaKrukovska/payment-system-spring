package com.krukovska.paymentsystem.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Response<T> {

    private T object;
    private List<String> errors = new ArrayList<>();

    public Response(T object) {
        this.object = object;
    }

    public Response(List<String> errors) {
        this.errors = errors;
    }

    public boolean isOkay() {
        return errors.isEmpty();
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

}
