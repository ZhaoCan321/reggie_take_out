package com.zz.reggie.common;

import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;

public class CustomException extends RuntimeException{

    public CustomException(String message) {
        super(message);
    }
}
