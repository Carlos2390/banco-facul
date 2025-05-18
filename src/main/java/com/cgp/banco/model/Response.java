package com.cgp.banco.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Response {
    private String message;
    private int status;
    private Object data;
}